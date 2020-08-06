package com.google.blockly.android.webview.utility;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.Toast;

import com.google.blockly.android.webview.R;
import com.google.blockly.android.webview.demo.MainActivity;

import java.util.concurrent.atomic.AtomicBoolean;


public class STT implements Codes {
    private static STT instance;
    private final AtomicBoolean isListening = new AtomicBoolean(false);
    private final AtomicBoolean isBusyAskingForInfo = new AtomicBoolean(false);

    public static STT getInstance() {
        if (instance == null) {
            return instance = new STT();
        } else {
            return instance;
        }
    }

    public AtomicBoolean getIsListening() {
        return isListening;
    }

    public void setIsListening(boolean isListening) {
        this.isListening.set(isListening);
    }

    public AtomicBoolean getIsBusyAskingForInfo() {
        return isBusyAskingForInfo;
    }

    public void setIsBusyAskingForInfo(boolean isBusyAskingForInfo) {
        this.isBusyAskingForInfo.set(isBusyAskingForInfo);
    }

    public synchronized boolean stt(final Context mContext, final MainActivity mainActivity,
                                    final int requestCode, String lang) {
        if (getIsBusyAskingForInfo().get() || getIsListening().get()) {
            return false;
        }
        try {
            if (isNetworkAvailable(mContext)) {
                Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.
                        ACTION_RECOGNIZE_SPEECH);
                mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                        mContext.getPackageName());
                mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        R.string.you_may_speak);
                String preferredLanguage;
                if (lang.equals("en")) {
                    preferredLanguage = "en-US";
                } else {
                    preferredLanguage = "fa-IR";
                }
                mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, preferredLanguage);
                mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                        preferredLanguage);
                if (mSpeechRecognizerIntent.resolveActivity(mContext.getPackageManager()) != null) {
                    mainActivity.startActivityForResult(mSpeechRecognizerIntent, requestCode);
                    setIsListening(true);
                    mainActivity.runOnUiThread(() ->
                            Toast.makeText(mContext, R.string.you_may_speak, Toast.LENGTH_SHORT).show());
                    return true;
                } else {
                    mainActivity.showInstallGoogleSearchBoxSttDialog();
                    return false;
                }
            } else {
                Log.e(getClass().getName(), mContext.getResources().getString(R.string.
                        ds_internet_not_enabled));
                mainActivity.runOnUiThread(() ->
                        Toast.makeText(mContext, R.string.ds_internet_not_enabled,
                                Toast.LENGTH_LONG).show());
                return false;
            }
        } catch (IllegalStateException | ActivityNotFoundException e) {
            Log.e(STT.class.getName(), e.getMessage(), e);
            mainActivity.runOnUiThread(() -> Toast.makeText(mContext, R.string.speech_to_text_error,
                    Toast.LENGTH_SHORT).show());
            return false;
        }
    }

    private boolean isNetworkAvailable(Context mContext) {
        // Initializing the connectivity Manager
        ConnectivityManager activeConnection = (ConnectivityManager) mContext.
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Getting the network information
        NetworkInfo networkInfo = activeConnection.getActiveNetworkInfo();

        return networkInfo != null && (networkInfo.getType() == ConnectivityManager.TYPE_WIFI ||
                networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
    }
}

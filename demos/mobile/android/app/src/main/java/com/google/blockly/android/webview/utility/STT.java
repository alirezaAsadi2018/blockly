package com.google.blockly.android.webview.utility;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import com.google.blockly.android.webview.R;
import com.google.blockly.android.webview.demo.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;


public class STT implements Codes {
    private static STT instance;
    private final AtomicBoolean isListening = new AtomicBoolean(false);
    private final AtomicBoolean isBusyAskingForInfo = new AtomicBoolean(false);
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private String currentSpeechLanguage;
    private List<String> supportedSpeechLanguages;
    private final String preferredLanguage = "fa-IR";
    private SpeechRecognizer speechRecognizer;

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

    public static STT getInstance() {
        if (instance == null) {
            return instance = new STT();
        } else {
            return instance;
        }
    }

    public synchronized void stt(final Context mContext, final MainActivity mainActivity, final int requestCode) {
        if (getIsBusyAskingForInfo().get() || getIsListening().get()) {
            return;
        }
        executorService.execute(() -> {
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
                    if (supportedSpeechLanguages.contains(preferredLanguage)) {
                        // Setting the speech language
                        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                                preferredLanguage);
                        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                                preferredLanguage);
                    } else {
                        mainActivity.runOnUiThread(() ->
                                Toast.makeText(mContext, R.string.speech_fa_lang_not_supported,
                                        Toast.LENGTH_LONG).show());
                        //TODO
                        //make proper action here
                    }

                    //another way is like this, but may cause error on some devices and I dono why!
//                       if (mSpeechRecognizerIntent.resolveActivity(mContext.getPackageManager()) != null) {
                    if (isIntentAvailable(mSpeechRecognizerIntent, mContext)) {
                        mainActivity.startActivityForResult(mSpeechRecognizerIntent, requestCode);
                        setIsListening(true);
                        mainActivity.runOnUiThread(() ->
                                Toast.makeText(mContext, R.string.you_may_speak, Toast.LENGTH_SHORT).show());
                    } else {
                        mainActivity.runOnUiThread(() ->
                                Toast.makeText(mContext, R.string.speech_not_supported,
                                        Toast.LENGTH_LONG).show());
                    }
                } else {
                    Log.e(getClass().getName(), mContext.getResources().getString(R.string.
                            ds_internet_not_enabled));
                    mainActivity.runOnUiThread(() ->
                            Toast.makeText(mContext, R.string.ds_internet_not_enabled,
                                    Toast.LENGTH_LONG).show());
                }
            } catch (IllegalStateException | ActivityNotFoundException e) {
                Log.e(STT.class.getName(), e.getMessage(), e);
            }
        });
    }

    private boolean isIntentAvailable(Intent intent, Context mContext) {
        PackageManager pm = mContext.getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
        return activities.size() > 0;
    }

    /**
     * Starts the language receiver
     */
    public void startLanguageReceiver(Context mContext) {

        Intent languageDetailsIntent = RecognizerIntent.getVoiceDetailsIntent(mContext);
        languageDetailsIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        LanguageReceiver languageReceiver = new LanguageReceiver();
        languageReceiver.setOnLanguageDetailsListener((defaultLanguage, otherLanguages) -> {
            currentSpeechLanguage = defaultLanguage;
            supportedSpeechLanguages = otherLanguages;
        });

        // Starting the broadcast receiver to get the language details
        mContext.sendOrderedBroadcast(languageDetailsIntent, null,
                languageReceiver, null, Activity.RESULT_OK, null, null);
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

/**
 * Droid Speech Language Receiver
 *
 * @author Vikram Ezhil
 */

class LanguageReceiver extends BroadcastReceiver {

    /**
     * Droid Speech Language Details Listener
     *
     * @author Vikram Ezhil
     */

    interface OnLanguageDetailsListener {
        /**
         * Sends an update with the device language details
         *
         * @param defaultLanguage The default language
         * @param otherLanguages  The other supported languages
         */
        void onLanguageDetailsInfo(String defaultLanguage, List<String> otherLanguages);
    }

    private OnLanguageDetailsListener onLanguageDetailsListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        List<String> supportedLanguages = new ArrayList<>();
        String defaultLanguagePreference = null;

        if (getResultCode() == Activity.RESULT_OK) {
            Bundle results = getResultExtras(true);
            if (results != null) {
                if (results.containsKey(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE)) {
                    defaultLanguagePreference = results.getString(RecognizerIntent.
                            EXTRA_LANGUAGE_PREFERENCE);
                }

                if (results.containsKey(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)) {
                    if (results.getStringArrayList(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES) != null) {
                        supportedLanguages = results.getStringArrayList(RecognizerIntent.
                                EXTRA_SUPPORTED_LANGUAGES);
                    }
                }
            }
        }

        if (onLanguageDetailsListener != null) {
            // Sending an update with the language details information
            onLanguageDetailsListener.onLanguageDetailsInfo(defaultLanguagePreference,
                    supportedLanguages);
        }
    }

    /**
     * Sets the language details listener
     *
     * @param onLanguageDetailsListener The language details listener
     */
    void setOnLanguageDetailsListener(OnLanguageDetailsListener onLanguageDetailsListener) {
        this.onLanguageDetailsListener = onLanguageDetailsListener;
    }
}

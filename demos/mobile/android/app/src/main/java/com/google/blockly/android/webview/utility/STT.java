package com.google.blockly.android.webview.utility;

import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.Toast;

import com.google.blockly.android.webview.R;
import com.google.blockly.android.webview.demo.MainActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class STT implements Codes {
    private static STT instance;
    private final AtomicBoolean isListening = new AtomicBoolean(false);
    private final AtomicBoolean isBusyAskingForInfo = new AtomicBoolean(false);
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

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
                Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fa-IR");
                mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        R.string.you_may_speak);
                mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                        mContext.getPackageName());
                mainActivity.runOnUiThread(() ->
                        Toast.makeText(mContext, R.string.you_may_speak, Toast.LENGTH_LONG).show());
                if (mSpeechRecognizerIntent.resolveActivity(mContext.getPackageManager()) != null) {
                    mainActivity.startActivityForResult(mSpeechRecognizerIntent, requestCode);
                    setIsListening(true);
                } else {
                    mainActivity.runOnUiThread(() ->
                            Toast.makeText(mContext, R.string.speech_not_supported,
                            Toast.LENGTH_LONG).show());
                }
            } catch (IllegalStateException e) {
                Log.e(STT.class.getName(), e.getMessage(), e);
            }
        });
    }
}

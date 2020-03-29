package com.google.blockly.android.webview.utility;

import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.Toast;

import com.google.blockly.android.webview.R;
import com.google.blockly.android.webview.demo.MainActivity;

public class STT implements Codes {
    private static STT instance;


    public static STT getInstance() {
        if (instance == null) {
            return instance = new STT();
        } else {
            return instance;
        }
    }

    public void stt(Context mContext, MainActivity mainActivity, int requestCode) {
        try {
            Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fa-IR");
            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, R.string.you_may_speak);
            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                    mContext.getPackageName());
            Toast.makeText(mContext, R.string.you_may_speak, Toast.LENGTH_LONG).show();
            if (mSpeechRecognizerIntent.resolveActivity(mContext.getPackageManager()) != null)
                mainActivity.startActivityForResult(mSpeechRecognizerIntent, requestCode);
            else
                Toast.makeText(mContext, R.string.speech_not_supported, Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            Log.e(STT.class.getName(), e.getMessage(), e);
        }
    }
}

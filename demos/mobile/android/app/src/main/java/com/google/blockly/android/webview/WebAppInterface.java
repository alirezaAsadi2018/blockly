package com.google.blockly.android.webview;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.example.blocklywebview.R;
import com.google.blockly.android.webview.demo.MainActivity;

public class WebAppInterface {
    private Context mContext;
    private MainActivity mainActivity;


    WebAppInterface(Context c, MainActivity mainActivity) {
        mContext = c;
        this.mainActivity = mainActivity;
    }

    /**
     * Show a toast from the web page
     */
    @JavascriptInterface
    public void stt() {
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
                mainActivity.startActivityForResult(mSpeechRecognizerIntent, 10);
            else
                Toast.makeText(mContext, R.string.speech_not_supported, Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}

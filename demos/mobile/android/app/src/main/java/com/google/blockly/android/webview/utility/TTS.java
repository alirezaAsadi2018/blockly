package com.google.blockly.android.webview.utility;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import com.google.blockly.android.webview.demo.MainActivity;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class TTS implements Codes {
    private TextToSpeech textToSpeech;
    private final AtomicBoolean isLocaleInitialized = new AtomicBoolean(false);
    private final MainActivity mMainActivity;
    private String lang;

    public AtomicBoolean getIsLocaleInitialized() {
        return isLocaleInitialized;
    }

    public TTS(final Context mContext, final MainActivity mainActivity, String engine, String lang) {
        mMainActivity = mainActivity;
        textToSpeech = new TextToSpeech(mContext, status -> {
            if (status == TextToSpeech.SUCCESS) {
                if(textToSpeech == null) {
                    Log.i(this.getClass().getName(), "tts instance is null!!");
                    return;
                }
                setLanguage(lang);
                Log.i("TTS", "Initialization success.");
            } else
                Log.e("TTS", "Initialization Failed!");
        }, engine);

        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                Log.d("TTS", "started " + utteranceId);
            }

            @Override
            public void onDone(String utteranceId) {
                Log.d("TTS", "done " + utteranceId);
                mainActivity.runOnUiThread(()->{
                    switch (Integer.parseInt(utteranceId)) {
                        case ASK_NAME_UTTERANCE_ID:
                            STT.getInstance().setIsBusyAskingForInfo(false);
                            STT.getInstance().stt(mContext, mainActivity, STT_GET_NAME, lang);
                            break;
                        case ASK_CITY_UTTERANCE_ID:
                            STT.getInstance().setIsBusyAskingForInfo(false);
                            STT.getInstance().stt(mContext, mainActivity, STT_GET_CITY_NAME, lang);
                            break;
                        case IDLE_UTTERANCE_ID:
                        default:
                            break;
                    }
                });
            }

            @Override
            public void onError(String utteranceId) {
                Log.e("TTS", "error on " + utteranceId);
            }
        });
    }

    public String getLang() {
        return lang;
    }

    public void setLanguage(String lang){
        this.lang = lang;
        int ttsLang = textToSpeech.setLanguage(new Locale(lang));
        if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.e("TTS", "Language is not supported or loaded!");
            isLocaleInitialized.set(false);
        } else {
            Log.i("TTS", "Language Supported.");
            isLocaleInitialized.set(true);
        }
        synchronized (isLocaleInitialized) {
            isLocaleInitialized.notifyAll();
        }
    }

    private void showRestartDialog(){
        mMainActivity.showRestartDialog(
        (dialog, which)-> {mMainActivity.setMTtsInstance(null);dialog.cancel();},
        (dialog)->mMainActivity.setMTtsInstance(null),
        ()->{stop();mMainActivity.setMTtsInstance(null);});
    }

    public void tts(final String text, final int utteranceID) {
        mMainActivity.runOnUiThread(()->{
            int speechStatus = textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null,
                    String.valueOf(utteranceID));
            if (speechStatus == TextToSpeech.ERROR) {
                Log.e("TTS", "Error in tts function!");
            }
        });
    }

    public void stop() {
        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }
    }
}

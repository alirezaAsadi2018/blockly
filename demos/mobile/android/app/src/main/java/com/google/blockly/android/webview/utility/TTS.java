package com.google.blockly.android.webview.utility;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import com.google.blockly.android.webview.demo.MainActivity;

import java.util.HashMap;
import java.util.Locale;

public class TTS implements Codes {
    private static TTS instance;
    private TextToSpeech textToSpeech;


    public TTS(final Context mContext, final MainActivity mainActivity){
        textToSpeech = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = textToSpeech.setLanguage(new Locale("fa"));
                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                } else
                    Log.e("TTS", "Initilization Failed!");
            }
        }, "com.googlecode.eyesfree.espeak");

        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                Log.d("TTS", "started " + utteranceId);
            }



            @Override
            public void onDone(String utteranceId) {
                Log.d("TTS", "done " + utteranceId);
                switch (Integer.parseInt(utteranceId)) {
                    case ASK_NAME_UTTERANCE_ID:
                        mainActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                STT.getInstance().stt(mContext, mainActivity, STT_GET_NAME);
                            }
                        });
                        break;
                    case ASK_CITY_UTTERANCE_ID:
                        mainActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                STT.getInstance().stt(mContext, mainActivity, STT_GET_CITY_NAME);
                            }
                        });
                        break;
                    case IDLE_UTTERANCE_ID:
                    default:
                        break;
                }
            }

            @Override
            public void onError(String utteranceId) {
                Log.d("TTS", "error on " + utteranceId);
            }
        });
    }


//    public static TTS getInstance() {
//        if (instance == null) {
//            return instance = new TTS();
//        } else {
//            return instance;
//        }
//    }

    public void tts(String text, int utteranceID){
        int speechStatus;
        HashMap<String, String> params = new HashMap<>();
        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, String.valueOf(utteranceID));
        speechStatus = textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, params);
        if (speechStatus == TextToSpeech.ERROR) {
            Log.e("TTS", "Error in converting Text to Speech!");
        }
    }

    public void tts2(final Context mContext, final MainActivity mainActivity, final String text,
                    final int utteranceID) {
        textToSpeech = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = textToSpeech.setLanguage(new Locale("fa"));
                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                    int speechStatus;
                    HashMap<String, String> params = new HashMap<>();
                    params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, String.valueOf(utteranceID));
                    speechStatus = textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, params);
                    if (speechStatus == TextToSpeech.ERROR) {
                        Log.e("TTS", "Error in converting Text to Speech!");
                    }
                } else
                    Log.e("TTS", "Initilization Failed!");
            }
        }, "com.googlecode.eyesfree.espeak");

        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                Log.d("TTS", "started " + utteranceId);
            }

            @Override
            public void onDone(String utteranceId) {
                Log.d("TTS", "done " + utteranceId);
                switch (Integer.parseInt(utteranceId)) {
                    case ASK_NAME_UTTERANCE_ID:
                        mainActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                STT.getInstance().stt(mContext, mainActivity, STT_GET_NAME);
                            }
                        });
                        break;
                    case ASK_CITY_UTTERANCE_ID:
                        mainActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                STT.getInstance().stt(mContext, mainActivity, STT_GET_CITY_NAME);
                            }
                        });
                        break;
                    case IDLE_UTTERANCE_ID:
                    default:
                        break;
                }
            }

            @Override
            public void onError(String utteranceId) {
                Log.d("TTS", "error on " + utteranceId);
            }
        });

        Log.i("TTS", textToSpeech.getEngines().toString());
        // [EngineInfo{name=com.google.android.tts}, EngineInfo{name=com.redzoc.ramees.tts.espeak}]
    }
}

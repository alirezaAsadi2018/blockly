package com.google.blockly.android.webview.utility;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import com.google.blockly.android.webview.demo.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class TTS implements Codes {
    private static final String tempDirFileName = "/roobin_voices/";
    private static final String ttsOutputFilename = "tts.wav";
    private final AtomicBoolean isLocaleInitialized = new AtomicBoolean(false);
    private final MainActivity mMainActivity;
    private final File ttsOutputFile;
    private TextToSpeech textToSpeech;
    private String lang;

    public TTS(final Context mContext, final MainActivity mainActivity, String engine, String lang) {
        mMainActivity = mainActivity;
        // create a folder and a file name to save the temp tts output in
//        String exStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
//        File ttsOutputDirFile = new File(exStoragePath + tempDirFileName);
//        boolean isDirectoryCreated = ttsOutputDirFile.mkdirs();
//        if(isDirectoryCreated){
//            Log.i(this.getClass().getName(), "tts output directory file was created successfully!!");
//        }else{
//            Log.i(this.getClass().getName(), "tts output directory file was already available!!");
//        }
        Log.d(this.getClass().getName(), mContext.getExternalFilesDir(null).getAbsolutePath());
        String destFileLocation = mContext.getExternalFilesDir(null).getAbsolutePath()
                + File.separator + ttsOutputFilename;
        ttsOutputFile = new File(destFileLocation);

        textToSpeech = new TextToSpeech(mContext, status -> {
            if (status == TextToSpeech.SUCCESS) {
                if (textToSpeech == null) {
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
                mainActivity.runOnUiThread(() -> {
                    switch (Integer.parseInt(utteranceId)) {
                        case ASK_NAME_UTTERANCE_ID:
                            STT.getInstance().setIsBusyAskingForInfo(false);
                            STT.getInstance().stt(mContext, mainActivity, STT_GET_NAME, lang);
                            break;
                        case ASK_CITY_UTTERANCE_ID:
                            STT.getInstance().setIsBusyAskingForInfo(false);
                            STT.getInstance().stt(mContext, mainActivity, STT_GET_CITY_NAME, lang);
                            break;
                        case SYNTHESIZE_To_FILE_UTTERANCE_ID:
                            Log.i(this.getClass().getName(), "tts temp file created successfully!");
                            try {
                                playTtsOutputFile();
                                deleteTtsOutputFile();
                            } catch (FileNotFoundException e) {
                                Log.e(this.getClass().getName(), Objects.requireNonNull(e.getMessage()));
                            } catch (IOException e) {
                                Log.e(this.getClass().getName(), "can't play media player!");
                            }
                        case IDLE_UTTERANCE_ID:
                        default:
                            break;
                    }
                });
            }

            @Override
            public void onError(String utteranceId) {
                Log.e("TTS", "error on " + utteranceId);
                if (Integer.parseInt(utteranceId) == SYNTHESIZE_To_FILE_UTTERANCE_ID) {
                    try {
                        deleteTtsOutputFile();
                    } catch (FileNotFoundException e) {
                        Log.e(this.getClass().getName(), Objects.requireNonNull(e.getMessage()));
                    }
                }
            }

            @Override
            public void onStop(String utteranceId, boolean interrupted) {
                super.onStop(utteranceId, interrupted);
                if (Integer.parseInt(utteranceId) == SYNTHESIZE_To_FILE_UTTERANCE_ID) {
                    try {
                        deleteTtsOutputFile();
                    } catch (FileNotFoundException e) {
                        Log.e(this.getClass().getName(), Objects.requireNonNull(e.getMessage()));
                    }
                }
            }
        });
    }

    private void playTtsOutputFile() throws IOException {
        File ttsOutputFile = getTtsOutputFile();
        MediaPlayer player = new MediaPlayer();
        player.setDataSource(ttsOutputFile.getPath());
        player.prepare();
        player.start();
        player.setOnCompletionListener(MediaPlayer::release);
        player.setLooping(false);
    }

    private void deleteTtsOutputFile() throws FileNotFoundException {
        File ttsOutputFile = getTtsOutputFile();
        boolean deleted = ttsOutputFile.delete();
    }

    private File getTtsOutputFile() throws FileNotFoundException {
        if (ttsOutputFile != null) {
            return ttsOutputFile;
        }
        throw new FileNotFoundException("tts temp file was not found!");
    }

    public AtomicBoolean getIsLocaleInitialized() {
        return isLocaleInitialized;
    }

    public String getLang() {
        return lang;
    }

    public void setLanguage(String lang) {
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

    private void showRestartDialog() {
        mMainActivity.showRestartDialog(
                (dialog, which) -> {
                    mMainActivity.setMTtsInstance(null);
                    dialog.cancel();
                },
                (dialog) -> mMainActivity.setMTtsInstance(null),
                () -> {
                    stop();
                    mMainActivity.setMTtsInstance(null);
                });
    }

    public void tts(final String text, final int utteranceID) {
        int speechStatus = textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null,
                String.valueOf(utteranceID));
        if (speechStatus == TextToSpeech.ERROR) {
            Log.e("TTS", "Error in tts function!");
        }
    }

    public void say(String text) {
        int speechStatus = textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null,
                String.valueOf(IDLE_UTTERANCE_ID));
        if (speechStatus == TextToSpeech.ERROR) {
            Log.e("TTS", "Error in tts function!");
        }
    }

    public void sayFromFile(String text) {
        // first write to file
        // then play that sound file when writing is done!
        writeTtsOutputToFile(text);
    }

    public void writeTtsOutputToFile(String text) {
        Bundle params = new Bundle();
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, text);
        try {
            textToSpeech.synthesizeToFile(text, params, getTtsOutputFile(),
                    String.valueOf(SYNTHESIZE_To_FILE_UTTERANCE_ID));
        } catch (FileNotFoundException e) {
            Log.e(this.getClass().getName(), Objects.requireNonNull(e.getMessage()));
        }
    }

    public void stop() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }
}

package com.google.blockly.android.webview.utility;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import com.google.blockly.android.webview.demo.MainActivity;
import com.semantive.waveformandroid.waveform.soundfile.WavFile;
import com.semantive.waveformandroid.waveform.soundfile.WavFileException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.SecureRandom;
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
    private Context mContext;

    public TTS(final Context mContext, final MainActivity mainActivity, String engine, String lang) {
        mMainActivity = mainActivity;
        this.mContext = mContext;
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
                                setStreamVolumeToMaximum();
                                playTtsOutputFile();
                            } catch (FileNotFoundException e) {
                                Log.e(this.getClass().getName(), "tts output file not found!! " +
                                        Objects.requireNonNull(e.getMessage()));
                            } catch (IOException e) {
                                Log.e(this.getClass().getName(), "can't play media player!");
                            } catch (WavFileException e) {
                                Log.e(this.getClass().getName(), "There is a problem with temp wav file created by TTS: "
                                        + e.getMessage());
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

    private long getCurrentTimeInMillis() {
        return System.currentTimeMillis();
    }

    private String mouthing(int ph) throws IOException {
        String msg = "p0" + ph + "\n";
        return msg;
//        mMainActivity.getmBluetoothControllerInstance().send(msg);
    }

    private void lipSync() {
        // duration = eyed3.load(name).info.time_secs
        try {
            File ttsOutputFile = null;
            ttsOutputFile = getTtsOutputFile();
            WavFile wavFile = WavFile.openWavFile(ttsOutputFile);
            double duration = (double) wavFile.getNumFrames() / (double) wavFile.getSampleRate();
            //long currentTimeMillis ()-Returns the current time in milliseconds.
            SecureRandom secureRandom = new SecureRandom();
            StringBuilder stringBuilder = new StringBuilder();
            double stop = getCurrentTimeInMillis() + duration * 0.91;
            while (getCurrentTimeInMillis() < stop) {
                // create a random number ranging from [1-3] both inclusive
                int ph = secureRandom.nextInt(3) + 1;
                stringBuilder.append(mouthing(ph));
            }
            mMainActivity.getmBluetoothControllerInstance().send(stringBuilder.toString());
            Thread.sleep((long) 0.5);
            stringBuilder = new StringBuilder();
            for (int i = 0; i < 10; ++i) {
                stringBuilder.append(mouthing(1));
                stringBuilder.append(mouthing(1));
                stringBuilder.append(mouthing(1));
//                mouthing(1);
//                mouthing(1);
//                mouthing(1);
            }
            mMainActivity.getmBluetoothControllerInstance().send(stringBuilder.toString());
//            stringBuilder = new StringBuilder();
//            stringBuilder.append(mouthing(1));
//            stringBuilder.append(mouthing(1));
//            mouthing(1);
//            mouthing(1);
//            mMainActivity.getmBluetoothControllerInstance().send(stringBuilder.toString());
        } catch (FileNotFoundException e) {
            Log.e(this.getClass().getName(), "lipsync sleeping thread was interrupted!");
        } catch (IOException | WavFileException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setStreamVolumeToMaximum() {
        AudioManager am = (AudioManager) mMainActivity.getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
    }

    private void playTtsOutputFile() throws IOException, WavFileException {
        File ttsOutputFile = getTtsOutputFile();
        MediaPlayer player = new MediaPlayer();
        player.setDataSource(ttsOutputFile.getPath());
        player.prepare();
        player.setOnPreparedListener((mediaPlayer) -> {
            lipSync();
        });
        player.setOnCompletionListener((mediaPlayer) -> {
            Log.i(TTS.class.getName(), "finished playing TTS wav file.");
            try {
                deleteTtsOutputFile();
            } catch (FileNotFoundException e) {
                Log.e(TTS.class.getName(), "can not delete temp output file created for TTS!");
            }
            mediaPlayer.release();
        });
        player.setLooping(false);
        player.start();
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

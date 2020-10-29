package com.google.blockly.android.webview.utility;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.webkit.WebView;

import com.google.blockly.android.webview.R;
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
    private final MainActivity mainActivity;
    private final File ttsOutputFile;
    private TextToSpeech textToSpeech;
    private String lang;
    private Context mContext;
    private float speechRate = 1;
    private float speakingPitch = 1;

    public TTS(final Context mContext, final MainActivity mainActivity, String engine, String lang) {
        this.mainActivity = mainActivity;
        this.mContext = mContext;
        String destFileLocation = Objects.requireNonNull(mContext.getExternalFilesDir(null)).getAbsolutePath()
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

        textToSpeech.setSpeechRate(speechRate);
        textToSpeech.setPitch(speakingPitch);

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
                                lipSync();
                            } catch (FileNotFoundException e) {
                                Log.e(this.getClass().getName(), "tts output file not found!! " +
                                        Objects.requireNonNull(e.getMessage()));
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

    private long getCurrentTimeInMillis() {
        return System.currentTimeMillis();
    }

    private void mouthing(int ph) throws IOException {
        String msg = "p0" + ph + "\n";
        mainActivity.getmBluetoothControllerInstance().send(msg);
    }

    private void lipSync() {
        try {
            File ttsOutputFile = getTtsOutputFile();
            WavFile wavFile = WavFile.openWavFile(ttsOutputFile);
            double durationMillis = ((double) wavFile.getNumFrames() / (double) wavFile.getSampleRate()) * 1000;
            SecureRandom secureRandom = new SecureRandom();
            double stop = getCurrentTimeInMillis() + durationMillis * 0.70;
            while (getCurrentTimeInMillis() < stop) {
                // create a random number ranging from [1-3] both inclusive
                int ph = secureRandom.nextInt(3) + 1;
                mouthing(ph);
            }
            for (int i = 0; i < 10; ++i) {
                mouthing(1);
                mouthing(1);
                mouthing(1);
            }
            Thread.sleep((long) 100);
            mouthing(1);
            mouthing(1);
        } catch (InterruptedException e) {
            Log.e(this.getClass().getName(), "lip sync sleeping thread was interrupted!");
        } catch (IOException | WavFileException e) {
            e.printStackTrace();
        }
    }

    private void setStreamVolumeToMaximum() {
        AudioManager am = (AudioManager) mainActivity.getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
    }

    private void playTtsOutputFile() throws IOException{
        File ttsOutputFile = getTtsOutputFile();
        MediaPlayer player = new MediaPlayer();
        player.setDataSource(ttsOutputFile.getPath());
        player.prepare();
        player.setOnCompletionListener((mediaPlayer)->{
            Log.d(TTS.class.getName(), "finished playing TTS wav file.");
            try {
                deleteTtsOutputFile();
            } catch (FileNotFoundException e) {
                Log.e(TTS.class.getName(), "can not delete temp output file created for TTS!");
            }
            mediaPlayer.release();
            WebView mWebView = mainActivity.findViewById(R.id.blockly_webview);
            mWebView.post(() -> mWebView.loadUrl("javascript:ttsFinished();"));
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
        mainActivity.showRestartDialog(
                (dialog, which) -> {
                    mainActivity.setMTtsInstance(null);
                    dialog.cancel();
                },
                (dialog) -> mainActivity.setMTtsInstance(null),
                () -> {
                    stop();
                    mainActivity.setMTtsInstance(null);
                });
    }

    public void tts(final String text, final int utteranceID) {
        int speechStatus = textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null,
                String.valueOf(utteranceID));
        if (speechStatus == TextToSpeech.ERROR) {
            Log.e("TTS", "Error in tts function!");
        }
    }

    public void setSpeakingPitch(float pitch) {
        this.speakingPitch = pitch;
        textToSpeech.setPitch(speakingPitch);
    }

    public void setSpeakingSpeed(float speed) {
        this.speechRate = speed;
        textToSpeech.setSpeechRate(speechRate);
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

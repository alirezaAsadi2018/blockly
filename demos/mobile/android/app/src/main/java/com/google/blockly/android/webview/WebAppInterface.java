package com.google.blockly.android.webview;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class WebAppInterface {
    Context mContext;

    private static class MyHandler extends Handler {
    }

    private MediaRecorder myAudioRecorder;
    private String outputFile;

    /**
     * Instantiate the interface and set the context
     *
     * @param c
     */
    WebAppInterface(Context c) {
        mContext = c;
    }

    /**
     * Show a toast from the web page
     */
    @JavascriptInterface
    public void showToast(String toast) {
//        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        recorder();
    }

    public void recorder() {
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);
        record();
    }

    public void record() {
        try {
            myAudioRecorder.prepare();
            myAudioRecorder.start();
            Toast.makeText(mContext, "Recording started", Toast.LENGTH_LONG).show();
            stopRecording();
        } catch (IllegalStateException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void stopRecording() {
        final MyHandler mHandler = new MyHandler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                myAudioRecorder.stop();
                myAudioRecorder.release();
                myAudioRecorder = null;
                Toast.makeText(mContext, "Audio Recorded successfully", Toast.LENGTH_LONG).show();
                play();
            }
        }, 4000);
    }

    public void play() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            File f = new File(outputFile);
            if (f.exists()) {
                mediaPlayer.setDataSource(outputFile);
                mediaPlayer.prepare();
                mediaPlayer.start();
                Toast.makeText(mContext, "Playing Audio", Toast.LENGTH_LONG).show();
                //post to python code


                HttpClient httpClient = HttpClientBuilder.create().build();
                try (InputStream inputStream = new FileInputStream(outputFile)) {
                    long fileSize = new File(outputFile).length();
                    byte[] allBytes = new byte[(int) fileSize];
                    int code = inputStream.read(allBytes);
                    Base64 codec = new Base64();
                    byte[] encoded = codec.encode(allBytes);
                    String base64EncodedAudio = new String(encoded);

                    HttpPost request = new HttpPost("http://alirezaasadi.pythonanywhere.com/messages");
                    StringEntity params = new StringEntity("details={\"message\":" +
                            "\"" + base64EncodedAudio + "\"} ");
                    request.addHeader("content-type", "application/x-www-form-urlencoded");
                    request.setEntity(params);
                    HttpResponse response = httpClient.execute(request);
                    if (response.getStatusLine().getStatusCode() == 201) {
                        Toast.makeText(mContext, "status is good!", Toast.LENGTH_SHORT).show();
                    }
                    //handle response here...

                } catch (Exception ex) {

                    //handle exception here

                } finally {
                    //Deprecated
                    //httpClient.getConnectionManager().shutdown();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}

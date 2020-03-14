package com.google.blockly.android.webview;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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
    public void stt() {
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
                Toast.makeText(mContext, "Playing Audio", Toast.LENGTH_LONG).show();
                mediaPlayer.setDataSource(outputFile);
                mediaPlayer.prepare();
                mediaPlayer.start();


                //use python code
                String server_url = "http://alirezaasadi.pythonanywhere.com";
                String id = postTOServer(server_url);
                String text = getFromServer(server_url, id);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String getFromServer(String server_url, String id) throws IOException {
        //make GET
        URL url = new URL(server_url + "/messages/" + id);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        if (responseCode == 200) { // success
            StringBuffer response = new StringBuffer();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }
            String resultText = "";
            for (String s : response.toString().split(",")) {
                if (s.contains("message")) {
                    resultText = s.split(":")[1].replaceAll("\"", "");
                }
            }
            Toast.makeText(mContext, resultText, Toast.LENGTH_LONG).show();
            return resultText;
        } else {
            System.out.println("GET request not worked");
            return null;
        }
    }

    private String postTOServer(String server_url) throws IOException {
        URL url = new URL(server_url + "/messages");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        try (InputStream inputStream = new FileInputStream(outputFile)) {
            long fileSize = new File(outputFile).length();
            byte[] allBytes = new byte[(int) fileSize];
            int code = inputStream.read(allBytes);

            byte[] encodedBytes = Base64.encodeBase64(allBytes);
            String base64EncodedAudio = new String(encodedBytes);

            String jsonInputString = "{\"message\":" +
                    "\"" + base64EncodedAudio + "\"} ";

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            StringBuffer response = new StringBuffer();
            String id = "";
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                for (String s : response.toString().split(",")) {
                    if (s.contains("messageId")) {
                        id = s.split(":")[1].replaceAll("\"", "");
                    }
                }
            }
            if (con.getResponseCode() == 201) {
                Toast.makeText(mContext, "status is good!", Toast.LENGTH_SHORT).show();
            }
            con.disconnect();
            return id;
        }
    }
}

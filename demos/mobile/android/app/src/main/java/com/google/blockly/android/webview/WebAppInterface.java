package com.google.blockly.android.webview;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.blockly.android.webview.demo.MainActivity;
import com.google.blockly.android.webview.utility.Codes;
import com.google.blockly.android.webview.utility.STT;

import java.io.IOException;
import java.util.logging.Handler;


public class WebAppInterface implements Codes {
    private final Context mContext;
    private final MainActivity mainActivity;

    WebAppInterface(Context mContext, MainActivity mainActivity) {
        this.mContext = mContext;
        this.mainActivity = mainActivity;
    }

    @JavascriptInterface
    public boolean on() {
        return mainActivity.getmBluetoothControllerInstance().on();
    }

    @JavascriptInterface
    public void off() {
        mainActivity.getmBluetoothControllerInstance().off();
        mainActivity.runOnUiThread(() -> Toast.makeText(mContext, "Turned off", Toast.LENGTH_LONG)
                .show());
    }

    @JavascriptInterface
    public void visible() {
        mainActivity.getmBluetoothControllerInstance().visible();
    }

    @JavascriptInterface
    public String list() throws InterruptedException {
        return mainActivity.getmBluetoothControllerInstance().list();
    }

    @JavascriptInterface
    public void connectBluetooth(String name) {
        try {
            mainActivity.getmBluetoothControllerInstance().connectBluetooth(name);
        } catch (IOException e) {
            Log.e(this.getClass().getName(), e + ", can not pair with the bluetooth device");
        }
    }

    @JavascriptInterface
    public void send(String text) {
        try {
            mainActivity.getmBluetoothControllerInstance().send(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void scan() {
        mainActivity.getmBluetoothControllerInstance().scan();
    }

    @JavascriptInterface
    public void restartBluetooth(){
        new Thread(()->{
            mainActivity.getmBluetoothControllerInstance().restart();
        }).start();
    }

    @JavascriptInterface
    public void tts(String text, String lang) {
        try {
            mainActivity.getMTtsInstanceForLanguage(lang).sayFromFile(text);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "tts engine installation error: " + e.getMessage());
        }
    }

    @JavascriptInterface
    public void setTtsSpeakingPitch(float pitch) {
        try {
            mainActivity.getMTtsInstance().setSpeakingPitch(pitch);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "tts engine installation error: " + e.getMessage());
        }
    }

    @JavascriptInterface
    public void changeTtsSpeakingPitch(float pitchChange){
        try {
            mainActivity.getMTtsInstance().changeSpeakingPitch(pitchChange);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "tts engine installation error: " + e.getMessage());
        }
    }

    @JavascriptInterface
    public void setTtsSpeakingSpeed(float speed) {
        try {
            mainActivity.getMTtsInstance().setSpeakingSpeed(speed);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "tts engine installation error: " + e.getMessage());
        }
    }

    @JavascriptInterface
    public void stopTts() {
        try {
            if (mainActivity.getMTtsInstance() != null)
                mainActivity.getMTtsInstance().stop();
//        isTtsLocaleInstallationDone.set(false);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "tts engine installation error: " + e.getMessage());
        }
    }

    @JavascriptInterface
    public void loadWebview(String htmlName) {
        String webviewFileBase = "file:///android_asset/blockly/";
        WebView mWebView = mainActivity.findViewById(R.id.blockly_webview);
        mWebView.post(() -> mWebView.loadUrl(webviewFileBase + htmlName));
    }


    @JavascriptInterface
    public String stt(String lang) throws InterruptedException {
        boolean res = STT.getInstance().stt(mContext, mainActivity, STT_DO_COMMAND_CODE, lang);
        if (res) {
            synchronized (mainActivity) {
                mainActivity.wait();
            }
            return mainActivity.sttResult;
        } else {
            return "";
        }
    }
}

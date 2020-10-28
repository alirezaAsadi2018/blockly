package com.google.blockly.android.webview;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.ParcelUuid;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.blockly.android.webview.demo.MainActivity;
import com.google.blockly.android.webview.utility.Codes;
import com.google.blockly.android.webview.utility.STT;
import com.google.blockly.android.webview.utility.TTS;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class WebAppInterface implements Codes {
    private final Context mContext;
    private final MainActivity mainActivity;
    private final AtomicBoolean isTtsLocaleInstallationDone = new AtomicBoolean(false);


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
        try{
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
    public void tts(String text, String lang) {
        if (checkIfEspeakIsInstalled(lang)) {
            mainActivity.getMTtsInstance().sayFromFile(text);
        }
    }

    @JavascriptInterface
    public void stopTts() {
        if (mainActivity.getMTtsInstance() != null)
            mainActivity.getMTtsInstance().stop();
//        isTtsLocaleInstallationDone.set(false);
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

    private synchronized boolean initTts(PackageManager pm, String lang) {
        if (mainActivity.getMTtsInstance() == null) {
            boolean isPackageInstalled = false;
            List<String> enginesList = new ArrayList<>();
            enginesList.add("com.redzoc.ramees.tts.espeak");
            enginesList.add("com.reecedunn.espeak");
            enginesList.add("com.googlecode.eyesfree.espeak");
            for (String engineName : enginesList) {
                if (isPackageInstalled(engineName, pm)) {
                    mainActivity.setMTtsInstance(new TTS(mContext, mainActivity, engineName, lang));
                    isPackageInstalled = true;
                    break;
                }
            }
            return isPackageInstalled;
        }
        return true;
    }

    private boolean checkIfEspeakIsInstalled(String lang) {
        PackageManager pm = mContext.getPackageManager();
        if (mainActivity.getMTtsInstance() == null) {
            boolean isPackageInstalled = initTts(pm, lang);
            if (!isPackageInstalled) {
                mainActivity.showInstallEspeakDialog();
                return false;
            }
        }
        if (mainActivity.getMTtsInstance() != null
                && mainActivity.getMTtsInstance().getIsLocaleInitialized().get()
                && mainActivity.getMTtsInstance().getLang().equals(lang)) {
            return true;
        }
        if (mainActivity.getMTtsInstance() != null) {
            mainActivity.getMTtsInstance().getIsLocaleInitialized().set(false);
            isTtsLocaleInstallationDone.set(false);
            synchronized (mainActivity.getMTtsInstance().getIsLocaleInitialized()) {
                try {
                    mainActivity.getMTtsInstance().getIsLocaleInitialized().wait(1000);
                    if (!mainActivity.getMTtsInstance().getIsLocaleInitialized().get()) {
                        mainActivity.getMTtsInstance().setLanguage(lang);
                        mainActivity.getMTtsInstance().getIsLocaleInitialized().wait(1000);
                    }
                    if (mainActivity.getMTtsInstance().getIsLocaleInitialized().get()) {
                        return true;
                    } else {
                        // run only once with one thread
                        if (!isTtsLocaleInstallationDone.get()) {
                            mainActivity.setIsSttButtonActive(false);
                            Intent installIntent = new Intent();
                            installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                            mainActivity.startActivityForResult(installIntent, INSTALL_TTS_DATA_CODE);
                            isTtsLocaleInstallationDone.set(true);
                        }
                        return false;
                    }
                } catch (InterruptedException e) {
                    Log.e(this.getClass().getName(), "thread interrupted waiting " +
                            "for tts init", e);
                    return false;
                }
            }
        }
        return false;
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}

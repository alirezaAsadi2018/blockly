package com.google.blockly.android.webview;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.google.blockly.android.webview.demo.MainActivity;
import com.google.blockly.android.webview.utility.Codes;
import com.google.blockly.android.webview.utility.STT;
import com.google.blockly.android.webview.utility.TTS;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class WebAppInterface implements Codes {
    private final Context mContext;
    private final MainActivity mainActivity;
    private final AtomicBoolean isTtsFaLocaleInstallationDone = new AtomicBoolean(false);


    WebAppInterface(Context mContext, MainActivity mainActivity) {
        this.mContext = mContext;
        this.mainActivity = mainActivity;
    }

    @JavascriptInterface
    public void tts(String text) throws InterruptedException {
        if (checkIfEspeakIsInstalled()) {
            mainActivity.getMTtsInstance().tts(text, IDLE_UTTERANCE_ID);
        }
    }

    @JavascriptInterface
    public void loadWebview(String htmlName){
        String webviewFileBase = "file:///android_asset/blockly/";
        WebView mWebView = mainActivity.findViewById(R.id.blockly_webview);
        mWebView.post(() -> {
            mWebView.loadUrl(webviewFileBase + htmlName);
        });
    }


    @JavascriptInterface
    public String stt() throws InterruptedException {
        STT.getInstance().stt(mContext, mainActivity, STT_DO_COMMAND_CODE);
        synchronized (mainActivity) {
            mainActivity.wait();
        }
        return mainActivity.sttResult;
    }

    private synchronized boolean initTts(PackageManager pm) {
        if (mainActivity.getMTtsInstance() == null) {
            boolean isPackageInstalled = false;
            List<String> enginesList = new ArrayList<>();
            enginesList.add("com.redzoc.ramees.tts.espeak");
            enginesList.add("com.reecedunn.espeak");
            enginesList.add("com.googlecode.eyesfree.espeak");
            for (String engineName : enginesList) {
                if (isPackageInstalled(engineName, pm)) {
                    mainActivity.setMTtsInstance(new TTS(mContext, mainActivity, engineName));
                    isPackageInstalled = true;
                    break;
                }
            }
            return isPackageInstalled;
        }
        return true;
    }

    private boolean checkIfEspeakIsInstalled() {
        PackageManager pm = mContext.getPackageManager();
        if (mainActivity.getMTtsInstance() == null) {
            boolean isPackageInstalled = initTts(pm);
            if (!isPackageInstalled) {
                mainActivity.showInstallEspeakDialog();
                return false;
            }
        }
        if (mainActivity.getMTtsInstance() != null &&
                mainActivity.getMTtsInstance().getIsFaLocaleInitialized().get()) {
            return true;
        }
        if (mainActivity.getMTtsInstance() != null &&
                !mainActivity.getMTtsInstance().getIsFaLocaleInitialized().get()) {
            synchronized (mainActivity.getMTtsInstance().getIsFaLocaleInitialized()) {
                try {
                    mainActivity.getMTtsInstance().getIsFaLocaleInitialized().wait(1000);
                    if (!mainActivity.getMTtsInstance().getIsFaLocaleInitialized().get()) {
                        mainActivity.getMTtsInstance().setLanguage();
                        mainActivity.getMTtsInstance().getIsFaLocaleInitialized().wait(1000);
                    }
                    if (mainActivity.getMTtsInstance().getIsFaLocaleInitialized().get()) {
                        return true;
                    } else {
                        // run only once with one thread
                        if (!isTtsFaLocaleInstallationDone.get()) {
                            mainActivity.setIsSttButtonActive(false);
                            Intent installIntent = new Intent();
                            installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                            mainActivity.startActivityForResult(installIntent, INSTALL_TTS_DATA_CODE);
                            isTtsFaLocaleInstallationDone.set(true);
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

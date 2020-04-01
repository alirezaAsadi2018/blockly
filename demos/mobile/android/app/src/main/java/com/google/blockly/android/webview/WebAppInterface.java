package com.google.blockly.android.webview;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.google.blockly.android.webview.demo.MainActivity;
import com.google.blockly.android.webview.utility.Codes;
import com.google.blockly.android.webview.utility.STT;
import com.google.blockly.android.webview.utility.TTS;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class WebAppInterface implements Codes {
    private Context mContext;
    private MainActivity mainActivity;
    private AtomicInteger activeThreads = new AtomicInteger(0);
    private int threadThreshold = 5;
    private ExecutorService executorService = Executors.newFixedThreadPool(threadThreshold);
    private final AtomicBoolean isTtsFaLocaleInstallationDone = new AtomicBoolean(false);


    WebAppInterface(Context c, MainActivity mainActivity) {
        mContext = c;
        this.mainActivity = mainActivity;
    }

    /**
     * Show a toast from the web page
     */
    @JavascriptInterface
    public void stt() {
        if (mainActivity.getIsSttButtonActive().get() && activeThreads.get() <= threadThreshold) {
            executorService.submit(this::checkIfEspeakIsInstalled);
            activeThreads.incrementAndGet();
        }
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

    private void checkIfEspeakIsInstalled() {
        PackageManager pm = mContext.getPackageManager();
        if (mainActivity.getMTtsInstance() == null) {
            boolean isPackageInstalled = initTts(pm);
            if (!isPackageInstalled) {
                mainActivity.showInstallEspeakDialog();
            }
        }
        if (mainActivity.getMTtsInstance() != null &&
                mainActivity.getMTtsInstance().getIsFaLocaleInitialized().get()) {
            STT.getInstance().stt(mContext, mainActivity, STT_DO_COMMAND_CODE);
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
                        STT.getInstance().stt(mContext, mainActivity, STT_DO_COMMAND_CODE);
                    } else {
                        // run only once with one thread
                        if (!isTtsFaLocaleInstallationDone.get()) {
                            mainActivity.setIsSttButtonActive(false);
                            Intent installIntent = new Intent();
                            installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                            mainActivity.startActivityForResult(installIntent, INSTALL_TTS_DATA_CODE);
                            isTtsFaLocaleInstallationDone.set(true);
                        }
                    }
                } catch (InterruptedException e) {
                    Log.e(this.getClass().getName(), "thread interrupted waiting " +
                            "for tts init", e);
                }
            }
        }
        activeThreads.decrementAndGet();
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

package com.google.blockly.android.webview;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.google.blockly.android.webview.demo.MainActivity;
import com.google.blockly.android.webview.utility.Codes;
import com.google.blockly.android.webview.utility.STT;

public class WebAppInterface implements Codes {
    private Context mContext;
    private MainActivity mainActivity;

    WebAppInterface(Context c, MainActivity mainActivity) {
        mContext = c;
        this.mainActivity = mainActivity;
    }

    /**
     * Show a toast from the web page
     */
    @JavascriptInterface
    public void stt() {
        STT.getInstance().stt(mContext, mainActivity, STT_DO_COMMAND_CODE);
    }
}

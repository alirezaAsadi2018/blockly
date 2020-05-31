package com.google.blockly.android.webview.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.blockly.android.webview.R;


/**
 * The primary activity of the demo application. The activity embeds the
 * {@link com.google.blockly.android.webview.BlocklyWebViewFragment}.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

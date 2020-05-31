package com.google.blockly.android.webview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.util.Objects;

import static android.content.ContentValues.TAG;

/**
 * This fragments contains and manages the web view that hosts Blockly.
 */
public class BlocklyWebViewFragment extends Fragment {
    protected @Nullable
    WebView mWebView = null;
    private final boolean DEBUG_MODE = true;
    private final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 10;
    private final int MY_PERMISSIONS_REQUEST_INTERNET = 15;

    private void checkPermissions() {
        final FragmentActivity activity = getActivity();
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(activity),
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("RECORD_AUDIO Permission is not granted");
            // Permission is not granted
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
        } else {
            System.out.println("RECORD_AUDIO Permission is granted");
        }
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(activity),
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("INTERNET Permission is not granted");

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.INTERNET},
                    MY_PERMISSIONS_REQUEST_INTERNET);
        }else{
            System.out.println("INTERNET Permission is granted");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.err.println("Permission granted");
                    // permission was granted, yay! Do the
                    // task you need to do.
                } else {
                    System.err.println("Permission not granted");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_INTERNET: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.err.println("Permission granted");
                    // permission was granted, yay! Do the
                    // task you need to do.
                } else {
                    System.err.println("Permission not granted");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if(DEBUG_MODE)
                WebView.setWebContentsDebuggingEnabled(true);
        }
        mWebView = new WebView(inflater.getContext());
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                Log.d(TAG, "onPermissionRequest");
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        request.grant(request.getResources());
                    }
                });
            }
        });
        checkPermissions();
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/blockly/webview.html");
        return mWebView;
    }

    // TODO: Method to invoke code generation
    // TODO: Method to load workspace from string (or InputStream?)
    // TODO: Method to serialize workspace to string (or OutputStream?)
    // TODO: Clear / reset workspace
    // TODO: Load toolbox
    // TODO: Listener for event JSON
    // TODO: Method to evaluate JavaScript string in the WebView
}

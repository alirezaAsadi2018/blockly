package com.google.blockly.android.webview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.blockly.android.webview.demo.MainActivity;

import java.util.Objects;

import static android.content.ContentValues.TAG;

/**
 * This fragments contains and manages the web view that hosts Blockly.
 */
public class BlocklyWebViewFragment extends Fragment {
    protected @Nullable
    WebView mWebView = null;
    private final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 10;
    private final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE_FILE = 11;
    private final int MY_PERMISSIONS_REQUEST_READ_STORAGE_FILE = 12;
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 13;
    private final int MY_PERMISSIONS_REQUEST_GET_ACCOUNTS = 14;
    private final int MY_PERMISSIONS_REQUEST_INTERNET = 15;


    private void checkPermissions() {
        Objects.requireNonNull(mWebView).setWebChromeClient(new WebChromeClient() {

            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                Log.d(TAG, "onPermissionRequest");
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        request.grant(request.getResources());
                    }
                });
            }

        });
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Permission is not granted");
            // Permission is not granted
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
        } else {
            System.out.println("Permission is granted");
        }

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Permission is not granted");
            // Permission is not granted
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
        } else {
            System.out.println("Permission is granted");
        }

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Permission is not granted");
            // Permission is not granted
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_STORAGE_FILE);
        } else {
            System.out.println("Permission is granted");
        }

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    MY_PERMISSIONS_REQUEST_GET_ACCOUNTS);
        }

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.INTERNET},
                    MY_PERMISSIONS_REQUEST_INTERNET);
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
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE_FILE: {
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
            case MY_PERMISSIONS_REQUEST_READ_STORAGE_FILE: {
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
            case MY_PERMISSIONS_REQUEST_GET_ACCOUNTS: {
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
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
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
        mWebView = new WebView(inflater.getContext());
        checkPermissions();
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new WebAppInterface(getContext(), (MainActivity) getActivity()), "Android");
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

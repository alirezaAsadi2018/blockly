package com.google.blockly.android.webview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.blockly.android.webview.demo.MainActivity;

import static android.content.ContentValues.TAG;

/**
 * This fragments contains and manages the web view that hosts Blockly.
 */
public class BlocklyWebViewFragment extends Fragment {
    private final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 10;
    private final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE_FILE = 11;
    private final int MY_PERMISSIONS_REQUEST_READ_STORAGE_FILE = 12;
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 13;
    private final int MY_PERMISSIONS_REQUEST_GET_ACCOUNTS = 14;
    private final int MY_PERMISSIONS_REQUEST_INTERNET = 15;
    private final int MY_PERMISSIONS_REQUEST_BLUETOOTH = 16;
    private final int MY_PERMISSIONS_REQUEST_BLUETOOTH_ADMIN = 17;
    protected @Nullable WebView mWebView = null;

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Permission is not granted");
            // Permission is not granted
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
        } else {
            System.out.println("Permission is granted");
        }

        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Permission is not granted");
            // Permission is not granted
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
        } else {
            System.out.println("Permission is granted");
        }

        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Permission is not granted");
            // Permission is not granted
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_STORAGE_FILE);
        } else {
            System.out.println("Permission is granted");
        }

        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    MY_PERMISSIONS_REQUEST_GET_ACCOUNTS);
        }

        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.INTERNET},
                    MY_PERMISSIONS_REQUEST_INTERNET);
        }

        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.BLUETOOTH},
                    MY_PERMISSIONS_REQUEST_BLUETOOTH);
        }

        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.BLUETOOTH_ADMIN},
                    MY_PERMISSIONS_REQUEST_BLUETOOTH_ADMIN);
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
//        int PIC_WIDTH = mWebView.getRight() - mWebView.getLeft();

//        mWebView.setPadding(0, 0, 0, 0);
//        mWebView.setInitialScale(getScale(PIC_WIDTH));
//        mWebView.setInitialScale(80);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
//        settings.setDatabaseEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                Log.d(TAG, "onPermissionRequest");
                requireActivity().runOnUiThread(() ->
                        request.grant(request.getResources()));
            }

        });
        checkPermissions();
//        mWebView.evaluateJavascript("(function() { return 'this'; })();", value -> {
//            // value is the result returned by the Javascript as JSON
//            Log.d("LogName", value); // Prints: "this"
//        });
        //set debugging
        WebView.setWebContentsDebuggingEnabled(true);
        mWebView.addJavascriptInterface(new WebAppInterface(getContext(),
                (MainActivity) getActivity()), "Android");
        String webviewFileBase = "file:///android_asset/blockly/";
        mWebView.loadUrl(webviewFileBase + "webview.html");
        return mWebView;
    }

    private int getScale(int PIC_WIDTH) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().
                getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        double val = (double) width / (double) PIC_WIDTH;
        val = val * 100d;
        return (int) val;
    }
    // TODO: Method to invoke code generation
    // TODO: Method to load workspace from string (or InputStream?)
    // TODO: Method to serialize workspace to string (or OutputStream?)
    // TODO: Clear / reset workspace
    // TODO: Load toolbox
    // TODO: Listener for event JSON
    // TODO: Method to evaluate JavaScript string in the WebView

}

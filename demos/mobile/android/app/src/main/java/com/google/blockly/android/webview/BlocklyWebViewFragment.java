package com.google.blockly.android.webview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.blockly.android.webview.demo.MainActivity;
import com.google.blockly.android.webview.utility.Codes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * This fragments contains and manages the web view that hosts Blockly.
 */
public class BlocklyWebViewFragment extends Fragment implements Codes {
    protected @Nullable WebView mWebView = null;

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Permission is not granted");
            // Permission is not granted
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
        } else {
            System.out.println("Permission is granted");
        }

        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Permission is not granted");
            // Permission is not granted
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
        } else {
            System.out.println("Permission is granted");
        }

        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Permission is not granted");
            // Permission is not granted
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_STORAGE_FILE);
        } else {
            System.out.println("Permission is granted");
        }

        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    MY_PERMISSIONS_REQUEST_GET_ACCOUNTS);
        }

        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.INTERNET},
                    MY_PERMISSIONS_REQUEST_INTERNET);
        }

        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.BLUETOOTH},
                    MY_PERMISSIONS_REQUEST_BLUETOOTH);
        }

        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
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

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            if (requestCode == FILE_CHOOSER_RESULT_CODE) {
//                if (((MainActivity)requireActivity()).mValueCallback == null)
//                    return;
//                ((MainActivity)requireActivity()).mValueCallback.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
//                ((MainActivity)requireActivity()).mValueCallback = null;
//            }
//        } else if (requestCode == FILE_CHOOSER_RESULT_CODE) {
//            if (((MainActivity)requireActivity()).mUploadMessage == null)
//                return;
//            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
//            // Use RESULT_OK only if you're implementing WebView inside an Activity
//            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
//            ((MainActivity)requireActivity()).mUploadMessage.onReceiveValue(result);
//            ((MainActivity)requireActivity()).mUploadMessage = null;
//        } else
//            Toast.makeText(requireContext(), "Failed to Upload Image", Toast.LENGTH_LONG).show();

//        super.onActivityResult(requestCode, resultCode, intent);
//        if(requestCode == FILE_CHOOSER_RESULT_CODE){
//            if (Build.VERSION.SDK_INT >= 21) {
//                Uri[] results = null;
//                if (((MainActivity)requireActivity()).mValueCallback == null) return;
//                String dataString = intent == null || resultCode != RESULT_OK ? null : intent.getDataString();
//                results = new Uri[]{Uri.parse(dataString)};
//                ((MainActivity)requireActivity()).mValueCallback.onReceiveValue(results);
//                ((MainActivity)requireActivity()).mValueCallback = null;
//            } else {
//                if (((MainActivity)requireActivity()).mUploadMessage == null) return;
//                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
//                ((MainActivity)requireActivity()).mUploadMessage.onReceiveValue(result);
//                ((MainActivity)requireActivity()).mUploadMessage = null;
//            }
//        }
//    }

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
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);
//        settings.setBuiltInZoomControls(true);
//        settings.setSupportZoom(true);
//        settings.setDatabaseEnabled(true);
        mWebView.setWebViewClient(new MyWebClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            try {
                storeData(url);
            } catch (IOException e) {
                Log.e(this.getClass().getName(), "can not download roobin blocks: " + e.getMessage());
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
                (MainActivity) requireActivity()), "Android");
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

    class MyWebClient extends WebViewClient{
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
        }
    }

    class MyWebChromeClient extends WebChromeClient{
        @Override
        public void onPermissionRequest(final PermissionRequest request) {
            Log.d(TAG, "onPermissionRequest");
            requireActivity().runOnUiThread(() ->
                    request.grant(request.getResources()));
        }

        //The undocumented magic method override
        //Eclipse will swear at you if you try to put @Override here :)
        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            ((MainActivity)requireActivity()).mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            ((MainActivity)requireActivity()).mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            startActivityForResult(Intent.createChooser(i, "File Browser"), FILE_CHOOSER_RESULT_CODE);
        }

        //For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
            ((MainActivity)requireActivity()).mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
        }

        // For Lollipop 5.0+ Devices
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (((MainActivity)requireActivity()).mValueCallback != null) {
//                ((MainActivity)requireActivity()).mValueCallback.onReceiveValue(new Uri[]{});
                ((MainActivity)requireActivity()).mValueCallback = null;
            }
            ((MainActivity)requireActivity()).mValueCallback = filePathCallback;
            Intent intent = fileChooserParams.createIntent();
            try {
                requireActivity().startActivityForResult(intent, FILE_CHOOSER_RESULT_CODE);
            } catch (ActivityNotFoundException e) {
                filePathCallback = null;
                Toast.makeText(requireContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                return true;
            }
            return true;
        }
    }

    private void storeData(String textData) throws IOException {
        final int notificationId = 1;
        final String name = "RoobinBlocks.txt";
        final File dwldsPath = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS) + "/" + name);
        String data = textData.replaceFirst("^data:text/plain;charset=utf-8,", "");
        data = URLDecoder.decode(data, "UTF-8");
        FileWriter os = new FileWriter(dwldsPath, false);
        os.write(data);
        os.flush();

        if (dwldsPath.exists()) {
            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            Uri apkURI = FileProvider.getUriForFile(requireContext(), requireContext().getApplicationContext().
                    getPackageName() + ".provider", dwldsPath);
            intent.setDataAndType(apkURI, MimeTypeMap.getSingleton().getMimeTypeFromExtension("text"));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 1, intent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            String CHANNEL_ID = "MYCHANNEL";
            final NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(
                    Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "download",
                        NotificationManager.IMPORTANCE_LOW);
                Notification notification = new Notification.Builder(getContext(), CHANNEL_ID)
                        .setContentText("You have got something new!")
                        .setContentTitle("File downloaded")
                        .setContentIntent(pendingIntent)
                        .setChannelId(CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.sym_action_chat)
                        .build();
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(notificationChannel);
                    notificationManager.notify(notificationId, notification);
                }

            } else {
                NotificationCompat.Builder b = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(android.R.drawable.sym_action_chat)
                        //.setContentIntent(pendingIntent)
                        .setContentTitle("download")
                        .setContentText(name);

                if (notificationManager != null) {
                    notificationManager.notify(notificationId, b.build());
                    Handler h = new Handler();
                    long delayInMilliseconds = 1000;
                    h.postDelayed(() -> notificationManager.cancel(notificationId), delayInMilliseconds);
                }
            }
        }
        Toast.makeText(getContext(), "FILE DOWNLOADED!", Toast.LENGTH_SHORT).show();
    }
}

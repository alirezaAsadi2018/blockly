package com.google.blockly.android.webview.utility;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.google.blockly.android.webview.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PackageInstallerActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String path = "android.resource://" + this.getPackageName() + "/raw/app2.apk";
//        InputStream inputStream = getResources().openRawResource(R.raw.app);
        InputStream inputStream = null;
        try {
            File tempFile = File.createTempFile("pre", "suf");
            copyFile(inputStream, new FileOutputStream(tempFile));
            Uri appUri = FileProvider.getUriForFile(getApplicationContext(),
                    getApplicationContext().getPackageName() + ".provider", tempFile);


//            intent.setDataAndType(Uri.fromFile(tempFile),
//                    "application/vnd.android.package-archive");


//            intent.setDataAndType(appUri, "application/vnd.android.package-archive");
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            startActivity(intent);



            Log.e("asd", "app installed");
        } catch (IOException e) {
            Log.e(this.getClass().getName(), e.getMessage(), e);
        }

//        intent.setDataAndType(Uri.fromFile(new File(fileName)),
//                "application/vnd.android.package-archive");
//        startActivity(intent);
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
}
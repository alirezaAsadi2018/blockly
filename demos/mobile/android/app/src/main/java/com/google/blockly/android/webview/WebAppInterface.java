package com.google.blockly.android.webview;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
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
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import me.aflak.bluetooth.Bluetooth;


public class WebAppInterface implements Codes {
    private final Context mContext;
    private final MainActivity mainActivity;
    private final AtomicBoolean isTtsLocaleInstallationDone = new AtomicBoolean(false);
    private final Bluetooth bluetooth;
    private BluetoothAdapter BA = BluetoothAdapter.getDefaultAdapter();
    private Set<BluetoothDevice> pairedDevices;
    private BluetoothSocket bluetoothSocket;
    private ObjectOutputStream outputStream;


    WebAppInterface(Context mContext, MainActivity mainActivity) {
        this.mContext = mContext;
        this.mainActivity = mainActivity;
        bluetooth = new Bluetooth(mContext);
        bluetooth.onStart();
    }

    @JavascriptInterface
    public boolean on() {
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mainActivity.startActivityForResult(turnOn, BLUETOOTH_ACTION_REQUEST_ENABLE);
            return true;
        } else {
            // bluetooth is already on
            return false;
        }
    }

    @JavascriptInterface
    public void off() {
        BA.disable();
        mainActivity.runOnUiThread(() -> Toast.makeText(mContext, "Turned off", Toast.LENGTH_LONG)
                .show());
    }

    @JavascriptInterface
    public void visible() {
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        getVisible.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
                BLUETOOTH_DISCOVERABLE_DURATION);
        mainActivity.startActivityForResult(getVisible, BLUETOOTH_ACTION_REQUEST_DISCOVERABLE);
    }

    @JavascriptInterface
    public String list() throws InterruptedException {
        if(on()){
            synchronized (mainActivity.isBluetoothEnabled) {
                mainActivity.isBluetoothEnabled.wait();
            }
            if(!mainActivity.isBluetoothEnabled.get()){
                //TODO
            }
        }

        visible();
        synchronized (mainActivity.isBluetoothDiscoverable){
            mainActivity.isBluetoothDiscoverable.wait();
        }
        if(!mainActivity.isBluetoothDiscoverable.get()){
            //TODO
        }
        pairedDevices = BA.getBondedDevices();
        StringBuilder devices = new StringBuilder();
        for (BluetoothDevice bt : pairedDevices) {
            devices.append(bt.getName()).append(", ");
        }
        if (devices.length() >= 2) {
            devices.delete(devices.length() - 2, devices.length());
        }
        return devices.toString();
    }

    @JavascriptInterface
    public void connectBluetooth(String name) {
        on();
        visible();
        boolean isBonded = false;
        try {
            for (BluetoothDevice pairedDevice : pairedDevices) {
                if (pairedDevice.getName().equalsIgnoreCase(name)) {
                    BluetoothDevice remoteDevice = BA.getRemoteDevice(pairedDevice.getAddress());
                    isBonded = remoteDevice.createBond();
                    ParcelUuid[] uuids = remoteDevice.getUuids();
                    if (uuids != null) {
                        bluetoothSocket = remoteDevice.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                        bluetoothSocket.connect();
                    }
                    break;
                }
            }

        } catch (Exception e) {
            Log.e(this.getClass().getName(), e + ", can not pair with the bluetooth device");
        }
    }

    @JavascriptInterface
    public void zero() {
        try {
//            bluetoothSocket.getOutputStream().write(48);
            outputStream.writeObject('0');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void one() {
        try {
//            bluetoothSocket.getOutputStream().write(49);
            outputStream.writeObject('1');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void send(String text) {
        try {
            text = new String(text.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            System.out.println("sending to blue: " + text);
            for (char c : text.toCharArray()) {
                bluetoothSocket.getOutputStream().write((byte) c);
            }
            bluetoothSocket.getOutputStream().write((byte) '\n');
            bluetoothSocket.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void scan() {
        BA.startDiscovery();
        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                ArrayList<String> list = new ArrayList<>();
                //Finding devices
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    if (device != null)
                        list.add(device.getName() + "\n" + device.getAddress());
                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mainActivity.registerReceiver(mReceiver, filter);
    }

    @JavascriptInterface
    public void tts(String text, String lang) {
        if (checkIfEspeakIsInstalled(lang)) {
            mainActivity.getMTtsInstance().tts(text, IDLE_UTTERANCE_ID);
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

package com.google.blockly.android.webview.utility;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.ParcelUuid;
import android.util.Log;

import com.google.blockly.android.webview.demo.MainActivity;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Set;

import me.aflak.bluetooth.Bluetooth;

public class BluetoothController implements Codes {
    private final Bluetooth bluetooth;
    private final Context mContext;
    private final MainActivity mainActivity;
    private BluetoothAdapter BA = BluetoothAdapter.getDefaultAdapter();
    private Set<BluetoothDevice> pairedDevices;
    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice lastConnectedBluetoothDevice;

    public BluetoothController(Context mContext, MainActivity mainActivity) {
        this.mContext = mContext;
        this.mainActivity = mainActivity;
        bluetooth = new Bluetooth(mContext);
        bluetooth.onStart();
    }


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


    public void off() {
        BA.disable();
    }


    public void visible() {
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        getVisible.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
                BLUETOOTH_DISCOVERABLE_DURATION);
        mainActivity.startActivityForResult(getVisible, BLUETOOTH_ACTION_REQUEST_DISCOVERABLE);
    }


    public String list() throws InterruptedException {
        if (on()) {
            synchronized (mainActivity.isBluetoothEnabled) {
                mainActivity.isBluetoothEnabled.wait();
            }
            if (!mainActivity.isBluetoothEnabled.get()) {
                //TODO
            }
        }

        visible();
        synchronized (mainActivity.isBluetoothDiscoverable) {
            mainActivity.isBluetoothDiscoverable.wait();
        }
        if (!mainActivity.isBluetoothDiscoverable.get()) {
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

    public boolean isConnected(String name){
        on();
        visible();
        return lastConnectedBluetoothDevice != null && lastConnectedBluetoothDevice.getName().equalsIgnoreCase(name)
                && bluetoothSocket != null;
    }


    public void connectBluetooth(String name) throws IOException {
        on();
        visible();
        for (BluetoothDevice pairedDevice : pairedDevices) {
            if (pairedDevice.getName().equalsIgnoreCase(name)) {
                lastConnectedBluetoothDevice = BA.getRemoteDevice(pairedDevice.getAddress());
                connectBluetooth();
                break;
            }
        }
    }

    private void connectBluetooth() throws IOException {
        on();
        visible();
        boolean isBonded = lastConnectedBluetoothDevice.createBond();
        ParcelUuid[] uuids = lastConnectedBluetoothDevice.getUuids();
        if (uuids != null) {
            bluetoothSocket = lastConnectedBluetoothDevice.createRfcommSocketToServiceRecord(uuids[0].getUuid());
            bluetoothSocket.connect();
        }
    }

    public void send(String text) throws IOException {
        text = new String(text.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        Log.d(this.getClass().getName(), "sending to blue: " + text);
        if(bluetoothSocket == null){
            return;
        }
        for (char c : text.toCharArray()) {
            bluetoothSocket.getOutputStream().write((byte) c);
        }
        bluetoothSocket.getOutputStream().write((byte) '\n');
        bluetoothSocket.getOutputStream().flush();
    }


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

    public void restart() {
        try {
            bluetoothSocket.close();
            Thread.sleep(5000);
            connectBluetooth();
        } catch (IOException e) {
            Log.e(this.getClass().getName(), "error when trying to close bluetooth socket!");
        } catch (InterruptedException e) {
            Log.e(this.getClass().getName(), "bluetooth restart method thread interrupted while sleeping!");
        }

    }
}

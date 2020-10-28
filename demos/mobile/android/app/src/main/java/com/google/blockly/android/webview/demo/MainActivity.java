package com.google.blockly.android.webview.demo;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.blockly.android.webview.MediaPlayerService;
import com.google.blockly.android.webview.R;
import com.google.blockly.android.webview.utility.BluetoothController;
import com.google.blockly.android.webview.utility.Codes;
import com.google.blockly.android.webview.utility.STT;
import com.google.blockly.android.webview.utility.TTS;
import com.google.blockly.android.webview.utility.WeatherApiRequestState;
import com.google.blockly.android.webview.utility.WeatherReport;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The primary activity of the demo application. The activity embeds the
 * {@link com.google.blockly.android.webview.BlocklyWebViewFragment}.
 */
public class MainActivity extends AppCompatActivity implements Codes {
    private final AtomicBoolean isSttButtonActive = new AtomicBoolean(true);
    private TTS mTtsInstance;
    private BluetoothController mBluetoothControllerInstance;
    public String sttResult;
    public final AtomicBoolean isBluetoothEnabled = new AtomicBoolean(false);
    public final AtomicBoolean isBluetoothDiscoverable = new AtomicBoolean(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        STT.getInstance().startLanguageReceiver(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.cancelAll();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == STT_DO_COMMAND_CODE || requestCode == STT_GET_CITY_NAME
                || requestCode == STT_GET_NAME) {
            STT.getInstance().setIsListening(false);
        }
        if (requestCode == INSTALL_TTS_DATA_CODE) {
            if (resultCode != RESULT_OK) {
                showInstallTtsVoiceDataDialog();
            } else {
                new Handler().postDelayed(() -> {
                    mTtsInstance.stop();
                    mTtsInstance = null;
                    setIsSttButtonActive(true);
                }, 1000);
            }
        }
        if(requestCode == BLUETOOTH_ACTION_REQUEST_ENABLE){
            synchronized (isBluetoothEnabled){
                isBluetoothEnabled.notifyAll();
            }
            System.out.println("resultCode on: " + resultCode);
            if(resultCode == RESULT_OK){
                isBluetoothEnabled.set(true);
            }else{
                isBluetoothEnabled.set(false);
            }
        }
        if(requestCode == BLUETOOTH_ACTION_REQUEST_DISCOVERABLE){
            System.out.println("resultCode discover: " + resultCode);
            synchronized (isBluetoothDiscoverable){
                isBluetoothDiscoverable.notifyAll();
            }
            if(resultCode == RESULT_OK || resultCode == BLUETOOTH_DISCOVERABLE_DURATION){
                isBluetoothDiscoverable.set(true);
            }else{
                isBluetoothDiscoverable.set(false);
            }
        }
        if (resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String text = Objects.requireNonNull(result).get(0);
            sttResult = text;
//            switch (requestCode) {
//                case STT_DO_COMMAND_CODE: //STT action from js result -> getting a command
//                    if (text.contains("آب و هوا") || text.contains("آب وهوا") ||
//                            text.contains("اب و هوا") || text.contains("اب وهوا") ||
//                            text.contains("آب\u200Cو\u200Cهوا") || text.contains("آب\u200Cوهوا") ||
//                            text.contains("آب\u200Cو هوا")) {
//                        Toast.makeText(getApplicationContext(), "weather", Toast.LENGTH_SHORT).show();
//                        STT.getInstance().setIsBusyAskingForInfo(true);
//                        askForCityName();
//                    } else if (text.contains("آهنگ") || text.contains("اهنگ") || text.contains("موزیک")) {
//                        Toast.makeText(getApplicationContext(), "music", Toast.LENGTH_SHORT).show();
//                        playMusic();
//                    } else if (text.contains("بازی")) {
//                        Toast.makeText(getApplicationContext(), "game", Toast.LENGTH_SHORT).show();
//                        openGameMenu();
//                    } else if (text.contains("سلام")) {
//                        Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_SHORT).show();
//                        STT.getInstance().setIsBusyAskingForInfo(true);
//                        askForName();
//                    }
//                    break;
//                case STT_GET_NAME:
//                    sayHello(text);
//                    break;
//                case STT_GET_CITY_NAME:
//                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
//                    reportWeather(text);
//                    break;
//            }
        } else if (resultCode == RESULT_CANCELED) {
            Log.i(MainActivity.class.getName(), "activity result cancelled!");
        } else {
            Log.i(MainActivity.class.getName(), "result is null in activity results!");
        }
        //notify the thread waiting for stt result in webView interface
        synchronized (this) {
            this.notifyAll();
        }
    }

    private void askForCityName() {
        String message = getResources().getString(R.string.tts_weather_ask_for_city_name);
        mTtsInstance.tts(message, ASK_CITY_UTTERANCE_ID);
    }

    private void sayHello(String name) {
        String message = getResources().getString(R.string.tts_greeting_hello) + " " + name;
        mTtsInstance.tts(message, IDLE_UTTERANCE_ID);
    }

    private void askForName() {
        String message = getResources().getString(R.string.tts_greeting_ask_for_name);
        mTtsInstance.tts(message, ASK_NAME_UTTERANCE_ID);
    }

    private void openGameMenu() {
        //TODO
    }


    private void reportWeather(String city) {
        WeatherReport.getInstance(this).getWeatherReport(city);
    }

    public void onWeatherForecastResult() {
        WeatherReport instance = WeatherReport.getInstance(this);

        //weather forecast part
        if (instance.getWeatherForecastState() == WeatherApiRequestState.DONE) {
            callTtsForWeatherForcast(getString(R.string.tts_weather_humidity),
                    instance.getHumidity());
            callTtsForWeatherForcast(getString(R.string.tts_weather_temp),
                    instance.getTemp());
            callTtsForWeatherForcast(getString(R.string.tts_weather_description),
                    instance.getWeatherDescription());
        } else {
            //Error with weather forecast api request
            mTtsInstance.tts(getString(R.string.city_weather_info_not_found), IDLE_UTTERANCE_ID);
//            mTtsInstance.tts(instance.getWeatherForecastErrorMessage(), IDLE_UTTERANCE_ID);
        }
    }

    public void onWeatherAqiRequestResult() {
        WeatherReport instance = WeatherReport.getInstance(this);

        //weather quality part
        if (instance.getAqiState() == WeatherApiRequestState.DONE) {
            callTtsForWeatherForcast(getString(R.string.tts_air_pollution),
                    instance.getAirPollutionResponse());
        } else {
            //Error with aqi api request
            mTtsInstance.tts(getString(R.string.city_aqi_info_not_found), IDLE_UTTERANCE_ID);
//            mTtsInstance.tts(instance.getAqiErrorMessage(), IDLE_UTTERANCE_ID);
        }
    }

    private void callTtsForWeatherForcast(String key, String value) {
        String message = key + " " + value;
        mTtsInstance.tts(message, IDLE_UTTERANCE_ID);
    }


    private void playMusic() {
        Intent intent = new Intent(getApplicationContext(), MediaPlayerService.class);
        intent.setAction(MediaPlayerService.ACTION_PLAY);
        startService(intent);
    }

    public void showRestartDialog(DialogInterface.OnClickListener onNeutralButtonClickedListener,
                                  DialogInterface.OnCancelListener onCancelListener,
                                  final Runnable cleaningBeforeRestart) {
        final Context mContext = getApplicationContext();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage(R.string.tts_engine_loading);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setPositiveButton(R.string.restart, (DialogInterface dialog, int id) -> {
            Thread thread = new Thread(cleaningBeforeRestart);
            // do cleaning before restart
            thread.start();
            try {
                //wait for cleaning to be done
                thread.join();
                //restart the program
                Intent i = new Intent(mContext, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(i);
                dialog.cancel();
            } catch (InterruptedException e) {
                Log.e(this.getClass().getName(), "thread interrupted while doing " +
                        "clean up before restart!", e);
            }
        });
        dialogBuilder.setNeutralButton(R.string.close, onNeutralButtonClickedListener);
        dialogBuilder.setOnCancelListener(onCancelListener);
        dialogBuilder.create().show();
    }

    public void showInstallTtsVoiceDataDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(R.string.you_dont_have_espeak_voice_data_installed);
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                R.string.install_voice_data, (DialogInterface dialog, int id) -> {
                    dialog.cancel();
                    Intent installIntent = new Intent();
                    installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivityForResult(installIntent, INSTALL_TTS_DATA_CODE);
                });

        runOnUiThread(() -> {
            AlertDialog alert11 = builder1.create();
            alert11.show();
        });
    }

    private void showInstallDialog(int message, Uri playMarketLinkUri, Uri directLinkUri) {
        final Context mContext = getApplicationContext();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(message);
        builder1.setCancelable(true);


        builder1.setNeutralButton(R.string.close, (dialog, id) -> dialog.cancel());

        builder1.setPositiveButton(
                R.string.install_from_google_play, (DialogInterface dialog, int id) -> {
                    dialog.cancel();
                    try {
                        Intent viewIntent = new Intent("android.intent.action.VIEW", playMarketLinkUri);
                        startActivity(viewIntent);
                    } catch (android.content.ActivityNotFoundException e) {
                        //TODO
                        //No Activity found to handle Intent { act=android.intent.action.VIEW dat=market://details?id=com.redzoc.ramees.tts.espeak&hl=en }
                        //use another strategy
                    } catch (Exception e) {
                        Toast.makeText(mContext, R.string.unable_to_connect,
                                Toast.LENGTH_LONG).show();
                        Log.e(this.getClass().getName(), "Unable to Connect to download url", e);
                    }
                });

        builder1.setNegativeButton(
                R.string.install_directly, (DialogInterface dialog, int id) -> {
                    dialog.cancel();
                    try {
                        Intent viewIntent = new Intent("android.intent.action.VIEW", directLinkUri);
                        startActivity(viewIntent);
                    } catch (Exception e) {
                        Toast.makeText(mContext, R.string.unable_to_connect,
                                Toast.LENGTH_LONG).show();
                        Log.e(this.getClass().getName(), "Unable to Connect to download url", e);
                    }
                });

        runOnUiThread(() -> {
            AlertDialog alert11 = builder1.create();
            alert11.show();
        });
    }

    public void showInstallEspeakDialog() {
        showInstallDialog(R.string.you_dont_have_espeak_engine,
                Uri.parse("market://details?id=com.redzoc.ramees.tts.espeak&hl=en"),
                Uri.parse("https://bit.ly/33WrSMa"));
    }

    public void showInstallGoogleSearchBoxSttDialog() {
        Uri picofileDirectLink = Uri.parse("https://bit.ly/36PwSmY");
        Uri uploadBoyDirectLink = Uri.parse("https://bit.ly/2yQv8gR");
        showInstallDialog(R.string.you_dont_have_google_stt_engine,
                Uri.parse("market://details?id=com.google.android.googlequicksearchbox"),
                picofileDirectLink);
    }

    public AtomicBoolean getIsSttButtonActive() {
        return isSttButtonActive;
    }

    public void setIsSttButtonActive(boolean isSttButtonActive) {
        this.isSttButtonActive.set(isSttButtonActive);
    }

    public TTS getMTtsInstance() {
        return mTtsInstance;
    }

    public BluetoothController getmBluetoothControllerInstance() {
        if(mBluetoothControllerInstance == null){
            mBluetoothControllerInstance = new BluetoothController(getApplicationContext(), this);
        }
        return mBluetoothControllerInstance;
    }

    public void setMTtsInstance(TTS mTtsInstance) {
        this.mTtsInstance = mTtsInstance;
    }
}

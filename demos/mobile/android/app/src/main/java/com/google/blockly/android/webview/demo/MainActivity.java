package com.google.blockly.android.webview.demo;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.blocklywebview.R;

import java.util.ArrayList;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String command = result.get(0);
                    if (command.contains("آب و هوا") || command.contains("آب وهوا")
                            || command.contains("اب و هوا") || command.contains("اب وهوا")) {
                        Toast.makeText(getApplicationContext(), "weather", Toast.LENGTH_LONG).show();
                        reportWeather();
                    } else if (command.contains("آهنگ") || command.contains("اهنگ") || command.contains("موزیک")) {
                        Toast.makeText(getApplicationContext(), "music", Toast.LENGTH_LONG).show();
                        playMusic();
                    } else if (command.contains("بازی")) {
                        Toast.makeText(getApplicationContext(), "game", Toast.LENGTH_LONG).show();
                        openGameMenu();
                    } else if (command.contains("سلام")) {
                        Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_LONG).show();
                        askForName();
                    }
//                    Toast.makeText(getApplicationContext(), result.get(0), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void askForName() {

    }

    private void openGameMenu() {

    }

    private void reportWeather() {

    }

    private void playMusic() {

    }
}

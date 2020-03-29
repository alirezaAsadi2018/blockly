package com.google.blockly.android.webview.demo;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.ClientError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.blockly.android.webview.MediaPlayerService;
import com.google.blockly.android.webview.R;
import com.google.blockly.android.webview.utility.Codes;
import com.google.blockly.android.webview.utility.TTS;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * The primary activity of the demo application. The activity embeds the
 * {@link com.google.blockly.android.webview.BlocklyWebViewFragment}.
 */
public class MainActivity extends AppCompatActivity implements Codes {
    private TTS mTtsInstance;
    private Boolean weatherApiIsOk = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTtsInstance = new TTS(getApplicationContext(), this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String text = Objects.requireNonNull(result).get(0);
            switch (requestCode) {
                case STT_DO_COMMAND_CODE: //STT action from js result -> getting a command
                    if (text.contains("آب و هوا") || text.contains("آب وهوا")
                            || text.contains("اب و هوا") || text.contains("اب وهوا")) {
                        Toast.makeText(getApplicationContext(), "weather", Toast.LENGTH_LONG).show();
                        askForCityName();
                    } else if (text.contains("آهنگ") || text.contains("اهنگ") || text.contains("موزیک")) {
                        Toast.makeText(getApplicationContext(), "music", Toast.LENGTH_LONG).show();
                        playMusic();
                    } else if (text.contains("بازی")) {
                        Toast.makeText(getApplicationContext(), "game", Toast.LENGTH_LONG).show();
                        openGameMenu();
                    } else if (text.contains("سلام")) {
                        Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_LONG).show();
                        askForName();
                    }
                    break;
                case STT_GET_NAME:
                    sayHello(text);
                    break;
                case STT_GET_CITY_NAME:
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                    reportWeather(text);
                    break;
            }
        } else {
            Log.e(MainActivity.class.getName(), "Error in converting speech to text!");
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

    }


    private void reportWeather(String city) {
        weatherApiIsOk = true;
        RequestQueue queue = Volley.newRequestQueue(this);
        String weather_api_key = "521daac643e52381dfd9c18688956964";
        String weather_api_url = "https://api.openweathermap.org/data/2.5/weather?q=" + city
                + "&units=metric&appid=" + weather_api_key + "&lang=fa"; //units = metric -> celsius °C
        JsonObjectRequest weatherRequest = new JsonObjectRequest
                (Request.Method.GET, weather_api_url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (!weatherApiIsOk)
                            return;
                        JSONObject main = null;
                        try {
                            main = response.getJSONObject("main");
                            JSONObject sys = response.getJSONObject("sys");
                            JSONObject wind = response.getJSONObject("wind");
                            JSONObject weather = response.getJSONArray("weather").getJSONObject(0);
                            String tempMin = "Min Temp: " + main.getString("temp_min") +
                                    getResources().getString(R.string.degree_centigrade);
                            String tempMax = "Max Temp: " + main.getString("temp_max") +
                                    getResources().getString(R.string.degree_centigrade);
                            String pressure = main.getString("pressure");
                            String address = response.getString("name") + ", " +
                                    sys.getString("country");

                            long updatedAt = response.getLong("dt");
                            String updatedAtText = "Updated at: " + new SimpleDateFormat(
                                    "dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                                    new Date(updatedAt * 1000));
                            String temp = main.getString("temp") +
                                    getResources().getString(R.string.degree_centigrade);
                            String humidity = main.getString("humidity");
                            String weatherDescription = weather.getString("description");

                            callTtsForWeatherForcast(getResources().getString(
                                    R.string.tts_weather_humidity), humidity);
                            callTtsForWeatherForcast(getResources().getString(
                                    R.string.tts_weather_temp), temp);
                            callTtsForWeatherForcast(getResources().getString(
                                    R.string.tts_weather_description), weatherDescription);
                        } catch (JSONException error) {
                            Log.e(this.getClass().getName(), "json form is not correct for " +
                                    "weather forecast api", error);
                            weatherApiIsOk = false;
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(this.getClass().getName(), "weather forecast api response error: " +
                                error.getMessage(), error);
                        if (error instanceof ClientError) {
                            String message = getResources().getString(R.string.city_name_not_found);
                            mTtsInstance.tts(message, IDLE_UTTERANCE_ID);
                        }
                        weatherApiIsOk = false;
                    }
                });
        String aqi_api_key = "727fd99bd0e91546eed5b6910804d3da4d29d099";
        String aqi_url = "https://api.waqi.info/search/?token=" + aqi_api_key + "&keyword=" + city;
        JsonObjectRequest aqiRequest = new JsonObjectRequest
                (Request.Method.GET, aqi_url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (!weatherApiIsOk)
                            return;
                        JSONObject data = null;
                        try {
                            if (response.getString("status").equals("ok")) {//it's always OK
                                if (response.getJSONArray("data").length() == 0) {
                                    // TODO
                                    //search the city name in English or
                                    // search for other stations if available or
                                    // at last show a good message
                                    return;
                                }
                                data = response.getJSONArray("data").getJSONObject(0);
                                if (data.getString("aqi").equals("-")) {
                                    // TODO
                                    //search the city name in English or
                                    // search for other stations if available or
                                    // at last show a good message
                                    return;
                                }
                                int aqi = Integer.parseInt(data.getString("aqi"));
                                String airPollutionResponse;
                                if (aqi >= 0 && aqi <= 50) {
                                    //Good
                                    airPollutionResponse = getResources().getString(R.string.good);
                                } else if (aqi <= 100) {
                                    //Moderate
                                    airPollutionResponse = getResources().getString(R.string.moderate);
                                } else if (aqi <= 150) {
                                    //Unhealthy for Sensitive Groups
                                    airPollutionResponse = getResources().getString(
                                            R.string.unhealthy_for_sensitive_groups);
                                } else if (aqi <= 200) {
                                    //Unhealthy
                                    airPollutionResponse = getResources().getString(R.string.unhealthy);
                                } else if (aqi <= 300) {
                                    //Very Unhealthy
                                    airPollutionResponse = getResources().getString(
                                            R.string.very_unhealthy);
                                } else {
                                    //Hazardous
                                    airPollutionResponse = getResources().getString(R.string.hazardous);
                                }
                                callTtsForWeatherForcast(getResources().getString(
                                        R.string.tts_air_pollution), airPollutionResponse);

                            }
                        } catch (JSONException error) {
                            Log.e(this.getClass().getName(), "json form is not correct for aqi api"
                                    , error);
                            weatherApiIsOk = false;
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(this.getClass().getName(), "aqi api response error: " +
                                error.getMessage(), error);
                        weatherApiIsOk = false;
                    }
                });
        queue.add(weatherRequest);
        queue.add(aqiRequest);
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
}

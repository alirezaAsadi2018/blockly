package com.google.blockly.android.webview.utility;

import android.util.Log;

import com.android.volley.ClientError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.blockly.android.webview.R;
import com.google.blockly.android.webview.demo.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class WeatherReport implements Codes {
    private static WeatherReport instance;
    private MainActivity mainActivity;
    private String weather_api_uri_template;
    private String weather_api_key;
    private String aqi_api_key;
    private String aqi_api_uri_template;
    private RequestQueue weatherQueue;
    private RequestQueue aqiQueue;
    // weather forecast vars
    private String tempMin;
    private String tempMax;
    private String pressure;
    private String address;
    private long updatedAt;
    private String updatedAtText;
    private String temp;
    private String humidity;
    private String weatherDescription;
    private WeatherApiRequestState weatherForecastState = WeatherApiRequestState.UNDONE;
    private String weatherForecastErrorMessage;
    //air quality variables
    int aqi;
    private String airPollutionResponse;
    private WeatherApiRequestState aqiState = WeatherApiRequestState.UNDONE;
    private String aqiErrorMessage;
    private AtomicInteger counter = new AtomicInteger(0);
    //enum showing request states

    public static WeatherReport getInstance(MainActivity mainActivity){
        if(instance == null){
            return instance = new WeatherReport(mainActivity);
        }else {
            return instance;
        }
    }

    private WeatherReport(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        //set weather forecast key and its template_uri without city name
        weather_api_key = mainActivity.getString(R.string.weather_api_key);
        //units = metric -> celsius °C
        weather_api_uri_template = mainActivity.getString(R.string.weather_api_uri_template) + "&appid=" +
                weather_api_key;
        //set air quality key and its template_uri without city name
        aqi_api_key = mainActivity.getString(R.string.aqi_api_key);
        aqi_api_uri_template = mainActivity.getString(R.string.aqi_api_uri_template) + "?token=" +
                aqi_api_key;
        // init request queue for sending json to the apis
        weatherQueue = Volley.newRequestQueue(mainActivity);
        aqiQueue = Volley.newRequestQueue(mainActivity);
        weatherQueue.addRequestFinishedListener(req->mainActivity.onWeatherForecastResult());
        aqiQueue.addRequestFinishedListener(req->mainActivity.onWeatherAqiRequestResult());
    }


    public void getWeatherReport(String city) {
        String weather_api_uri = weather_api_uri_template + "&q=" + city;
        JsonObjectRequest weatherRequest = new JsonObjectRequest
                (Request.Method.GET, weather_api_uri, null, this::getWeatherForecast,
                        new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(this.getClass().getName(), "weather forecast api response error: " +
                                error.getMessage(), error);
                        if (error instanceof ClientError) {
                            weatherForecastErrorMessage = mainActivity.getString(R.string.city_name_not_found);
                        }else{
                            weatherForecastErrorMessage = mainActivity.getString(
                                    R.string.weather_forecast_response_error);
                        }
                        weatherForecastState = WeatherApiRequestState.ERROR;
                    }
                });

        String aqi_api_uri = aqi_api_uri_template + "&keyword=" + city;
        JsonObjectRequest aqiRequest = new JsonObjectRequest
                (Request.Method.GET, aqi_api_uri, null, this::getAqiReport,
                        new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(this.getClass().getName(), "aqi api response error: " +
                                error.getMessage(), error);
                        aqiErrorMessage = mainActivity.getString(R.string.aqi_response_error);
                        aqiState = WeatherApiRequestState.ERROR;
                    }
                });
        weatherQueue.add(weatherRequest);
        aqiQueue.add(aqiRequest);

    }

    public void getAqiReport(JSONObject response){
        JSONObject data = null;
        try {
            if (response.getString("status").equals("ok")) {//it's always OK
                if (response.getJSONArray("data").length() == 0) {
                    // TODO
                    //search the city name in English or
                    // search for other stations if available or
                    // at last show a good message
                    aqiErrorMessage = mainActivity.getString(R.string.city_name_not_found);
                    aqiState = WeatherApiRequestState.ERROR;
                    return;
                }
                data = response.getJSONArray("data").getJSONObject(0);
                if (data.getString("aqi").equals("-")) {
                    // TODO
                    //search the city name in English or
                    // search for other stations if available or
                    // at last show a good message
                    aqiErrorMessage = mainActivity.getString(R.string.city_name_not_found);
                    aqiState = WeatherApiRequestState.ERROR;
                    return;
                }
                setAqi(Integer.parseInt(data.getString("aqi")));
                setAirPollutionResponse(aqi);
                aqiState = WeatherApiRequestState.DONE;
            }
        } catch (JSONException error) {
            Log.e(this.getClass().getName(), "json form is not correct for aqi api"
                    , error);
            aqiErrorMessage = mainActivity.getString(R.string.aqi_response_error);
            aqiState = WeatherApiRequestState.ERROR;
        }
    }

    public void getWeatherForecast(JSONObject response){
        JSONObject main = null;
        try {
            main = response.getJSONObject("main");
            JSONObject sys = response.getJSONObject("sys");
            JSONObject wind = response.getJSONObject("wind");
            JSONObject weather = response.getJSONArray("weather").getJSONObject(0);

            setTempMin(main.getString("temp_min"));
            setTempMax(main.getString("temp_max"));
            setPressure(main.getString("pressure"));
            setAddress(response.getString("name"), sys.getString("country"));
            setUpdatedAt(response.getLong("dt"));
            setUpdatedAtText(getUpdatedAt());
            setTemp(main.getString("temp"));
            setHumidity(main.getString("humidity"));
            setWeatherDescription(weather.getString("description"));

            weatherForecastState = WeatherApiRequestState.DONE;
        } catch (JSONException error) {
            Log.e(this.getClass().getName(), "json form is not correct for " +
                    "weather forecast api", error);
            weatherForecastErrorMessage = mainActivity.getString(R.string.city_name_not_found);
            weatherForecastState = WeatherApiRequestState.ERROR;
        }
    }

    private long getUpdatedAt() {
        return updatedAt;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getTemp() {
        return temp;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public WeatherApiRequestState getWeatherForecastState() {
        return weatherForecastState;
    }

    public String getWeatherForecastErrorMessage() {
        return weatherForecastErrorMessage;
    }

    public String getAqiErrorMessage() {
        return aqiErrorMessage;
    }

    public WeatherApiRequestState getAqiState() {
        return aqiState;
    }

    public String getAirPollutionResponse() {
        return airPollutionResponse;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = "Min Temp: " + tempMin + mainActivity.getString(R.string.degree_centigrade);
    }

    public void setTempMax(String tempMax) {
        this.tempMax = "Max Temp: " + tempMax + mainActivity.getString(R.string.degree_centigrade);
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public void setAddress(String name, String country) {
        this.address = name + ", " + country;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUpdatedAtText(long updatedAt) {
        this.updatedAtText = "Updated at: " + new SimpleDateFormat(
                "dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                new Date(updatedAt * 1000));
    }

    public void setTemp(String temp) {
        this.temp = temp + mainActivity.getString(R.string.degree_centigrade);
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public void setAqi(int aqi) {
        this.aqi = aqi;
    }

    public void setAirPollutionResponse(int aqi) {
        if (aqi >= 0 && aqi <= 50) {
            //Good
            this.airPollutionResponse = mainActivity.getString(R.string.good);
        } else if (aqi <= 100) {
            //Moderate
            this.airPollutionResponse = mainActivity.getString(R.string.moderate);
        } else if (aqi <= 150) {
            //Unhealthy for Sensitive Groups
            this.airPollutionResponse = mainActivity.getString(R.string.unhealthy_for_sensitive_groups);
        } else if (aqi <= 200) {
            //Unhealthy
            this.airPollutionResponse = mainActivity.getString(R.string.unhealthy);
        } else if (aqi <= 300) {
            //Very Unhealthy
            this.airPollutionResponse = mainActivity.getString(R.string.very_unhealthy);
        } else {
            //Hazardous
            this.airPollutionResponse = mainActivity.getString(R.string.hazardous);
        }
    }
}

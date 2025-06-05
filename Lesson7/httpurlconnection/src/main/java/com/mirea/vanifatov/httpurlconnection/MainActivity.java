package com.mirea.vanifatov.httpurlconnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mirea.vanifatov.httpurlconnection.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(view -> {
            if (isNetworkAvailable()) {
                new DownloadGeoTask().execute("https://ipinfo.io/json");
            } else {
                Log.d(TAG, "Проверьте подключение к интернету");
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager != null ?
                connectivityManager.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnected();
    }

    private class DownloadGeoTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json = new JSONObject(result);

                binding.textViewIP.setText("IP: " + json.optString("ip"));
                binding.textViewCity.setText("Город: " + json.optString("city"));
                binding.textViewRegion.setText("Регион: " + json.optString("region"));
                binding.textViewCountry.setText("Страна: " + json.optString("country"));

                String[] coords = json.optString("loc", "0,0").split(",");
                if (coords.length == 2) {
                    new DownloadWeatherTask().execute(
                            "https://api.open-meteo.com/v1/forecast?" +
                                    "latitude=" + coords[0] +
                                    "&longitude=" + coords[1] +
                                    "&current_weather=true");
                }
            } catch (JSONException e) {
                binding.textViewWeather.setText("Ошибка при запросе погоды");
            }
        }
    }

    private class DownloadWeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Ошибка получения погоды: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json = new JSONObject(result);
                JSONObject currentWeather = json.getJSONObject("current_weather");
                double temp = currentWeather.getDouble("temperature");
                double wind = currentWeather.getDouble("windspeed");

                String weatherText = String.format(Locale.getDefault(),
                        "Текущая погода:\nТемпература: %.1f°C\nСкорость ветра: %.1f км/ч",
                        temp, wind);

                binding.textViewWeather.setText(weatherText);
            } catch (JSONException e) {
                binding.textViewWeather.setText("Ошибка получения погоды");
            }
        }
    }

    private String downloadUrl(String urlString) throws IOException {
        InputStream inputStream = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                return stringBuilder.toString();
            } else {
                throw new IOException("HTTP error code: " + connection.getResponseCode());
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
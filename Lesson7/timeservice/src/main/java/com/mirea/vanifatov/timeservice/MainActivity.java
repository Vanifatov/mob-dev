package com.mirea.vanifatov.timeservice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mirea.vanifatov.timeservice.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private final String host = "time.nist.gov";
    private final int port = 13;
    private Handler updateHandler;
    private ActivityMainBinding binding;
    private final String TAG = "CheckUp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        updateHandler = new Handler();

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTime();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void updateTime() {
        GetTimeTask timeTask = new GetTimeTask();
        timeTask.execute();
    }

    private class GetTimeTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String timeResult = "";
            try {
                Socket socket = new Socket(host, port);
                BufferedReader reader = SocketUtils.getReader(socket);
                reader.readLine();
                timeResult = reader.readLine();
                if (timeResult != null) {
                    Log.d(TAG, timeResult);
                } else {
                    Log.w(TAG, "Получено пустое значение от сервера");
                }
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "Ошибка подключения, попробуйте сменить сервер");
                timeResult = null;
            }
            return timeResult;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == null) {
                Log.e(TAG,"Ошибка: не удалось получить данные, попробуйте сменить сервер");
                return;
            }

            String[] dateTime = parseTimeString(result);
            String date = dateTime[0];
            String time = dateTime[1];
            String formattedDate = formatDate(date);

            binding.textViewDate.setText(formattedDate);
            binding.textViewTime.setText(time);
        }
    }

    private String[] parseTimeString(String timeResult) {
        if (timeResult == null || timeResult.isEmpty()) {
            return new String[]{"", ""};
        }

        String[] parts = timeResult.trim().split("\\s+");

        if (parts.length >= 3) {
            return new String[]{parts[1], parts[2]};
        }

        return new String[]{"", ""};
    }

    private String formatDate(String date) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yy-MM-dd", Locale.getDefault());
            Date parsedDate = inputFormat.parse(date);

            SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            return outputFormat.format(parsedDate);
        } catch (Exception e) {
            e.printStackTrace();
            return date;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateHandler.removeCallbacksAndMessages(null);
    }

    public static class SocketUtils {
        public static BufferedReader getReader(Socket s) throws IOException {
            return new BufferedReader(new InputStreamReader(s.getInputStream()));
        }

        public static PrintWriter getWriter(Socket s) throws IOException {
            return new PrintWriter(s.getOutputStream(), true);
        }
    }
}
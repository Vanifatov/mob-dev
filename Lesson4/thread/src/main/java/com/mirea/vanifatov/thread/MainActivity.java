package com.mirea.vanifatov.thread;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mirea.vanifatov.thread.databinding.ActivityMainBinding;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding	= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView infoTextView = findViewById(R.id.textView);
        Thread mainThread = Thread.currentThread();
        infoTextView.setText("Имя текущего потока: " + mainThread.getName());

        mainThread.setName("МОЙ НОМЕР ГРУППЫ: 01, НОМЕР ПО СПИСКУ: 2, МОЙ ЛЮБИМЫЙ ФИЛЬМ: ХАТИКО ");
        infoTextView.append("\n Новое имя потока: " + mainThread.getName());

        Log.d(MainActivity.class.getSimpleName(), "Stack: " + Arrays.toString(mainThread.getStackTrace()));
        Log.d(MainActivity.class.getSimpleName(), "Group: " + mainThread.getThreadGroup());

        final int[] counter = {0};

        binding.buttonMirea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)	{
                new	Thread(new Runnable() {
                    public void run() {
                        int	numberThread = counter[0] +1;
                        counter[0] = numberThread;
                        Log.d("ThreadProject",	String.format("Запущен поток № %d студентом группы № %s номер по " +
                                "списку № %d ",	numberThread, "БИСО-01-20", 2));

                        long endTime = System.currentTimeMillis() + 20 * 1000;
                        while (System.currentTimeMillis() < endTime) {
                            synchronized (this) {
                                try	{
                                    wait(endTime	- System.currentTimeMillis());
                                    Log.d(MainActivity.class.getSimpleName(), "Endtime: " + endTime);
                                }
                                catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            Log.d("ThreadProject",	"Выполнен поток № " + numberThread);
                        }
                    }
                }).start();
            }
        });

        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        String input1 = binding.editTextText.getText().toString();
                        String input2 = binding.editTextText2.getText().toString();

                        if (!input1.isEmpty() && !input2.isEmpty()) {
                            int num1 = Integer.parseInt(input1);
                            int num2 = Integer.parseInt(input2);

                            if (num2 != 0) {
                                int result = num1 / num2;

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.textView2.setText("Результат: " + result);
                                    }
                                });
                            }
                        }
                    }
                }).start();
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
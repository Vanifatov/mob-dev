package com.mirea.vanifatov.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mirea.vanifatov.looper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding	= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Handler mainThreadHandler = new Handler(Looper.getMainLooper())	{
            @Override
            public void handleMessage(Message msg) {
                Log.d(MainActivity.class.getSimpleName(), "Task execute. This is result: " + msg.getData().getString("result"));
                Log.d(MainActivity.class.getSimpleName(), "Task execute. This is result: " + msg.getData().getString("result2"));
            }
        };
        MyLooper myLooper = new	MyLooper(mainThreadHandler);
        myLooper.start();

        binding.editTextTextMirea.setText("Мой номер по списку №2");

        binding.buttonMirea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message	msg = Message.obtain();
                Bundle bundle =	new	Bundle();

                EditText editText1 = findViewById(R.id.editTextText2);
                EditText editText2 = findViewById(R.id.editTextText3);

                String Age = editText1.getText().toString();
                String Work = editText2.getText().toString();

                bundle.putString("KEY", "mirea");
                bundle.putString("KEY2", Age);
                bundle.putString("KEY3", Work);

                msg.setData(bundle);
                myLooper.mHandler.sendMessage(msg);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
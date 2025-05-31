package com.mirea.vanifatov.internalfilestorage;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mirea.vanifatov.internalfilestorage.databinding.ActivityMainBinding;

import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String FILE_NAME = "note.txt";
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(v -> {
            String date = binding.editTextText.getText().toString();
            String description = binding.editTextText2.getText().toString();

            if (!date.isEmpty() && !description.isEmpty()) {
                saveToFile(date, description);
            } else {
                Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void saveToFile(String date, String description) {
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            String content = "Дата: " + date + "\nОписание: " + description;
            fos.write(content.getBytes());
            fos.close();

            Toast.makeText(this, "Файл сохранён!", Toast.LENGTH_SHORT).show();
            Log.d("FileSave", "Файл записан: " + getFilesDir() + "/" + FILE_NAME);

        } catch (IOException e) {
            Toast.makeText(this, "Ошибка: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
package com.mirea.vanifatov.internalfilestorage;

import android.os.Bundle;
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
            String date = binding.editTextData.getText().toString();
            String note = binding.editTextNote.getText().toString();
            saveFile(date, note);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void saveFile(String date, String note) {
        try {
            FileOutputStream file = openFileOutput(FILE_NAME, MODE_PRIVATE);
            String content = "Дата: " + date + "\nОписание: " + note;
            file.write(content.getBytes());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
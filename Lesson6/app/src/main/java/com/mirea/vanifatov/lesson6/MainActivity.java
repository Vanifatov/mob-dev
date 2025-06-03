package com.mirea.vanifatov.lesson6;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mirea.vanifatov.lesson6.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences settings = getSharedPreferences("my_saved_file", MODE_PRIVATE);
        binding.editTextNumber.setText(settings.getString("NumberOfList", ""));
        binding.editTextGroup.setText(settings.getString("Group", ""));
        binding.editTextFilm.setText(settings.getString("LoveFilm", ""));

        binding.button.setOnClickListener(v -> {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("NumberOfList", binding.editTextNumber.getText().toString());
            editor.putString("Group", binding.editTextGroup.getText().toString());
            editor.putString("LoveFilm", binding.editTextFilm.getText().toString());
            editor.apply();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
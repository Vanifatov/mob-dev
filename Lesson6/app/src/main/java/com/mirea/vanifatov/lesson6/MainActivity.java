package com.mirea.vanifatov.lesson6;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mirea.vanifatov.lesson6.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private EditText groupNumberEditText, listNumberEditText, favoriteMovieEditText;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences settings = getSharedPreferences("MySavedFile", MODE_PRIVATE);
        binding.editTextText.setText(settings.getString("groupNumber", ""));
        binding.editTextText2.setText(settings.getString("listNumber", ""));
        binding.editTextText3.setText(settings.getString("favoriteMovie", ""));

        binding.button.setOnClickListener(v -> {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("groupNumber", binding.editTextText.getText().toString());
            editor.putString("listNumber", binding.editTextText2.getText().toString());
            editor.putString("favoriteMovie", binding.editTextText3.getText().toString());
            editor.apply();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
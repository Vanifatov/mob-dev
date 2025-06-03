package com.mirea.vanifatov.securesharedpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.mirea.vanifatov.securesharedpreferences.databinding.ActivityMainBinding;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(v -> {
            try {
                crypto_Name();
            } catch (GeneralSecurityException | IOException e) {
                throw new RuntimeException(e);
            }
            DisplayPhoto();
        });

        DisplayPhoto();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void crypto_Name() throws GeneralSecurityException, IOException {
        String poetName = binding.editTextText.getText().toString();

        SharedPreferences sharedPrefs = EncryptedSharedPreferences.create(
                "secret_poet",
                MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                getApplicationContext(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );

        sharedPrefs.edit().putString("NameRusPoet", poetName).apply();

    }
    private void DisplayPhoto() {
        try {
            SharedPreferences sharedPrefs = EncryptedSharedPreferences.create(
                    "secret_poet",
                    MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            String NameRusPoet = sharedPrefs.getString("NameRusPoet", "");

            if (!NameRusPoet.isEmpty()) {
                binding.imageView.setVisibility(View.VISIBLE);
                binding.imageView.setImageResource(R.drawable.pushkin);
            } else {
                binding.imageView.setVisibility(View.GONE);
            }
        } catch (GeneralSecurityException | IOException e) {}
    }
}
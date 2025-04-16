package com.mirea.vanifatov.favoritebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> activityResultLauncher;
    static final String BOOK_NAME_KEY = "book_name";
    static final String QUOTES_KEY = "quotes_name";
    static final String USER_MESSAGE = "MESSAGE";
    private TextView textViewUserBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewUserBook = findViewById(R.id.textViewBook);

        ActivityResultCallback<ActivityResult> callback = new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    String userBook = data.getStringExtra(BOOK_NAME_KEY);
                    String quote = data.getStringExtra(QUOTES_KEY);

                    String resultText = "Моя любимая книга: " + userBook + " и цитата " + quote;

                    textViewUserBook.setText(resultText);
                }
            }
        };

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                callback
        );

        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ShareActivity.class);
            activityResultLauncher.launch(intent);
        });
    }
}

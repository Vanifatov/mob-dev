package com.mirea.vanifatov.favoritebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ShareActivity extends AppCompatActivity {

    public EditText editTextBook;
    public EditText editTextQuote;
    static final String USER_MESSAGE = "MESSAGE";
    static final String BOOK_NAME_KEY = "book_name";
    static final String QUOTES_KEY = "quotes_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        editTextBook = findViewById(R.id.editTextBook);
        editTextQuote = findViewById(R.id.editTextQuote);

        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(v -> {
            String Book = editTextBook.getText().toString();
            String Quote = editTextQuote.getText().toString();

            Intent resultIntent = new Intent();

            resultIntent.putExtra(BOOK_NAME_KEY, Book);
            resultIntent.putExtra(QUOTES_KEY, Quote);

            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }
}

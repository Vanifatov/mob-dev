package com.mirea.vanifatov.mireaproject;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mirea.vanifatov.mireaproject.databinding.FragmentConvertFilesBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class ConvertFilesFragment extends Fragment {

    private FragmentConvertFilesBinding binding;

    public ConvertFilesFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentConvertFilesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.buttonSave.setOnClickListener(v -> saveFile());
        binding.buttonLoad.setOnClickListener(v -> loadFile());
        binding.buttonConvert.setOnClickListener(v -> convertFileToPdf());
        binding.floatingButton.setOnClickListener(v -> createRecordNote());
    }

    private void saveFile() {
        String filename = binding.textViewName.getText().toString().trim();
        String quote = binding.textViewQuote.getText().toString();


        try {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            if (!path.exists()) path.mkdirs();

            if (!filename.endsWith(".txt")) {
                filename += ".txt";
            }

            File file = new File(path, filename);

            try (FileOutputStream fos = new FileOutputStream(file, false)) {
                fos.write(quote.getBytes());
            }

            Log.d("Save_File", "Файл сохранён по пути: " + file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFile() {
        String fileName = binding.textViewName.getText().toString().trim();

        try {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            if (!fileName.endsWith(".txt")) {
                fileName += ".txt";
            }

            File file = new File(path, fileName);

            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file), "UTF-8"))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }

            binding.textViewQuote.setText(content.toString().trim());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void convertFileToPdf() {
        String fileName = binding.textViewName.getText().toString().trim();


        try {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            if (!path.exists()) path.mkdirs();

            String txt_filename = fileName.endsWith(".txt") ? fileName : fileName + ".txt";
            String pdf_filename = fileName.endsWith(".pdf") ? fileName : fileName + ".pdf";

            File txt_file = new File(path, txt_filename);
            File pdf_file = new File(path, pdf_filename);


            StringBuilder text = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(txt_file), "UTF-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    text.append(line).append("\n");
                }
            }

            PdfDocument pdfDocument = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);

            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            paint.setTextSize(12);

            int x = 10, y = 25;
            for (String line : text.toString().split("\n")) {
                canvas.drawText(line, x, y, paint);
                y += paint.descent() - paint.ascent() + 5;
            }

            pdfDocument.finishPage(page);

            try (FileOutputStream fos = new FileOutputStream(pdf_file)) {
                pdfDocument.writeTo(fos);
            }

            pdfDocument.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createRecordNote() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Создать запись");

        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        builder.setView(input);

        builder.setPositiveButton("Сохранить", (dialog, which) -> {
            String text = input.getText().toString().trim();
            binding.textViewQuote.setText(text);
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
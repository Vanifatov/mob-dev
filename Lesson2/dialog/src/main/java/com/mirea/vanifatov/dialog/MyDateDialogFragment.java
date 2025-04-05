package com.mirea.vanifatov.dialog;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

public class MyDateDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {

        int year = 1970;
        int month = 01;
        int day = 01;

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        String date = String.format("Выбранная дата: %04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                        Toast.makeText(requireContext(), date, Toast.LENGTH_LONG).show();
                        showSnackBar(view, date);
                    }
                },
                year,
                month,
                day
        );
        return datePickerDialog;
    }

    private void showSnackBar(View anchorView, String message) {
        View rootView = requireActivity().findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
    }
}
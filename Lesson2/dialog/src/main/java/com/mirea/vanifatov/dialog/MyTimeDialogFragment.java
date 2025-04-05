package com.mirea.vanifatov.dialog;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

public class MyTimeDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        int hour = 0;
        int minute = 0;

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = String.format("Выбранное время: %02d:%02d", hourOfDay, minute);
                        Toast.makeText(requireContext(), time, Toast.LENGTH_LONG).show();
                        showSnackBar(view, time);
                    }
                },
                hour,
                minute,
                true
        );

        return timePickerDialog;
    }

    private void showSnackBar(View anchorView, String message) {
        View rootView = requireActivity().findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
    }
}
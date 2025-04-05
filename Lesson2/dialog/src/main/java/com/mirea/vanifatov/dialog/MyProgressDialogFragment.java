package com.mirea.vanifatov.dialog;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

public class MyProgressDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        showSnackBar(getView(), "...");

        progressDialog.show();
        return progressDialog;
    }

    private void showSnackBar(View anchorView, String message) {
        View rootView = requireActivity().findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
    }

}
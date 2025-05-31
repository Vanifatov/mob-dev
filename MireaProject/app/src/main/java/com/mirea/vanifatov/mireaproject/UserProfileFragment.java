package com.mirea.vanifatov.mireaproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class UserProfileFragment extends Fragment {

    private static final String PREFS_NAME = "ProfilePrefs";
    private EditText etName, etAge, etEmail;
    private RadioGroup rgGender;
    private Button btnSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        etName = view.findViewById(R.id.etName);
        etAge = view.findViewById(R.id.etAge);
        etEmail = view.findViewById(R.id.etEmail);
        rgGender = view.findViewById(R.id.rgGender);
        btnSave = view.findViewById(R.id.btnSave);

        loadProfileData();

        btnSave.setOnClickListener(v -> saveProfileData());

        return view;
    }

    private void saveProfileData() {
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("name", etName.getText().toString());
        editor.putInt("age", Integer.parseInt(etAge.getText().toString()));
        editor.putString("email", etEmail.getText().toString());
        editor.putInt("gender", rgGender.getCheckedRadioButtonId());

        editor.apply();
        Toast.makeText(getContext(), "Профиль сохранён", Toast.LENGTH_SHORT).show();
    }

    private void loadProfileData() {
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, 0);

        etName.setText(prefs.getString("name", ""));
        etAge.setText(String.valueOf(prefs.getInt("age", 0)));
        etEmail.setText(prefs.getString("email", ""));

        int genderId = prefs.getInt("gender", -1);
        if (genderId != -1) {
            rgGender.check(genderId);
        }
    }

}
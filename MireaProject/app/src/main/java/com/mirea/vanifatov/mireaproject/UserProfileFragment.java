package com.mirea.vanifatov.mireaproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mirea.vanifatov.mireaproject.databinding.FragmentUserProfileBinding;

public class UserProfileFragment extends Fragment {
    private FragmentUserProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        loadProfileData();
        binding.buttonSave2.setOnClickListener(v -> saveProfileData());
        return binding.getRoot();
    }

    private void saveProfileData() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("profile_saved", 0);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("Name", binding.editTextName.getText().toString());
        editor.putInt("Age", Integer.parseInt(binding.editTextAge.getText().toString()));
        editor.putString("E-mail", binding.editTextEmail.getText().toString());
        editor.putInt("Gender", binding.radioGroupGender.getCheckedRadioButtonId());

        editor.apply();
    }

    private void loadProfileData() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("profile_saved", 0);

        binding.editTextName.setText(prefs.getString("Name", ""));
        binding.editTextAge.setText(String.valueOf(prefs.getInt("Age", 0)));
        binding.editTextEmail.setText(prefs.getString("E-mail", ""));

        int genderId = prefs.getInt("Gender", -1);
        if (genderId != -1) {
            binding.radioGroupGender.check(genderId);
        }
    }

}
package com.mirea.vanifatov.mireaproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mirea.vanifatov.mireaproject.databinding.FragmentFireBaseBinding;

import java.util.Objects;


public class FragmentFireBase extends Fragment {

    private FirebaseAuth auth;
    private FragmentFireBaseBinding binding;
    private static final String TAG = "CheckUp";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFireBaseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();

        binding.buttonSignIn.setOnClickListener(v -> signIn(
                binding.EditTextEmail.getText().toString(),
                binding.EditTextPassword.getText().toString()));

        binding.buttonCreateAcc.setOnClickListener(v -> createAccount(
                binding.EditTextEmail.getText().toString(),
                binding.EditTextPassword.getText().toString()));

        binding.buttonSignOut.setOnClickListener(v -> signOut());
        binding.buttonVerifyMail.setOnClickListener(v -> sendEmailVerification());

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.nav_web_firebase), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = auth.getCurrentUser();
        updateUI(currentUser);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = binding.EditTextEmail.getText().toString();
        if (email.isEmpty()) {
            binding.EditTextEmail.setError("Required.");
            valid = false;
        } else {
            binding.EditTextEmail.setError(null);
        }

        String password = binding.EditTextPassword.getText().toString();
        if (password.isEmpty()) {
            binding.EditTextPassword.setError("Required.");
            valid = false;
        } else {
            binding.EditTextPassword.setError(null);
        }
        return valid;
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            binding.TextViewStatus.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            binding.TextViewInfo.setText(getString(R.string.firebase_status_fmt, user.getUid()));
            binding.buttonsPostSignIn.setVisibility(View.VISIBLE);
            binding.layoutEmailPass.setVisibility(View.GONE);
            binding.buttonsEmailPass.setVisibility(View.GONE);
            binding.buttonVerifyMail.setEnabled(!user.isEmailVerified());
            navigationWeb();
        } else {
            binding.TextViewStatus.setText(R.string.signed_out);
            binding.TextViewInfo.setText(null);
            binding.buttonsPostSignIn.setVisibility(View.GONE);
            binding.layoutEmailPass.setVisibility(View.VISIBLE);
            binding.buttonsEmailPass.setVisibility(View.VISIBLE);
        }
    }

    private void navigationWeb() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.navigation_fb_web);
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = auth.getCurrentUser();
                    updateUI(user);
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(requireContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
        });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = auth.getCurrentUser();
                    updateUI(user);
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(requireContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
                if (!task.isSuccessful()) {
                    binding.TextViewStatus.setText(R.string.auth_failed);
                }
        });
    }

    private void signOut() {
        auth.signOut();
        updateUI(null);
    }

    private void sendEmailVerification() {
        binding.buttonVerifyMail.setEnabled(false);

        final FirebaseUser user = auth.getCurrentUser();
        Objects.requireNonNull(user).sendEmailVerification()
                .addOnCompleteListener(requireActivity(), task -> {
                binding.buttonVerifyMail.setEnabled(true);
                if (task.isSuccessful()) {
                    Toast.makeText(requireContext(), "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "sendEmailVerification", task.getException());
                    Toast.makeText(requireContext(), "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                }
        });
    }
}
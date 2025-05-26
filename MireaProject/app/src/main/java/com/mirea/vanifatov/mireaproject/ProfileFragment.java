package com.mirea.vanifatov.mireaproject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mirea.vanifatov.mireaproject.databinding.FragmentProfileBinding;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;


public class ProfileFragment extends Fragment {

    private static final int REQUEST_CODE_PERMISSION = 100;

    private FragmentProfileBinding binding;
    private boolean isWork = false;
    private Uri imageUri;

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        checkPermissions();

        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        binding.imageView.setImageURI(imageUri);
                    }
                });

        binding.imageView.setOnClickListener(v -> {
            if (isWork) {
                launchCamera();
            } else {
                Toast.makeText(getContext(), "Нет разрешений", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    private void checkPermissions() {
        Context context = requireContext();
        int cameraPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA);
        int storagePermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (cameraPermission == PackageManager.PERMISSION_GRANTED &&
                storagePermission == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            requestPermissions(
                    new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION
            );
        }
    }

    private void launchCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File photoFile = createImageFile();
            String authorities = requireContext().getPackageName() + ".fileprovider";
            imageUri = FileProvider.getUriForFile(requireContext(), authorities, photoFile);

            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            cameraActivityResultLauncher.launch(cameraIntent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            isWork = Arrays.stream(grantResults).allMatch(p -> p == PackageManager.PERMISSION_GRANTED);
            if (!isWork) {
                Toast.makeText(getContext(), "Нужны разрешения!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
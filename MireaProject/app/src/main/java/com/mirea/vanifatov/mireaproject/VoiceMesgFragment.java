package com.mirea.vanifatov.mireaproject;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.Manifest;

import com.mirea.vanifatov.mireaproject.databinding.FragmentVoiceMesgBinding;

import java.io.File;
import java.io.IOException;

public class VoiceMesgFragment extends Fragment {

    private static final int REQUEST_CODE_PERMISSION = 200;
    private final String TAG = VoiceMesgFragment.class.getSimpleName();
    private boolean isWork;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private boolean isStartRecording = true;
    private boolean isStartPlaying = true;
    private String recordFilePath;
    private FragmentVoiceMesgBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVoiceMesgBinding.inflate(inflater, container, false);
        binding.play.setEnabled(false);
        recordFilePath = (new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                "/audiorecordtest.3gp")).getAbsolutePath();
        checkPermissions();

        binding.record.setOnClickListener(v -> {
            if (isStartRecording) {
                binding.record.setText("Остановить запись");
                binding.play.setEnabled(false);
                startRecording();
            } else {
                binding.record.setText("Начать запись");
                binding.play.setEnabled(true);
                stopRecording();
            }
            isStartRecording = !isStartRecording;
        });

        binding.play.setOnClickListener(v -> {
            if (isStartPlaying) {
                binding.play.setText("Остановить проигрывание");
                binding.record.setEnabled(false);
                startPlaying();
            } else {
                binding.play.setText("Начать проигрывание");
                binding.record.setEnabled(true);
                stopPlaying();
            }
            isStartPlaying = !isStartPlaying;
        });

        return binding.getRoot();
    }

    private void checkPermissions() {
        Context context = requireContext();
        int audioRecordPermissionStatus = ContextCompat.checkSelfPermission(context,
                Manifest.permission.RECORD_AUDIO);
        int storagePermissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.
                WRITE_EXTERNAL_STORAGE);

        if (audioRecordPermissionStatus == PackageManager.PERMISSION_GRANTED &&
                storagePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            requestPermissions(
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            isWork = grantResults.length > 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED;
            if (!isWork) {
                Toast.makeText(requireContext(), "Требуются все разрешения", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(recordFilePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }
        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(recordFilePath);
            player.prepare();
            player.start();

            player.setOnCompletionListener(mp -> {
                stopPlaying();
                binding.play.setText("Начать проигрывание");
                binding.record.setEnabled(true);
                isStartPlaying = true;
            });
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }
    }
    
    private void stopPlaying() {
        player.release();
        player = null;
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
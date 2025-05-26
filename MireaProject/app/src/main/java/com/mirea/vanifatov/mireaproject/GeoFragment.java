package com.mirea.vanifatov.mireaproject;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GeoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GeoFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;

    private float[] gravity;
    private float[] geomagnetic;

    private float azimuth = 0f;

    private Button directionButton;
    private TextView directionText;

    public GeoFragment() {
        // Required empty public constructor
    }

    public static GeoFragment newInstance(String param1, String param2) {
        GeoFragment fragment = new GeoFragment();
        Bundle args = new Bundle();
        args.putString("ARG_PARAM1", param1);
        args.putString("ARG_PARAM2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_geo, container, false);

        directionButton = view.findViewById(R.id.button);
        directionText = view.findViewById(R.id.textView);

        directionButton.setOnClickListener(v -> {
            String direction = getDirectionFromAzimuth(azimuth);
            directionText.setText("Направление: " + direction);
        });

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            gravity = event.values.clone();
        else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            geomagnetic = event.values.clone();

        if (gravity != null && geomagnetic != null) {
            float[] R = new float[9];
            float[] I = new float[9];
            if (SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)) {
                float[] orientation = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimuth = (float) Math.toDegrees(orientation[0]);
                if (azimuth < 0)
                    azimuth += 360;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private String getDirectionFromAzimuth(float azimuth) {
        if (azimuth >= 337.5 || azimuth < 22.5)
            return "Север";
        else if (azimuth >= 22.5 && azimuth < 67.5)
            return "Северо-восток";
        else if (azimuth >= 67.5 && azimuth < 112.5)
            return "Восток";
        else if (azimuth >= 112.5 && azimuth < 157.5)
            return "Юго-восток";
        else if (azimuth >= 157.5 && azimuth < 202.5)
            return "Юг";
        else if (azimuth >= 202.5 && azimuth < 247.5)
            return "Юго-запад";
        else if (azimuth >= 247.5 && azimuth < 292.5)
            return "Запад";
        else
            return "Северо-запад";
    }
}

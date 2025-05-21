package com.mirea.vanifatov.mireaproject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WorkerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkerFragment newInstance(String param1, String param2) {
        WorkerFragment fragment = new WorkerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worker, container, false);

        Button button = view.findViewById(R.id.button);
        Button button2 = view.findViewById(R.id.button2);

        TextView textView = view.findViewById(R.id.textView3);
        TextView textView2 = view.findViewById(R.id.textView4);

        button.setOnClickListener(v -> {
            if (isInternetAvailable()) {
                textView.setText("Интернет есть!");
                startMyWorker();
            } else {
                textView.setText("Интернета нет.");
            }
        });

        button2.setOnClickListener(v -> {
            if (isDeviceCharging()) {
                textView2.setText("Зарядка есть!");
                startMyWorker();
            } else {
                textView2.setText("Зарядки нет.");
            }
        });
        return view;
    }

    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private boolean isDeviceCharging() {
        BatteryManager batteryManager = (BatteryManager) getActivity().getSystemService(Context.BATTERY_SERVICE);
        return batteryManager.isCharging();
    }

    private void startMyWorker() {
        WorkRequest myWorkRequest = new OneTimeWorkRequest.Builder(MyWorker.class).build();
        WorkManager.getInstance(requireContext()).enqueue(myWorkRequest);
    }
}
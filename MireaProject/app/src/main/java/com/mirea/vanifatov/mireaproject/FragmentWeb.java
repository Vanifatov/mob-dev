package com.mirea.vanifatov.mireaproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.auth.FirebaseAuth;
import com.mirea.vanifatov.mireaproject.databinding.FragmentWebBinding;

public class FragmentWeb extends Fragment {

    private FirebaseAuth auth;
    private FragmentWebBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWebBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();

        WebView webView = binding.webView;
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://priem.mirea.ru/");
    }
}
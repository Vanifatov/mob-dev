package com.mirea.vanifatov.cryptoloader;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MyLoader extends AsyncTaskLoader<String> {

    public static final String ARG_WORD = "arg_word";
    private final Bundle args;

    public MyLoader(@NonNull Context context, @Nullable Bundle args) {
        super(context);
        this.args = args;
    }

    @Nullable
    @Override
    public String loadInBackground() {
        if (args != null) {
            byte[] cryptText = args.getByteArray(ARG_WORD);
            byte[] key = args.getByteArray("key");

            SecretKey originalKey = new SecretKeySpec(key, 0, key.length, "AES");
            return decryptMsg(cryptText, originalKey);
        }
        return null;
    }

    public static String decryptMsg(byte[] cipherText, SecretKey secret) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secret);
            return new String(cipher.doFinal(cipherText));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onStartLoading() {
        if (args != null) {
            forceLoad();
        }
    }
}

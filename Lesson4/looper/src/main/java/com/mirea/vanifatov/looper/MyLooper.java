package com.mirea.vanifatov.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class MyLooper extends Thread {
    public Handler mHandler;
    private Handler mainHandler;
    public MyLooper(Handler	mainThreadHandler) {
        mainHandler	= mainThreadHandler;
    }
    public void run() {
        Log.d("MyLooper", "run");
        Looper.prepare();
        mHandler = new Handler(Looper.myLooper()) {
            public void	handleMessage(Message msg) {
                String data = msg.getData().getString("KEY");
                Log.d("MyLooper get message: ", data);

                String data2 = msg.getData().getString("KEY2");
                Log.d("MyLooper2 get message: ", data2);

                String data3 = msg.getData().getString("KEY3");
                Log.d("MyLooper3 get message: ", data3);

                int delay = 0;
                try {
                    delay = Integer.parseInt(data2);
                } catch (NumberFormatException e) {
                    Log.e("MyLooper", "Invalid number format for delay: " + data2);
                }

                try {
                    if (delay > 0) {
                        TimeUnit.SECONDS.sleep(delay);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int	count = data.length();
                Message	message = new Message();
                Bundle bundle = new	Bundle();

                bundle.putString("result", String.format("The number of letters in the word %s is %d", data, count));
                bundle.putString("result2", String.format("Мне %s года и я работаю на должности %s", data2, data3));

                message.setData(bundle);
                mainHandler.sendMessage(message);
            }
        };
        Looper.loop();
    }
}
package com.liang.ibinderpooltest;

import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG  = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
            }
        }).start();

    }

    private void doWork() {
        final StringBuffer stringBuffer = new StringBuffer();
        BinderPool binderPool =  BinderPool.getInstance(this);
        IBinder countBinder = binderPool.queryBinder(BinderPool.BINDER_CODEZ_COUNT);
        ICount count = CountImpl.asInterface(countBinder);
        try {
            stringBuffer.append("sum(2,5) = "+count.add(2,5)+"\n\n");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        IBinder maxBinder =binderPool.queryBinder(BinderPool.BINDER_CODEZ_MAX);
        IMax max = MaxImpl.asInterface(maxBinder);
        try {
            stringBuffer.append("max(2,5) = "+max.max(2,5));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                ((TextView)findViewById(R.id.mtext)).setText(stringBuffer.toString());
            }
        });
    }
}

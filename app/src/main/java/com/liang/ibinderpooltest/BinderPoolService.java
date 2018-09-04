package com.liang.ibinderpooltest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BinderPoolService extends Service {

    private static final String  TAG = BinderPoolService.class.getName();

    public BinderPoolService() {
    }

    private Binder mBinder = new BinderPool.BinderPoolImpl();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

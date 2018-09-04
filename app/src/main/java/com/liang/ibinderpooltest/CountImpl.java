package com.liang.ibinderpooltest;

import android.os.RemoteException;

public class CountImpl extends ICount.Stub {
    private static final String TAG  = CountImpl.class.getName();

    @Override
    public int add(int a, int b) throws RemoteException {
        return a+b;
    }
}

package com.liang.ibinderpooltest;

import android.os.RemoteException;
import android.util.Log;

public class MaxImpl extends IMax.Stub{
    private static final String TAG  = MaxImpl.class.getName();
    @Override
    public int max(int a, int b) throws RemoteException {
        return Math.max(a,b);
    }
}

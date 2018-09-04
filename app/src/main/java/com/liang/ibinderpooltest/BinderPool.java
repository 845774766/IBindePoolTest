package com.liang.ibinderpooltest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.concurrent.CountDownLatch;

public class BinderPool {
    private static final String TAG  = BinderPool.class.getName();
    public static final int BINDER_CODEZ_COUNT = 1;
    public static final int BINDER_CODEZ_MAX = 2;


    private static volatile BinderPool mInstances ;
    private Context context;
    private IBinderPool mIBinderPool;
    private CountDownLatch mCountDownLatch;

    private BinderPool(Context context){
        this.context = context.getApplicationContext();
        connectBinderPoolService();
    }

    public static BinderPool getInstance(Context context){
        if (mInstances ==null){
            synchronized (BinderPool.class){
                if (mInstances==null){
                    mInstances = new BinderPool(context);
                }
            }
        }
        return mInstances;
    }

    private synchronized void connectBinderPoolService(){
        mCountDownLatch = new CountDownLatch(1);
        Intent service = new Intent(context,BinderPoolService.class);
        context.bindService(service,mBinderPoolConnection,Context.BIND_AUTO_CREATE);
        try {
            mCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private ServiceConnection mBinderPoolConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                mIBinderPool.asBinder().linkToDeath(mBinderDeathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private IBinder.DeathRecipient mBinderDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.d(TAG, "binderDied: ");
            mIBinderPool.asBinder().unlinkToDeath(mBinderDeathRecipient,0);
            mIBinderPool = null;
            connectBinderPoolService();
        }
    };

    public static class BinderPoolImpl extends IBinderPool.Stub{

        public BinderPoolImpl() {
            super();
        }

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            switch (binderCode){
                case BINDER_CODEZ_COUNT:
                    binder = new CountImpl();
                    break;
                case BINDER_CODEZ_MAX:
                    binder = new MaxImpl();
                    break;
                default:
                    break;
            }
            return binder;
        }
    }

    /**
     * 根据binderCode通过aidl方法查询到Binder
     * @param binderCode
     * @return
     */
    public IBinder queryBinder(int binderCode){
        IBinder binder = null;
        try{
            if (mIBinderPool!=null){
                binder = mIBinderPool.queryBinder(binderCode);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return binder;
    }

}

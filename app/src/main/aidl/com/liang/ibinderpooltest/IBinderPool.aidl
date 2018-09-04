// IBinderPool.aidl
package com.liang.ibinderpooltest;

interface IBinderPool {
    /**
    *  binder连接池关键方法
    *   查询并返回对应binder
    */
    IBinder queryBinder(int binderCode);
}

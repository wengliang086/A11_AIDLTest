package com.my.aidl.test;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MyService extends Service {

    private static final String TAG = "aidl_server: ";

    private IMyAidlInterface.Stub mBinder = new IMyAidlInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
            Log.d(TAG, "basicTypes: Invoked");
        }

        @Override
        public int add(int num1, int num2) throws RemoteException {
            Log.d(TAG, "add() called with: " + "num1 = [" + num1 + "], num2 = [" + num2 + "]");
            return num1 + num2;
        }
    };

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: Invoked");
        return mBinder;
    }
}

package com.my.aidl.client;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.my.aidl.test.IMyAidlInterface;

import java.util.List;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Aidl_Client:";

    private EditText num1;
    private EditText num2;
    private TextView result;
    private Button add;

    private IMyAidlInterface service;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected: ");
            service = IMyAidlInterface.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "onServiceDisconnected: ");
            service = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onBindService();
        initView();

        isServiceWork(this, "");
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (myList.size() <= 0) {
            return false;
        }
        Log.e("Total=", myList.size() + "");
        TreeMap<String, String> map = new TreeMap<>();
        for (int i = 0; i < myList.size(); i++) {
            ActivityManager.RunningServiceInfo rsi = myList.get(i);
            String s = "className=" + rsi.service.getClassName() + ",pid=" + rsi.pid + ",process=" + rsi.process + ",clientCount=" + rsi.clientCount;
            map.put(s, s);
//            Log.e("ServiceName:", mName);
            if (rsi.service.getClassName().equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        for (String m : map.values()) {
            Log.e("ServiceName:", m);
        }
        return isWork;
    }

    private void onBindService() {
        Intent intent = new Intent();
//        intent.setPackage("com.my.aidl.test");
        intent.setComponent(new ComponentName("com.my.aidl.test", "com.my.aidl.test.MyService"));
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    private void initView() {
        num1 = (EditText) findViewById(R.id.id_num1);
        num2 = (EditText) findViewById(R.id.id_num2);
        result = (TextView) findViewById(R.id.id_result);
        add = (Button) findViewById(R.id.id_btn);
        add.setOnClickListener(this);
        findViewById(R.id.id_running_service).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.id_running_service) {
            startActivity(new Intent(this, RunningServiceActivity.class));
        } else {
            int n1 = Integer.parseInt(num1.getText().toString());
            int n2 = Integer.parseInt(num2.getText().toString());
//        result.setText(n1 + n2 + "");

            result.setTextColor(Color.BLUE);
            int resultNum = 0;
            try {
                resultNum = service.add(n1, n2);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            result.setText(resultNum + "");
        }
    }
}

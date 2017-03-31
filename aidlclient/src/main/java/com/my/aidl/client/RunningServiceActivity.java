package com.my.aidl.client;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/31.
 */

public class RunningServiceActivity extends AppCompatActivity {

    private ListView listView;
    private List<ActivityManager.RunningServiceInfo> list = new ArrayList<>();
    private BaseAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_service);

        findViewById(R.id.id_btn_hoolai).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData("hoolai");
                adapter.notifyDataSetChanged();
            }
        });

        initData(null);

        listView = (ListView) findViewById(R.id.id_list_view);
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder;
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = LayoutInflater.from(RunningServiceActivity.this).inflate(R.layout.item_running_service, null);
                    holder.id = (TextView) convertView.findViewById(R.id.tv_pid);
                    holder.name = (TextView) convertView.findViewById(R.id.tv_name);
                    holder.count = (TextView) convertView.findViewById(R.id.tv_client_count);
                    holder.Label = (TextView) convertView.findViewById(R.id.tv_client_lable);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                ActivityManager.RunningServiceInfo rsi = list.get(position);
//                String s = "className=" + rsi.service.getClassName() + ",pid=" + rsi.pid + ",process=" + rsi.process + ",clientCount=" + rsi.clientCount;
                holder.id.setText(rsi.pid + "");
                holder.name.setText(rsi.service.getShortClassName());
                holder.count.setText(rsi.clientCount + "");
                holder.Label.setText(rsi.process);
                return convertView;
            }

            class ViewHolder {
                TextView id;
                TextView name;
                TextView count;
                TextView Label;
            }
        };
        listView.setAdapter(adapter);
    }

    private void initData(String condition) {
        if (list != null) {
            list.clear();
        }
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (TextUtils.isEmpty(condition)) {
            list.addAll(services);
        } else {
            for (ActivityManager.RunningServiceInfo info : services) {
                if (info.service.getClassName().contains(condition)) {
                    list.add(info);
                }
            }
        }
    }
}

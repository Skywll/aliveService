package com.gogogo.aliveservice;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class MessageService extends Service {
    private static final String TAG = "MessageService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return new ProcessConnection.Stub(){};
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
//        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
        unbindService(mServiceConnection);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                // FIXME: 2017/5/19 逻辑处理
                while (true) {
                    SystemClock.sleep(5000);
                    Log.d(TAG, "run: 接收消息");
                }
            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        startForeground(1, new Notification());//优先级
        bindService(new Intent(this, GuardService.class), mServiceConnection, Context.BIND_IMPORTANT);//绑定
        return START_STICKY;
    }
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接上
            Toast.makeText(MessageService.this, "Message建立连接",  Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开
            Toast.makeText(MessageService.this, "Message断开连接", Toast.LENGTH_SHORT).show();
            startService(new Intent(MessageService.this, GuardService.class));
            bindService(new Intent(MessageService.this, GuardService.class), mServiceConnection, Context.BIND_IMPORTANT);//绑定

        }
    };
}

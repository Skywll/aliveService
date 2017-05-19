package com.gogogo.aliveservice;


import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * 工作服务
 */
public class MessageService extends Service {

    private static final String TAG = "MessageService";
    private MessageServiceConnection mServiceConnection;
    private MessageBind mMessageBind;
    private final int MessageServiceId = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // FIXME: 2017/5/11 (不死服务)写需要的服务逻辑就可以了
                while (true) {
                    Log.e(TAG, "等待接收消息");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        if (mServiceConnection == null) {
            mServiceConnection = new MessageServiceConnection();
        }
        if (mMessageBind == null) {
            mMessageBind = new MessageBind();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(MessageServiceId, new Notification());//提高优先级
        MessageService.this.bindService(new Intent(MessageService.this, GuardService.class), mServiceConnection, Context.BIND_IMPORTANT);
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessageBind;
    }

    private class MessageBind extends ProcessConnection.Stub {

    }

    private class MessageServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 建立连接
            Toast.makeText(MessageService.this, "建立连接Message", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 断开连接
            Toast.makeText(MessageService.this, "断开连接Message", Toast.LENGTH_LONG).show();
            Intent guardIntent = new Intent(MessageService.this, GuardService.class);
            // 发现断开我就从新启动和绑定
            startService(guardIntent);
            MessageService.this.bindService(guardIntent, mServiceConnection, Context.BIND_IMPORTANT);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        unbindService(mServiceConnection);
    }
}
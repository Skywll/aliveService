package com.gogogo.aliveservice;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * 守护进程 守护服务
 */
public class GuardService extends Service {

    private final int GuardId = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(GuardId, new Notification());//提高优先级
        //绑定链接
        bindService(new Intent(this, MessageService.class), mServiceConnection, Context.BIND_IMPORTANT);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ProcessConnection.Stub() {
        };//进程间通信
    }


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 建立连接
            Toast.makeText(GuardService.this, "建立连接GuardService", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {//断开连接,重新启动绑定
            Toast.makeText(GuardService.this, "断开连接GuardService", Toast.LENGTH_LONG).show();
            Intent messageIntent = new Intent(GuardService.this, MessageService.class);
            // 发现断开我就从新启动和绑定
            startService(messageIntent);
            GuardService.this.bindService(messageIntent, mServiceConnection, Context.BIND_IMPORTANT);
        }
    };
}

package com.gogogo.aliveservice;

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
 * 守护进程 守护服务
 */
public class GuardService extends Service {
    private static final String TAG = "GuardService";
    private final int GuardId = 1;
    private GuardBind mGuardBind;
    private MessageServiceConnection mServiceConnection;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        //startForeground(GuardId, new Notification());//提高优先级不能写写了用户可能在notifycation中将其关闭掉
        //绑定链接
        bindService(new Intent(GuardService.this, MessageService.class), mServiceConnection, Context.BIND_IMPORTANT);
        Intent messageIntent = new Intent(GuardService.this, MessageService.class);
        // 发现断开我就从新启动和绑定
        startService(messageIntent);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return mGuardBind ;

    }
    private class GuardBind extends ProcessConnection.Stub {

    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        if (mServiceConnection == null) {
            mServiceConnection = new GuardService.MessageServiceConnection();
        }
        if (mGuardBind == null){
            mGuardBind = new GuardService.GuardBind();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

        private class MessageServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: ");
            // 建立连接
            Toast.makeText(GuardService.this, "建立连接GuardService", Toast.LENGTH_LONG).show();
            Intent messageIntent = new Intent(GuardService.this, MessageService.class);
            startService(messageIntent);//开启工作service
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {//断开连接,重新启动绑定
            Log.d(TAG, "onServiceDisconnected: ");
            Toast.makeText(GuardService.this, "断开连接GuardService", Toast.LENGTH_LONG).show();
            Intent messageIntent = new Intent(GuardService.this, MessageService.class);
            // 发现断开我就从新启动和绑定
            startService(messageIntent);
            GuardService.this.bindService(messageIntent, mServiceConnection, Context.BIND_IMPORTANT);
        }
    };
}

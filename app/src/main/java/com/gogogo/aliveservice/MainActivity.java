package com.gogogo.aliveservice;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // FIXME: 2017/5/11 (不死服务)第一步开启工作和守护服务服务,
        // FIXME: 2017/5/11 (不死服务)第二步进入清单文件AndroidManifest注意
        // FIXME: 2017/5/11 (不死服务)第三步写aidl文件,注意创建了之后需要build工程
        // FIXME: 2017/5/11 (不死服务)只要写MessageService的工作,其他直接复制
        startService(new Intent(this, MessageService.class));//开启工作服务,所有的工作坊这里面
        startService(new Intent(this, GuardService.class));//开启守护进程和服务
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0以上的一个新的服务是监测电量的,用来开启这个jobService每隔一段时间就去监测服务
            //必须大于5.0
            startService(new Intent(this, JobAwakenService.class));
        }


    }
}

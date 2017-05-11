package com.gogogo.aliveservice;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.List;

/**
 * Created by Darren on 2017/3/25.
 * Email: 240336124@qq.com
 * Description:
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JobAwakenService extends JobService {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //开启轮循
        JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(this, JobAwakenService.class));
        builder.setPeriodic(500);//间隔500ms就执行一次
        JobInfo jobInfo = builder.build();
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e("TAG", "onStartJob");//一直轮顺环,如果被杀死就开启这个服务
        // boolean isGuardAlive = isServiceWork(this,GuardService.class.getName());
        boolean isMessageAlive = isServiceWork(this, MessageService.class.getName());
        if (!isMessageAlive) {//如果这个服务死了就开启
            // startService(new Intent(this,GuardService.class));
            startService(new Intent(this, MessageService.class));
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
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
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }
}
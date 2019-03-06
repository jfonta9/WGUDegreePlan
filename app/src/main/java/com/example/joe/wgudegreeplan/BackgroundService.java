package com.example.joe.wgudegreeplan;

import android.annotation.TargetApi;
import android.app.*;
import android.content.*;
import android.database.Cursor;
import android.os.*;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BackgroundService extends Service {


    private Thread backgroundThread;
    private boolean isRunning;
    private Context context;
    static int notificationId;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Runnable myTask = new Runnable() {
        @TargetApi(Build.VERSION_CODES.O)
        public void run() {
            System.out.println("THE BACKGROUND SERVICE IS RUNNING!!!");
            stopSelf();
        }
    };

    @Override
    public void onCreate() {
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);
    }


    @Override
    public void onDestroy() {
        this.isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
        }
        return START_STICKY;
    }

}

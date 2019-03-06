package com.example.joe.wgudegreeplan;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String courseAlarmFile = "courseAlarms";
    static int notificationId;
    String channel_id = "test";
    private String courseStart;
    String GROUP_KEY_COURSE_ALERT = "COURSE_ALERT";
    public static final String alarmFile = "alarmFile";
    public static final String nextAlarmField = "nextAlarmId";

    @Override
    public void onReceive(Context context, Intent intent) {

        createNotificationChannel(context,channel_id);
        String type = intent.getStringExtra( "type" );
        String name = intent.getStringExtra( "name" );

        if(type.equals( "start" )) {
            Notification nc = new NotificationCompat.Builder( context, channel_id )
                    .setSmallIcon( R.drawable.ic_action_alarm )
                    .setContentTitle( "Course Starting!" )
                    .setContentText( name + " starts today!" ).build();
            NotificationManager nm = (NotificationManager) context.getSystemService( Context.NOTIFICATION_SERVICE );
            nm.notify( notificationId++, nc );
        }
        if(type.equals( "end" )){
            Notification nc = new NotificationCompat.Builder( context, channel_id )
                    .setSmallIcon( R.drawable.ic_action_alarm )
                    .setContentTitle( "Course Ending!" )
                    .setContentText( name + " ends today!" ).build();
            NotificationManager nm = (NotificationManager) context.getSystemService( Context.NOTIFICATION_SERVICE );
            nm.notify( notificationId++, nc );
        }
        if(type.equals("assessment")){
            Notification nc = new NotificationCompat.Builder( context, channel_id )
                    .setSmallIcon( R.drawable.ic_action_alarm )
                    .setContentTitle( "Assessment Due!" )
                    .setContentText( name + " is due today!" ).build();
            NotificationManager nm = (NotificationManager) context.getSystemService( Context.NOTIFICATION_SERVICE );
            nm.notify( notificationId++, nc );
        }
    }


    private void createNotificationChannel(Context context, String channelId) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Alert";
            String description = "Alert";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

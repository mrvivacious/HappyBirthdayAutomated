// HappyBirthday
// MyAlarmService.java
//
// This file carries out the daily check-and-SMS function of the app
//
// The code was taken from this tutorial:
// http://android-er.blogspot.com/2010/10/simple-example-of-alarm-service-using.html
//  with some help from
// http://android-er.blogspot.com/2010/10/schedule-repeating-alarm.html
//
// @author Vivek Bhookya

package us.mrvivacio.happybirthday;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;

public class MyAlarmService extends Service {
    @Override
    public void onCreate() {
        Log.d("MyAlarmService", "onCreate: createdddddd");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("MyAlarmService", "onBind: boundeddddd");
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyAlarmService", "onDestroy: destroyeddddd");

        // Notify myself that the app was either closed (intentionally) or killed by Android (sad)
        String msg = "RIP HappyBirthday 2018 - ";
        int year = Calendar.getInstance().get(Calendar.YEAR);
        Utilities.sendSMS(EnvironmentVars.myNumber, msg + year);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d("MyAlarmService", "onStart: wuzzup");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MyAlarmService", "startService: We have entered our alarm service method");

        // I don't know if this code is written appropriately with respect to Android principles
        // It works, but I still can't get a service to set 24 hour alarms...great pull request for any1 :D

        // "WHEN TO RUN OUR ALARM" SET UP
        // Create the intent connecting this class with the alarm service class
        Intent myIntent = new Intent(this, MyAlarmService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);

        // Create an alarmManager and a calendar instances
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();

        // Set the alarm for (24 hours plus the current time) in the future
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.HOUR, 24);

        // Setting alarm
        // GOTTA USE RTC/_WAKEUP with the current implementation
         alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 6*1000, pendingIntent);
//        Log.d("MyAlarmService, "startService: ALARM SETTTTT");

        // "WHAT TO DO WHEN OUR ALARM RUNS" SET UP
        // EVERYTHING BELOW HANDLES THE CHECKING OF DATE AND RECIPIENT LIST SMS
        Utilities.checkDate();

        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("MyAlarmService", "onUnbind: uuunnnnnnbounded");

        return super.onUnbind(intent);
    }
}

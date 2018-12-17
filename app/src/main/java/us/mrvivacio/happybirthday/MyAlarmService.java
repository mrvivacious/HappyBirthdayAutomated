// HappyBirthday
// MyAlarmService.java
// This file carries out the daily check-and-SMS function of the app
//
// The code was taken from this tutorial:
// http://android-er.blogspot.com/2010/10/simple-example-of-alarm-service-using.html
//  with some help from
// http://android-er.blogspot.com/2010/10/schedule-repeating-alarm.html
//
// @author Vivek Bhookya

package us.mrvivacio.happybirthday;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyAlarmService extends Service {
    @Override
    public void onCreate() {
        Log.d("fuck u", "onCreate: createdddddd");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("fuck u", "onBind: boundeddddd");
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("fuck u", "onDestroy: destroyeddddd");

        String msg = "RIP HappyBirthday 2018 - ";
        int year = Calendar.getInstance().get(Calendar.YEAR);
        sendSMS("3096602340", msg + year);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d("fuck u", "onStart: wuzzup");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("fuck u", "startService: We have entered our alarm service method");

        // Create the intent connecting this class with the alarm service class
        Intent myIntent = new Intent(this, MyAlarmService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);

        Log.d("fuck u", "startService: attempting to set this alarm lmao");

        // Create an alarmManager and a calendar instances
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();

        // Set the alarm for 6 seconds plus the current time in the future
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 6);

        // Setting alarm
         alarmManager.set(AlarmManager.ELAPSED_REALTIME, calendar.getTimeInMillis(), pendingIntent);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 6*1000, pendingIntent);
//        Log.d("fuck u", "startService: ALARM SETTTTT");

        // Thank you, https://www.mkyong.com/java/java-how-to-get-current-date-time-date-and-calender/
        DateFormat dateFormat;
        Date date;
        String msg;

        Log.d("fuck u", "onStart: We tryna send this text dawg");

        msg = "nyeh\n";

        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        date = new Date();
        msg += dateFormat.format(date);

        sendSMS("3096602340", msg);
        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("fuck u", "onUnbind: uuunnnnnnbounded");

        return super.onUnbind(intent);
    }

    // Function sendSMS
    // Sends the msg to the phoneNo
    // Thank you, https://stackoverflow.com/questions/26311243/sending-sms-programmatically-without-opening-message-app
    private void sendSMS(String phoneNo, String msg) {
        // Request SMS permission if not yet authorized
        // Thank you, https://stackoverflow.com/questions/32635704/android-permission-doesnt-work-even-if-i-have-declared-it
        int PERMISSION_REQUEST_CODE = 1;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "ENABLE PERMISSIONS ~", Toast.LENGTH_LONG).show();
                // Log.d("fuck u", "permission denied to SEND_SMS - requesting it");
                // String[] permissions = {Manifest.permission.SEND_SMS};

                // requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }
        }

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);

        } catch (Exception ex) {
            Toast.makeText(this, "Error with sendSMS: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }
}

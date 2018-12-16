package us.mrvivacio.happybirthday;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Pattern;

import static us.mrvivacio.happybirthday.Utilities.reformat;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button add = findViewById(R.id.b_Add);
        Button view = findViewById(R.id.b_View);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Thank you, https://developer.android.com/reference/android/widget/EditText
                String name = ((TextView) findViewById(R.id.et_Name)).getText().toString();
                String number = ((TextView) findViewById(R.id.et_PhoneNum)).getText().toString();
                String month = ((TextView) findViewById(R.id.et_Month)).getText().toString();
                String day = ((TextView) findViewById(R.id.et_Day)).getText().toString();

                // Screen for valid inputs
                if (name.length() < 1) {
                    myToast("Please add a name");
                    return;
                } else if (number.length() < 1) {
                    myToast("Please add a phone number");
                    return;
                } else if (month.length() < 1 || month.length() > 2) {
                    myToast("Please add a valid month");
                    return;
                } else if (day.length() < 1 || day.length() > 2) {
                    myToast("Please add a valid day");
                    return;
                }

                // We have valid input, so add this person to sharedPref
                else {
//                    myToast("Good work: " + name + " -- " + number);
                    String date = reformat(month, day);
//                    save(name, number, date);
                    saveToFile(name, number, date);
                }
            }
        });

        // View the specified date's birthdays via Toast
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String month = ((TextView) findViewById(R.id.et_Month)).getText().toString();
                String day = ((TextView) findViewById(R.id.et_Day)).getText().toString();
                String date = reformat(month, day);

                show(date);
            }
        });
    }

    private void show(String date) {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        String bdays = sharedPref.getString(date, "rip");

        if (bdays == "rip") {
            myToast(bdays);
        }

        // Else we got sumtin
        String toShow = "";
        // Thank you, https://stackoverflow.com/questions/14134558/list-of-all-special-characters-that-need-to-be-escaped-in-a-regex
        String delimit = Pattern.quote("$$");

        String[] newBdays = bdays.split(delimit);
        Log.d("fuck u", newBdays[0]);

        for (int i = 0; i < newBdays.length; i++) {
            String[] parameters = newBdays[i].split("/");
            String name = parameters[0];
            String number = parameters[1];

//            sendSMS(number, "Did u get this text if you did the android code works lmao");
            Log.d("fuck u", "Sending to " + number);

            toShow += name + ": " + number + ", ";
        }

        toShow = toShow.substring(0,toShow.length() - 2);
        myToast(toShow);

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

                Log.d("fuck u", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);

            }
        }

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            myToast("Message sent ~");

        } catch (Exception ex) {
            myToast(ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void saveToFile(String name, String number, String date) {
        // Request storage permissions if not yet authorized
        // Thank you, https://stackoverflow.com/questions/32635704/android-permission-doesnt-work-even-if-i-have-declared-it
        int PERMISSION_REQUEST_CODE = 1;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("fuck u", "permission denied to WRTIE EXTERNAL STORAGE - requesting it");
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);

            }
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("fuck u", "permission denied to READ EXTERNAL STORAGE - requesting it");
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);

            }
        }

        // Thank you, https://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder,
        // https://stackoverflow.com/questions/1239026/how-to-create-a-file-in-android
        try {
            // get the path to storage
            File path = Environment.getExternalStorageDirectory();

             // to this path add a new directory path
            File dir = new File(path.getAbsolutePath() + "/HappyBirthday/");
            Log.d("fuck u", "path = " + dir);

            // create this directory if not already created
            dir.mkdir();

            // create the file in which we will write the contents
            File file = new File(dir, date + ".txt");
            Log.d("fuck u", "two");


            FileOutputStream os = new FileOutputStream(file);
            String data = name + "/" + number + "\n";

            os.write(data.getBytes());
            Log.d("fuck u", "three");

            os.close();

            myToast("Saved");

        } catch (IOException ioe) {
            ioe.printStackTrace();
            myToast("Uh oh error: " + ioe);
        }

    }

    // Function save
    // Saves contact to shared preferences
    private void save(String name, String number, String date) {
        // Thank you, https://developer.android.com/training/data-storage/shared-preferences#java
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Build the contact schema
        // Get the existing list for this recipient's birth date
        // Add this recipient to that list
        // Save the updated list

        // Build the contact schema
        // Schema: Key: Date, Value: List of name:number pairs
        String contact = name + "/" + number;

        // Attempt to get the list for today
        String birthdays = sharedPref.getString(date, null);

        // No existing list? Create a new one
        if (birthdays == null) {
            birthdays = "";
        }

        // Add recipient
        // We are using delimiters because I was having some weird issues with StringSet
        birthdays += contact + "$$";

        // Save the updated list
        editor.putString(date, birthdays);

        editor.apply();
        myToast("Saved!");
    }

    // Function myToast
    // Quicker and more readable way to make Toasts without all the code
    private void myToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }
}

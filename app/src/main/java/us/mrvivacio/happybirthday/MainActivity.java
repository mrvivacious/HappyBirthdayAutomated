// HappyBirthday
// MainActivity.java
// This file handles the functionality of HappyBirthday and allows the user to save and view birthdays
//
// Noteworthy:
// : SMS, READ/WRITE storage permissions
// : File read/write operations
//
// @author Vivek Bhookya

// Features:
// o Properly format the name and number
// o What do we do if there isn't any mobile service available?
// o Set up the automated background service to check for birthdays every 24 hours
//
// ––– Finished ~ –––
//
// √ Add a recipient's name and number to some birthday
// √ Properly format the date
// √ Read and write the data to files on the device (this allows for easier editing with external
//  applications)
// √ Code support for sending SMS messages

package us.mrvivacio.happybirthday;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;

import static android.support.v4.app.NotificationCompat.PRIORITY_MIN;
import static android.support.v4.app.NotificationCompat.VISIBILITY_PUBLIC;
import static us.mrvivacio.happybirthday.Utilities.reformat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPermissions();

        startService(new Intent(this, MyAlarmService.class));

        Button add = findViewById(R.id.b_Add);
        Button view = findViewById(R.id.b_View);

        // Add a recipient
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

                // We have valid input, so save this person to our data
                else {
                    // myToast("Good work: " + name + " -- " + number);
                    String date = reformat(month, day);
                    // save(name, number, date);
                    saveToFile(name, number, date);

                    // Empty the input fields
                    ((TextView) findViewById(R.id.et_Name)).setText("");
                    ((TextView) findViewById(R.id.et_PhoneNum)).setText("");
                    ((TextView) findViewById(R.id.et_Month)).setText("");
                    ((TextView) findViewById(R.id.et_Day)).setText("");
                }
            }
        });

        // View the specified date's birthdays via Toast
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String month = ((TextView) findViewById(R.id.et_Month)).getText().toString();
                String day = ((TextView) findViewById(R.id.et_Day)).getText().toString();

                // Input validation
                if (month.length() < 1 || month.length() > 2) {
                    myToast("Please add a valid month");
                    return;
                } else if (day.length() < 1 || day.length() > 2) {
                    myToast("Please add a valid day");
                    return;
                }

                String date = reformat(month, day);

                show(date);
            }
        });
    }

    // Function show
    // View the recipient's saved for this date
    private void show(String date) {
        // Thank you, https://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder,
        // https://stackoverflow.com/questions/1239026/how-to-create-a-file-in-android
        try {
            // Construct the filepath for the received month
            // First, get the path to external storage...
            File path = Environment.getExternalStorageDirectory();

            // ...and specify the HappyBirthday folder with the appropriate month
            String month = date.substring(0, date.length() - 2);
            String day = date.substring(3);
            File dir = new File(path.getAbsolutePath() + "/HappyBirthday/" + month);
            // Log.d("fuck u", "path = " + dir);

            // Check if the directory exists
            if (!dir.isDirectory()) {
                // This directory doesn't exist, so nobody is saved!
                myToast("No birthdays for this date were saved");
            }

            // Now, we definitely know that the directory exists, so let's keep going
            // Create the filepath of this date's recipient data
            File file = new File(dir, day + ".txt");

            // Check if the file exists
            // Thank you, https://stackoverflow.com/questions/15571496/how-to-check-if-a-folder-exists
            if (!file.exists()) {
                // This file doesn't exist, so nobody is saved!
                myToast("No birthdays for this date were saved");
            }

            // Else, file exists, so open it and read the recipient data
            else {
                // Thank you, https://stackoverflow.com/questions/3806062/how-to-open-a-txt-file-and-read-numbers-in-java
                // Create a reader to parse the file
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String currLine = reader.readLine();
                String listofRecipients = date + "'s birthdays: ";

                // While we still have text to read, save each line to updatedText and keep reading
                while (currLine != null) {
                    Log.d("fuck u", "output: " + currLine);
                    listofRecipients += currLine + ", ";
                    currLine = reader.readLine();
                }

                // Great, we have all the data! Let's take a look!
                // Substring to remove the last comma lol
                myToast(listofRecipients.substring(0, listofRecipients.length() - 2));
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
            myToast("Uh oh error: " + ioe);
        }

    }

    // Function sendSMS
    // Sends the msg to the phoneNo
    // Thank you, https://stackoverflow.com/questions/26311243/sending-sms-programmatically-without-opening-message-app
    private void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            // myToast("Message sent ~");

        } catch (Exception ex) {
            myToast("Error with sendSMS: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void initPermissions() {
        // Request storage permissions if not yet authorized
        // Thank you, https://stackoverflow.com/questions/32635704/android-permission-doesnt-work-even-if-i-have-declared-it
        int PERMISSION_REQUEST_CODE = 1;

        // Request file read/write permissions
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

            // Request SMS permission if not yet authorized
            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("fuck u", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }
        }
    }

    // Function saveToFile
    // Saves the recipient data to files in the phone's storage to enable easier "on the fly" editing
    // No more endless sharedPreferences .remove .put .apply
    private void saveToFile(String name, String number, String date) {
        // Thank you, https://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder,
        // https://stackoverflow.com/questions/1239026/how-to-create-a-file-in-android
        try {
            String msgToSend = "Automated: Thank you for letting me add u to my tiny HappyBirthday app, xoxo Vivek";
            // Construct the filepath for the received month
            // First, get the path to external storage...
            File path = Environment.getExternalStorageDirectory();

             // ...and specify the HappyBirthday folder with the appropriate month
            String month = date.substring(0, date.length() - 2);
            String day = date.substring(3);
            File dir = new File(path.getAbsolutePath() + "/HappyBirthday/" + month);
            // Log.d("fuck u", "path = " + dir);

            // Check if the directory exists
            if (!dir.isDirectory()) {
                // This directory doesn't exist, so let's create it real quick
                dir.mkdir();
                // Log.d("fuck u", "saveToFile: created directory ~");
            }

            // Now, we definitely know that the directory exists, so let's keep going
            // Create the filepath of this date's recipient data
            File file = new File(dir, day + ".txt");

            // Check if the file exists
            // Thank you, https://stackoverflow.com/questions/15571496/how-to-check-if-a-folder-exists
            if (!file.exists()) {
                // File doesn't exist, so let's create a new file and save this recipient
                FileOutputStream os = new FileOutputStream(file);
                String data = name + "/" + number + "\n";

                os.write(data.getBytes());
                // Log.d("fuck u", "CREATING NEW FILE");

                os.close();

                // Our job here is done
                myToast("New date, saved: " + name);
                sendSMS(number, msgToSend);
            }

            // Else, file exists, so open it and append this new recipient (instead of overwriting it lmao)
            else {
                // Log.d("fuck u", "MODIFYING OLD FILE");

                // Thank you, https://stackoverflow.com/questions/3806062/how-to-open-a-txt-file-and-read-numbers-in-java
                // Create a reader to parse the file
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String currLine = reader.readLine();
                String updatedText = "";

                // While we still have text to read, save each line to updatedText and keep reading
                while (currLine != null) {
                    Log.d("fuck u", "output: " + currLine);
                    updatedText += currLine + "\n";
                    currLine = reader.readLine();
                }

                // Great, we have all the original data, so let's append the new recipient
                String data = name + "/" + number + "\n";
                updatedText += data;
                // Log.d("fuck u", "finally: " + updatedText);

                // Overwrite the file with our new recipient
                // (this process might be real shitty for if we have > 1000 people in a file...I guess this doesn't scale lol whatever)
                FileOutputStream os = new FileOutputStream(file);
                os.write(updatedText.getBytes());

                os.close();

                // Our job here is done
                myToast("Updated date, added: " + name);
                sendSMS(number, msgToSend);
            }

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

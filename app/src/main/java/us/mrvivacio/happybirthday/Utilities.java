package us.mrvivacio.happybirthday;

import android.os.Environment;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {
    // Reads the previous date checked in from SharedPrefs (re: file io)
    // If prevDate is same as today, don't do anything
    // Else, we have a new day we need to check, so SMS all the recipients on today's list and update
    //  prevDate in our SharedPrefs (re: file io)
    public static void checkDate() {
        // "CaN't ReferEnce non-StAtIc MethoD fRom a StatiC ConTeXt" rip shared preferences idea
        // Back to our hero, file io

        try {
            // Construct the filepath for the received month
            // First, get the path to external storage...
            File path = Environment.getExternalStorageDirectory();

            // ...and specify the HappyBirthday folder in order to access our prevDate info
            File dir = new File(path.getAbsolutePath() + "/HappyBirthday/");

            // Check if the directory exists
            if (!dir.isDirectory()) {
                // This directory doesn't exist, so let's create it real quick
                dir.mkdir();
                // Log.d("fuck u", "saveToFile: created directory ~");
            }

            // Now, we definitely know that the directory exists, so let's keep going
            // Create the filepath of this date's recipient data
            File file = new File(dir, "prevDate.txt");

            // Check if the file exists
            // Thank you, https://stackoverflow.com/questions/15571496/how-to-check-if-a-folder-exists
            if (!file.exists()) {
                // File doesn't exist, so let's create a new file and save today's date
                FileOutputStream os = new FileOutputStream(file);

                String todayDate;
                DateFormat dateFormat = new SimpleDateFormat("MM/dd");
                Date date = new Date();

                todayDate = dateFormat.format(date);

                os.write(todayDate.getBytes());
                // Log.d("fuck u", "CREATING NEW FILE");

                os.close();

                // Our job here is done
            }

            // Else, file exists, so open it and check the date
            else {
                // Thank you, https://stackoverflow.com/questions/3806062/how-to-open-a-txt-file-and-read-numbers-in-java
                // Create a reader to parse the file
                BufferedReader reader = new BufferedReader(new FileReader(file));
                // The first (and only) line of the file should be the date
                String prevDate = reader.readLine();


                // Great, we have our prevDate , so let's investigate it
                // Get today's date to compare against
                String todayDate;
                DateFormat dateFormat = new SimpleDateFormat("MM/dd");
                Date date = new Date();

                todayDate = dateFormat.format(date);

                // When using the same dates (ie. 12/17 and 12/17), using == operator returned false for whatever reason whereas .contains() returns true
                // Log.d("datezzz", "checkDate: " + prevDate + " ==? " + todayDate + ": " + (prevDate.contains(todayDate)));

                // If prevDate is the same as todayDate, do nothing
                if (prevDate.contains(todayDate)) {
                    // Nothing needs to be done
                    return;
                }

                // Else, our dates are different, so today is a new day
                // As a result, we have to update our file and send our texts!
                // Update the file
                FileOutputStream os = new FileOutputStream(file);
                os.write(todayDate.getBytes());

                os.close();

                // Send the texts
                sendSMS();


                // Our job here is done
//                myToast("Updated date, added: " + name);
//                sendSMS(number, msgToSend);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
//            myToast("Uh oh error: " + ioe);
        }

    }

    // Function reformat
    // Reformats the date parameters into an easier-to-work-with format
    public static String reformat(String month, String day) {
        String retDate = "";

        // Formatting for the singular months (ie. 04 is April)
        if (month.length() == 1) {
            month = "0" + month;
        }

        // Add slash delimiter
        retDate += month;
        retDate += "/";

        // Formatting for the singular days (ie. First of May should be 01 instead of just 1)
        if (day.length() == 1) {
            day = "0" + day;
        }

        retDate += day;

        return retDate;
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
            // Bump myself
            sendSMS(EnvironmentVars.myNumber, "Error from Utilities.sendSMS");
            ex.printStackTrace();
        }
    }
}

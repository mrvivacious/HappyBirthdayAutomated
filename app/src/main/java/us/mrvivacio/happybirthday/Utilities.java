package us.mrvivacio.happybirthday;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

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

            // ...and specify the HappyBirthday folder with the appropriate month
//        String month = date.substring(0, date.length() - 2);
//        String day = date.substring(3);
//        File dir = new File(path.getAbsolutePath() + "/HappyBirthday/" + month);
            // Log.d("fuck u", "path = " + dir);

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
                // File doesn't exist, so let's create a new file and save this recipient
                FileOutputStream os = new FileOutputStream(file);
//                String data = name + "/" + number + "\n";

                String data = "ice kremz";

                os.write(data.getBytes());
                 Log.d("fuck u", "CREATING NEW FILE");

                os.close();

                // Our job here is done
//                myToast("New date, saved: " + name);
//                sendSMS(number, msgToSend);
            }

            // Else, file exists, so open it and append this new recipient (instead of overwriting it lmao)
            else {
                // Log.d("fuck u", "MODIFYING OLD FILE");

                // Thank you, https://stackoverflow.com/questions/3806062/how-to-open-a-txt-file-and-read-numbers-in-java
                // Create a reader to parse the file
//                BufferedReader reader = new BufferedReader(new FileReader(file));
//                String currLine = reader.readLine();
//                String updatedText = "";
//
//                // While we still have text to read, save each line to updatedText and keep reading
//                while (currLine != null) {
//                    Log.d("fuck u", "output: " + currLine);
//                    updatedText += currLine + "\n";
//                    currLine = reader.readLine();
//                }
//
//                // Great, we have all the original data, so let's append the new recipient
////                String data = name + "/" + number + "\n";
////                updatedText += data;
//                // Log.d("fuck u", "finally: " + updatedText);
//
//                // Overwrite the file with our new recipient
//                // (this process might be real shitty for if we have > 1000 people in a file...I guess this doesn't scale lol whatever)
//                FileOutputStream os = new FileOutputStream(file);
//                os.write(updatedText.getBytes());
//
//                os.close();

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
}

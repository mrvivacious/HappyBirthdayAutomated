package us.mrvivacio.happybirthday;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button add = findViewById(R.id.b_Add);

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
                }

                else if (number.length() < 1) {
                    myToast("Please add a phone number");
                    return;
                }

                else if (month.length() < 1 || month.length() > 2) {
                    myToast("Please add a valid month");
                    return;
                }

                else if (day.length() < 1 || day.length() > 2) {
                    myToast("Please add a valid day");
                    return;
                }

                // We have valid input, so add this person to sharedPref
                else {
//                    myToast("Good work: " + name + " -- " + number);
                    String date = reformat(month, day);
                    save(name, number, date);
                }
            }
        });
    }

    // Function reformat
    // Reformats the date parameters into an easier-to-work-with format
    private String reformat(String month, String day) {
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

    // Function save
    // Saves contact to shared preferences
    private void save(String name, String number, String date) {
        // Thank you, https://developer.android.com/training/data-storage/shared-preferences#java
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("01/01");

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
    }

    // Function myToast
    // Quicker and more readable way to make Toasts without all the code
    private void myToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }
}

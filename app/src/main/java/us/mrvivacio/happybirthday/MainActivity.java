package us.mrvivacio.happybirthday;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

                else if (month.length() < 1) {
                    myToast("Please add a month");
                    return;
                }

                else if (day.length() < 1) {
                    myToast("Please add a day");
                    return;
                }

                // We have valid input, so add this person to sharedPref
                else {
//                    myToast("Good work: " + name + " -- " + number);
                    // TODO:: format month and day into a singular date
                    save(name, number, month, day);
                }
            }
        });
    }

    // Function save
    // Saves contact to shared preferences
    private void save(String name, String number, String month, String day) {
        // Thank you, https://developer.android.com/training/data-storage/shared-preferences#java
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("fuck you", name + "HELLOOOOOO");
        editor.apply();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String saved = sharedPref.getString("fuck you", "rip doesn't exist");
        myToast("True : Saved == " + name + " : " + saved);

    }

    // Function myToast
    // Quicker and more readable way to make Toasts without all the code
    private void myToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }
}

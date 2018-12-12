package us.mrvivacio.happybirthday;

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

                }
            }
        });
    }

    private void myToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}

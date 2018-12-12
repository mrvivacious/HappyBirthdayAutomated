package us.mrvivacio.happybirthday;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String name;
        String number;
        String month;
        String day;

        Button add = findViewById(R.id.b_Add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                name = findViewById(R.id.et_Name).toString();
//                number = findViewById(R.id.et_PhoneNum).toString();
//                month = findViewById(R.id.et_Month).toString();
//                day = findViewById(R.id.et_Day).toString();

                Toast.makeText(MainActivity.this, "sup dawg", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

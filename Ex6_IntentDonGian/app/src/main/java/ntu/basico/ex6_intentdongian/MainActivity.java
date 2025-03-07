package ntu.basico.ex6_intentdongian;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button nutMH2;
    Button nutMH3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nutMH2 = (Button) findViewById(R.id.btnMH2);
        nutMH3 = (Button) findViewById(R.id.btnMH3);

        nutMH2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Xử lý chuyển màn hình
                // B1. Tạo một Intent
                Intent intentMH2 = new Intent(MainActivity.this, MH2Activity.class);
                // B2. Gửi
                startActivity(intentMH2);
            }
        });

        nutMH3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMH3 = new Intent(MainActivity.this, MH3Activity.class);
                startActivity(intentMH3);
            }
        });

    }
}
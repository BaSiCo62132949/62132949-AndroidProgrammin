package ntu.basico.ontapgk;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;
import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button btn_Cau1, btn_Cau2, btn_Cau3, btn_Cau4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_Cau1 = findViewById(R.id.btn_Cau1);
        Button btn_Cau2 = findViewById(R.id.btn_Cau2);
        Button btn_Cau3 = findViewById(R.id.btn_Cau3);
        Button btn_Cau4 = findViewById(R.id.btn_Cau4);

        btn_Cau1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Cau1.class);
                startActivity(intent);
            }
        });

        btn_Cau2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Cau2.class);
                startActivity(intent);
            }
        });

        btn_Cau3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Cau3.class);
                startActivity(intent);
            }
        });

        btn_Cau4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Cau4.class);
                startActivity(intent);
            }
        });


    }
}
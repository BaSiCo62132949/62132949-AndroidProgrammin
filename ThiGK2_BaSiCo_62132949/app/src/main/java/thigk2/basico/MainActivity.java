package thigk2.basico;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button btn_Cn1, btn_Cn2, btn_Cn3, btn_Cn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btn_Cn1 = findViewById(R.id.btn_Cn1);
        btn_Cn2 = findViewById(R.id.btn_Cn2);
        btn_Cn3 = findViewById(R.id.btn_Cn3);
        btn_Cn4 = findViewById(R.id.btn_Cn4);

        btn_Cn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityCn1.class);
                startActivity(intent);
            }
        });

        btn_Cn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityCn2.class);
                startActivity(intent);
            }
        });

        btn_Cn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityCn3.class);
                startActivity(intent);
            }
        });

        btn_Cn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityCn4.class);
                startActivity(intent);
            }
        });

    }
}

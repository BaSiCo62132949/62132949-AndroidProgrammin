package ntu.basico.baithick_62132949;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import ntu.basico.baithick_62132949.R;
import ntu.basico.baithick_62132949.MainActivity;

public class WelcomeActivity extends AppCompatActivity {

    MaterialButton buttonBatDauNgay;
    MaterialButton buttonHuongDan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Ẩn Action Bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // view
        buttonBatDauNgay = findViewById(R.id.buttonBatDauNgay);
        buttonHuongDan = findViewById(R.id.buttonHuongDan);

        //click
        buttonBatDauNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, ntu.basico.baithick_62132949.MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonHuongDan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WelcomeActivity.this, "Chức năng hướng dẫn sẽ được cập nhật!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

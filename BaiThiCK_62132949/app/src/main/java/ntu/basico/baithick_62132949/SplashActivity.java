package ntu.basico.baithick_62132949;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import ntu.basico.baithick_62132949.R;
import ntu.basico.baithick_62132949.WelcomeActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int THOI_GIAN_CHO = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Handler để chuyển màn hình sau 1 khoảng thời gian
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, ntu.basico.baithick_62132949.WelcomeActivity.class); // Hoặc MainActivity nếu bỏ Welcome
                startActivity(intent);
                finish();
            }
        }, THOI_GIAN_CHO);
    }
}
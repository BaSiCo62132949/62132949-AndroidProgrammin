package ntu.basico.ex7_intentlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timDK();
    }

    Button btnDangNhap;
    private void timDK(){
        btnDangNhap = (Button) findViewById(R.id.btnDangNhap);
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lệnh Xử lý ở đây
                Intent iLogin = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(iLogin);
            }
        });
    }
}
package ntu.basico.ex7_intentlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    TextView tvTenDN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        addViews();
        hienThi();
    }
    private void addViews(){
        tvTenDN = (TextView) findViewById(R.id.tvUserName);
    }
    private void hienThi(){
        Intent intent = getIntent();
        String tenDN = intent.getStringExtra("ten_dang_nhap");
        tvTenDN.setText(tenDN);
    }
}
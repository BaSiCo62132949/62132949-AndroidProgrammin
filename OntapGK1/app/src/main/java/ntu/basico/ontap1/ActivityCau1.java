package ntu.basico.ontap1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActivityCau1 extends AppCompatActivity {

    EditText txtA, txtB;
    TextView txtKetQua;
    Button btnTinhTong;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cau1);

        txtA = findViewById(R.id.NhapA);
        txtB = findViewById(R.id.NhapB);
        txtKetQua = findViewById(R.id.KetQua);
        btnTinhTong = findViewById(R.id.btnTinhTong);

        btnTinhTong.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                tinhTong();
            }
        });

    }

    public void tinhTong() {
        String a = txtA.getText().toString();
        String b = txtB.getText().toString();

        int soA = Integer.parseInt(a);
        int soB = Integer.parseInt(b);

        int tong = soA + soB;

        txtKetQua.setText("Kết quả: " + tong);

    }
}
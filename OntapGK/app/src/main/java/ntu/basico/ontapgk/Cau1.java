package ntu.basico.ontapgk;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Cau1 extends AppCompatActivity {

    private EditText editTextA, editTextB;
    private Button btnTinhTong;
    private TextView textViewKetQua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cau1);

        editTextA = findViewById(R.id.editTextA);
        editTextB = findViewById(R.id.editTextB);
        btnTinhTong = findViewById(R.id.btnTinhTong);
        textViewKetQua = findViewById(R.id.textViewKetQua);

        btnTinhTong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int a = Integer.parseInt(editTextA.getText().toString());
                    int b = Integer.parseInt(editTextB.getText().toString());
                    int tong = a + b;
                    textViewKetQua.setText(" " + tong);
                } catch (NumberFormatException e) {
                    Toast.makeText(Cau1.this, "Vui lòng nhập số hợp lệ", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }
}
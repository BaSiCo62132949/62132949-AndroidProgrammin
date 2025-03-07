package ntu.basico.ex7_intentlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText edtTenDN, edPass;
    Button btnXacNhan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addViews();

    }

    private void addViews() {
        edtTenDN = (EditText) findViewById(R.id.edtUsername);
        edPass = (EditText) findViewById(R.id.edtPass);
        btnXacNhan = (Button) findViewById(R.id.btnOK);
    }

    public void xacnhan(View view){
        String tenDangNhap = edtTenDN.getText().toString();
        String mk = edPass.getText().toString();
        if (tenDangNhap.equals("basico") && mk.equals("123") ) // mk đúng
        {
            Intent iQuiz = new Intent(this, HomeActivity.class);
            iQuiz.putExtra("ten_dang_nhap",tenDangNhap);
            startActivity(iQuiz);
        }
        else
        {
            Toast.makeText(LoginActivity.this, "BẠN NHẬP SAI THÔNG TIN", Toast.LENGTH_LONG).show();
        }
    }
}
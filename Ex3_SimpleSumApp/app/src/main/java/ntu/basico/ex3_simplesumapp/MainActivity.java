package ntu.basico.ex3_simplesumapp;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText edtNumber1, edtNumber2, txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtNumber1 = findViewById(R.id.edtNumber1);
        edtNumber2 = findViewById(R.id.edtNumber2);
        txtResult = findViewById(R.id.txtResult);
    }

    public void calculate(View view) {
        double num1, num2, result;
        try {
            num1 = Double.parseDouble(edtNumber1.getText().toString());
            num2 = Double.parseDouble(edtNumber2.getText().toString());
        } catch (NumberFormatException e) {
            txtResult.setText("Vui lòng nhập số hợp lệ");
            return;
        }
        result = num1 + num2;
        txtResult.setText(String.valueOf(result));
    }
}
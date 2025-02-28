package ntu.basico.ex5_addsubmuldiv_anynomous;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText edtNumber1, edtNumber2, txtResult;
    private Button btnAdd, btnSubtract, btnMultiply, btnDivide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtNumber1 = findViewById(R.id.edtNumber1);
        edtNumber2 = findViewById(R.id.edtNumber2);
        txtResult = findViewById(R.id.txtResult);

        btnAdd = findViewById(R.id.btnAdd);
        btnSubtract = findViewById(R.id.btnSubtract);
        btnMultiply = findViewById(R.id.btnMultiply);
        btnDivide = findViewById(R.id.btnDivide);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btnAdd) {
                    calculate('+');
                } else if (v.getId() == R.id.btnSubtract) {
                    calculate('-');
                } else if (v.getId() == R.id.btnMultiply) {
                    calculate('*');
                } else if (v.getId() == R.id.btnDivide) {
                    calculate('/');
                }
            }
        };

        btnAdd.setOnClickListener(listener);
        btnSubtract.setOnClickListener(listener);
        btnMultiply.setOnClickListener(listener);
        btnDivide.setOnClickListener(listener);
    }

    private void calculate(char operator) {
        double num1, num2, result = 0;
        try {
            num1 = Double.parseDouble(edtNumber1.getText().toString());
            num2 = Double.parseDouble(edtNumber2.getText().toString());
        } catch (NumberFormatException e) {
            txtResult.setText("Vui lòng nhập số hợp lệ");
            return;
        }
        switch (operator) {
            case '+': result = num1 + num2; break;
            case '-': result = num1 - num2; break;
            case '*': result = num1 * num2; break;
            case '/': result = num2 != 0 ? num1 / num2 : Double.NaN; break;
        }
        txtResult.setText(String.valueOf(result));
    }
}
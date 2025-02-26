package ntu.basico.ex4_addsubmuldiv_onclick;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

    public void add(View view) {
        calculate('+');
    }

    public void subtract(View view) {
        calculate('-');
    }

    public void multiply(View view) {
        calculate('*');
    }

    public void divide(View view) {
        calculate('/');
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
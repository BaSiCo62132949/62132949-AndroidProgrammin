package ntu.basico.appbmii;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private EditText weightEditText, heightEditText;
    private RadioButton asiaRadioButton, whoRadioButton;
    private Button calculateButton;
    private TextView resultTextView, categoryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weightEditText = findViewById(R.id.weightEditText);
        heightEditText = findViewById(R.id.heightEditText);
        asiaRadioButton = findViewById(R.id.asiaRadioButton);
        whoRadioButton = findViewById(R.id.whoRadioButton);
        calculateButton = findViewById(R.id.calculateButton);
        resultTextView = findViewById(R.id.resultTextView);
        categoryTextView = findViewById(R.id.categoryTextView);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBMI();
            }
        });
    }

    private void calculateBMI() {
        String weightStr = weightEditText.getText().toString().trim();
        String heightStr = heightEditText.getText().toString().trim();

        if (weightStr.isEmpty() || heightStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập cân nặng và chiều cao.", Toast.LENGTH_SHORT).show();
            return;
        }

        weightStr = weightStr.replace(",", ".");
        heightStr = heightStr.replace(",", ".");

        double weight, height;
        try {
            weight = Double.parseDouble(weightStr);
            height = Double.parseDouble(heightStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Vui lòng nhập số hợp lệ. Ví dụ: 1.75", Toast.LENGTH_LONG).show();
            return;
        }

        if (weight <= 0 || height <= 0) {
            Toast.makeText(this, "Cân nặng và chiều cao phải lớn hơn 0.", Toast.LENGTH_SHORT).show();
            return;
        }

        double bmi = weight / (height * height);
        DecimalFormat df = new DecimalFormat("#.#");
        String formattedBMI = df.format(bmi);
        String category = getBmiCategory(bmi);

        resultTextView.setText("Kết quả BMI: " + formattedBMI);
        categoryTextView.setText("Phân loại: " + category);
    }

    private String getBmiCategory(double bmi) {
        if (asiaRadioButton.isChecked()) {
            if (bmi < 18.5) return "Thiếu cân";
            else if (bmi < 23) return "Bình thường";
            else if (bmi < 25) return "Thừa cân - Tiền béo phì";
            else if (bmi < 30) return "Béo phì độ I";
            else return "Béo phì độ II";
        } else {
            if (bmi < 18.5) return "Thiếu cân";
            else if (bmi < 25) return "Bình thường";
            else if (bmi < 30) return "Thừa cân";
            else if (bmi < 35) return "Béo phì độ I";
            else if (bmi < 40) return "Béo phì độ II";
            else return "Béo phì độ III";
        }
    }
}

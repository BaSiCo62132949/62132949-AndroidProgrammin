package ntu.basico.ontap1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ItemActivity extends AppCompatActivity {

    TextView textViewItemDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        textViewItemDetail = findViewById(R.id.textViewItemDetail);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("NOI_DUNG_CHON")) {
            String noiDung = intent.getStringExtra("NOI_DUNG_CHON");
            textViewItemDetail.setText(noiDung); // Hiển thị dữ liệu nhận được
        } else {
            // Xử lý trường hợp không nhận được dữ liệu (ví dụ: hiển thị thông báo lỗi)
            Toast.makeText(this, "Không nhận được dữ liệu", Toast.LENGTH_SHORT).show();
            textViewItemDetail.setText("Lỗi: Không có dữ liệu");
        }
    }
}
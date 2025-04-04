package ntu.basico.ontap1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomItemActivity extends AppCompatActivity {

    ImageView imageViewDetail;
    TextView textViewTenDetail;
    TextView textViewMoTaDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_item);

        imageViewDetail = findViewById(R.id.imageViewDetail);
        textViewTenDetail = findViewById(R.id.textViewTenDetail);
        textViewMoTaDetail = findViewById(R.id.textViewMoTaDetail);

        // Nhận Intent
        Intent intent = getIntent();

        // Kiểm tra và lấy dữ liệu MonAn từ Intent
        if (intent != null && intent.hasExtra("MON_AN_DATA")) {
            // Lấy đối tượng MonAn (vì nó là Serializable)
            MonAn monAn = (MonAn) intent.getSerializableExtra("MON_AN_DATA");

            if (monAn != null) {
                // Hiển thị dữ liệu lên các View
                imageViewDetail.setImageResource(monAn.getHinhAnhResource());
                textViewTenDetail.setText(monAn.getTenMonAn());
                textViewMoTaDetail.setText(monAn.getMoTa());
            } else {
                showError();
            }
        } else {
            showError();
        }
    }

    private void showError() {
        Toast.makeText(this, "Lỗi: Không nhận được dữ liệu món ăn", Toast.LENGTH_SHORT).show();
        // Có thể đặt ảnh/text mặc định báo lỗi
        imageViewDetail.setImageResource(android.R.drawable.ic_dialog_alert); // Ảnh báo lỗi mặc định
        textViewTenDetail.setText("Lỗi");
        textViewMoTaDetail.setText("Không thể tải thông tin chi tiết.");
    }
}
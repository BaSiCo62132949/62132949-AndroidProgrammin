package ntu.basico.ontap1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent; // Import Intent
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityCau3 extends AppCompatActivity {

    RecyclerView recyclerViewMonAn;
    ArrayList<MonAn> danhSachMonAn;
    MonAnAdapter monAnAdapter;

    // --- Phần nâng cao: Tạo Activity để hiển thị chi tiết ---
    // Bạn cần tạo một Activity mới tên là CustomItemActivity
    // và một layout activity_custom_item.xml có ImageView và TextViews tương ứng
    private void chuyenSangCustomItemActivity(MonAn monAnDuocChon) {
        Intent intent = new Intent(ActivityCau3.this, CustomItemActivity.class);
        // Đính kèm toàn bộ đối tượng MonAn (vì MonAn đã implement Serializable)
        intent.putExtra("MON_AN_DATA", monAnDuocChon);
        startActivity(intent);
    }
    // --------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cau3);

        // Ánh xạ RecyclerView
        recyclerViewMonAn = findViewById(R.id.recyclerViewMonAn);

        // Chuẩn bị dữ liệu
        danhSachMonAn = new ArrayList<>();
        prepareData(); // Gọi hàm để thêm dữ liệu mẫu

        // Khởi tạo Adapter
        monAnAdapter = new MonAnAdapter(this, danhSachMonAn);

        // Thiết lập LayoutManager cho RecyclerView
        // (Có thể bỏ qua nếu đã set trong XML bằng app:layoutManager)
        recyclerViewMonAn.setLayoutManager(new LinearLayoutManager(this));

        // Gắn Adapter vào RecyclerView
        recyclerViewMonAn.setAdapter(monAnAdapter);

        // Xử lý sự kiện click item (thông qua interface trong Adapter)
        monAnAdapter.setOnItemClickListener(new MonAnAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                MonAn monAnDuocChon = danhSachMonAn.get(position);

                // --- Yêu cầu 2đ: Hiển thị Toast ---
                Toast.makeText(ActivityCau3.this, "Bạn chọn: " + monAnDuocChon.getTenMonAn(), Toast.LENGTH_SHORT).show();

                // --- Yêu cầu 1đ: Chuyển sang CustomItemActivity thay cho Toast ---
                // Bỏ comment dòng dưới và comment dòng Toast.makeText ở trên để thực hiện
                // chuyenSangCustomItemActivity(monAnDuocChon);
            }
        });
    }

    // Hàm chuẩn bị dữ liệu mẫu
    private void prepareData() {
        // QUAN TRỌNG: Thay R.drawable.ten_anh bằng ID ảnh thực tế bạn đã thêm vào drawable
        danhSachMonAn.add(new MonAn("Phở Bò", "Món ăn truyền thống Việt Nam với nước dùng đậm đà.", R.drawable.pho)); // Thay pho bằng tên file ảnh của bạn
        danhSachMonAn.add(new MonAn("Bánh Mì", "Bánh mì giòn rụm với nhiều loại nhân hấp dẫn.", R.drawable.banh_mi)); // Thay banh_mi bằng tên file ảnh của bạn
        danhSachMonAn.add(new MonAn("Bún Chả", "Thịt nướng thơm lừng ăn kèm bún và nước chấm chua ngọt.", R.drawable.bun_cha)); // Thay bun_cha bằng tên file ảnh của bạn
        danhSachMonAn.add(new MonAn("Gỏi Cuốn", "Món cuốn thanh mát với tôm, thịt, rau sống.", R.drawable.goi_cuon)); // Thay goi_cuon bằng tên file ảnh của bạn
        danhSachMonAn.add(new MonAn("Cơm Tấm", "Cơm tấm sườn bì chả thơm ngon.", R.drawable.com_tam)); // Thay com_tam bằng tên file ảnh của bạn
        // Thêm các món ăn khác nếu muốn...
    }
}
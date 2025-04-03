package ntu.basico.ontap1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ActivityCau2 extends AppCompatActivity {

    ListView lvMH;
    ArrayList<String> arrayListMonHoc;

    ArrayAdapter<String> arrayAdapterMonHoc;

    private void ItemActivity(String noiDung) {
        Intent intent = new Intent(ActivityCau2.this, ItemActivity.class);
        intent.putExtra("noi_Dung_chon", noiDung);
        startActivity(intent);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cau2);

        lvMH = findViewById(R.id.DanhSachMH);
        arrayListMonHoc = new ArrayList<>();
        arrayListMonHoc.add("Lập trình Android");
        arrayListMonHoc.add("Cơ sở dữ liệu");
        arrayListMonHoc.add("Cấu trúc dữ liệu và giải thuật");
        arrayListMonHoc.add("Mạng máy tính");
        arrayListMonHoc.add("Thiết kế Web");
        arrayListMonHoc.add("An toàn thông tin");
        arrayListMonHoc.add("Trí tuệ nhân tạo");

        arrayAdapterMonHoc = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListMonHoc);
        lvMH.setAdapter(arrayAdapterMonHoc);

        lvMH.setOnItemClickListener(new AdapterView.OnItemClickListener()  {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                String noiDung = arrayListMonHoc.get(position);
                ItemActivity(noiDung);
            }
        });

    }
}
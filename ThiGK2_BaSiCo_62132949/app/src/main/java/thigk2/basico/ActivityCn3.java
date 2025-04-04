package thigk2.basico;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ActivityCn3 extends AppCompatActivity {

    ListView listViewSongs;
    ArrayList<String> songList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cn3);

        listViewSongs = findViewById(R.id.listViewSongs);
        songList = new ArrayList<>();

        songList.add("Tiến về Sài Gòn");
        songList.add("Giải phóng Miền Nam");
        songList.add("Đất nước trọn niềm vui");
        songList.add("Bài ca thống nhất");
        songList.add("Mùa xuân trên thành phố HCM");
        songList.add("Như có Bác Hồ trong ngày vui đại thắng");
        songList.add("Cô gái Sài Gòn đi tải đạn");
        songList.add("Tiếng hát từ thành phố mang tên Người");

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                songList);

        listViewSongs.setAdapter(adapter);

        listViewSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSong = songList.get(position);
                Intent intent = new Intent(ActivityCn3.this, Item3Activity.class);

                intent.putExtra(EXTRA_SONG_TITLE, selectedSong);


                startActivity(intent);
            }
        });


    }
}
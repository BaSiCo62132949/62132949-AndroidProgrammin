package thigk2.basico;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Item3Activity extends AppCompatActivity {

    TextView textViewContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item3);

        setTitle(getString(R.string.item3_activity_title));
        textViewContent = findViewById(R.id.textItem3);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(ActivityCn3.EXTRA_SONG_TITLE)) {
            String songTitle = intent.getStringExtra(ActivityCn3.EXTRA_SONG_TITLE);

            textViewContent.setText(songTitle);
        } else {
            textViewContent.setText("Lỗi: Không nhận được tên bài hát.");
        }
    }
}
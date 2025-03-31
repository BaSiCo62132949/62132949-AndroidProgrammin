package ntu.basico.ontapgk;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Cau4 extends AppCompatActivity {

    private ImageView imageViewAvatar;
    private TextView textViewName, textViewBio, textViewLocation, textViewInterests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cau4);

        imageViewAvatar = findViewById(R.id.image_view_avatar);
        textViewName = findViewById(R.id.text_view_name);
        textViewBio = findViewById(R.id.text_view_bio);
        textViewLocation = findViewById(R.id.text_view_location);
        textViewInterests = findViewById(R.id.text_view_interests);

        textViewName.setText("Nguyễn Văn A");
        textViewBio.setText("Sinh viên năm 3, thích lập trình và du lịch.");
        textViewLocation.setText("Địa điểm: Hà Nội");
        textViewInterests.setText("Sở thích: Âm nhạc, Du lịch, Thể thao");

    }
}
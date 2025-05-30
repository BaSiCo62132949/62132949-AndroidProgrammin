package ntu.basico.bottom_navigation_view;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bot_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bot_nav = findViewById(R.id.bot_nav);

        bot_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               int mnuItemDuocChonID = item.getItemId();
               if(mnuItemDuocChonID == R.id.mnu_home) {
                   Toast.makeText(MainActivity.this, "Thay Home", Toast.LENGTH_SHORT).show();
               }else
                   if(mnuItemDuocChonID == R.id.mnu_search) {
                       Toast.makeText(MainActivity.this, "Thay Search", Toast.LENGTH_SHORT).show();
                   }else
                   if(mnuItemDuocChonID == R.id.mnu_profile) {
                       Toast.makeText(MainActivity.this, "Thay Profile", Toast.LENGTH_SHORT).show();
                   }else return false;
                return true;
            }
        });


    }
}
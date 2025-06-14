package ntu.basico.baithick_62132949;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import ntu.basico.baithick_62132949.R;
import ntu.basico.baithick_62132949.CurrencyConverterFragment;
import ntu.basico.baithick_62132949.HistoryFragment;
import ntu.basico.baithick_62132949.SettingsFragment;
import ntu.basico.baithick_62132949.UnitConverterFragment;




public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);



        if (savedInstanceState == null) {
            loadFragment(new ntu.basico.baithick_62132949.HistoryFragment());
            bottomNavigation.setSelectedItemId(R.id.navigation_lich_su);
        }

        // Xử lý sự kiện chọn item trên Bottom Navigation
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.navigation_lich_su) {
                    selectedFragment = new ntu.basico.baithick_62132949.HistoryFragment();
                } else if (itemId == R.id.navigation_don_vi) {
                    selectedFragment = new ntu.basico.baithick_62132949.UnitConverterFragment();
                } else if (itemId == R.id.navigation_may_tinh) {
                    selectedFragment = new ntu.basico.baithick_62132949.CalculatorFragment(); Log.d("MainActivity", "Calculator tab selected!");  // Sử dụng tên lớp mới
                } else if (itemId == R.id.navigation_tien_te) {
                    selectedFragment = new ntu.basico.baithick_62132949.CurrencyConverterFragment();
                } else if (itemId == R.id.navigation_cai_dat) {
                    selectedFragment = new ntu.basico.baithick_62132949.SettingsFragment();
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                    return true;
                }
                return false;
            }
        });

        // Xử lý sự kiện click FAB
    }

    // Hàm tải Fragment vào container và ghi log thông báo
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
        Log.d("MainActivity", "Loaded fragment: " + fragment.getClass().getSimpleName());
    }
}
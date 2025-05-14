package ntu.basico.baithick_62132949;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.List;
import android.widget.AdapterView;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.materialswitch.MaterialSwitch;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";
    private static final String PREFS_NAME = "AppSettingsPrefs";
    private static final String PREF_DARK_MODE = "darkModeEnabled";

    private MaterialSwitch switchDarkMode;
    private MaterialButton buttonGioiThieu;
    private SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        switchDarkMode = view.findViewById(R.id.switchDarkMode);
        buttonGioiThieu = view.findViewById(R.id.buttonGioiThieu);



        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        setupDarkModeSwitch();
        setupAboutButton();

        return view;
    }

    private void setupDarkModeSwitch() {
        boolean isDarkModeEnabled = sharedPreferences.getBoolean(PREF_DARK_MODE, false);
        switchDarkMode.setChecked(isDarkModeEnabled);
        Log.d(TAG, "setupDarkModeSwitch: Initial dark mode state: " + isDarkModeEnabled);


        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged: Dark mode switch changed to: " + isChecked);
                applyTheme(isChecked);
                saveThemePreference(isChecked);
            }
        });
    }

    private void applyTheme(boolean isDarkMode) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void saveThemePreference(boolean isDarkMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_DARK_MODE, isDarkMode);
        editor.apply(); // Lưu bất đồng bộ
        Log.d(TAG, "saveThemePreference: Saved dark mode state: " + isDarkMode);
    }


    private void setupAboutButton() {
        buttonGioiThieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutDialog();
            }
        });
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Giới thiệu ứng dụng")
                .setMessage("Unit & Currency Converter\n\n" +
                        "Phiên bản: 1.0\n" +
                        "Sinh viên thực hiện: Ba Si Co\n" +
                        "Mã số sinh viên: 62132949\n" +
                        "Đề tài môn học: Lập trình thiết bị di động\n\n" +
                        "Ứng dụng đơn giản giúp chuyển đổi các đơn vị và tiền tệ phổ biến.")
                .setPositiveButton("Đóng", null)
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }


    public static void applySavedTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean(PREF_DARK_MODE, false);
        Log.d(TAG, "applySavedTheme: Applying saved theme - Dark Mode: " + isDarkMode);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}

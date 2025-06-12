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


import com.google.android.material.button.MaterialButton;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";
    private static final String PREFS_NAME = "AppSettingsPrefs";
    private static final String PREF_DARK_MODE = "darkModeEnabled";

    private MaterialButton buttonGioiThieu;
    private SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        
        buttonGioiThieu = view.findViewById(R.id.buttonGioiThieu);



        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        setupAboutButton();

        return view;
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

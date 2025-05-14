package ntu.basico.baithick_62132949;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.util.Locale;

public class UnitConverterFragment extends Fragment {

    private static final String TAG_FRAGMENT = "UnitConverterFrag";

    private TabLayout tabLayoutLoaiDonVi;
    private TextInputEditText editTextGiaTriNguon;
    private Spinner spinnerDonViNguon;
    private TextInputEditText textViewKetQua;
    private Spinner spinnerDonViDich;
    private ImageButton buttonHoanDoi;
    private MaterialButton buttonLuu;

    private String[] unitTypesArray;
    private String currentUnitTypeString;

    private HistoryDbHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unit_converter, container, false);

        tabLayoutLoaiDonVi = view.findViewById(R.id.tabLayoutLoaiDonVi);
        editTextGiaTriNguon = view.findViewById(R.id.editTextGiaTriNguon);
        spinnerDonViNguon = view.findViewById(R.id.spinnerDonViNguon);
        textViewKetQua = view.findViewById(R.id.textViewKetQua);
        spinnerDonViDich = view.findViewById(R.id.spinnerDonViDich);
        buttonHoanDoi = view.findViewById(R.id.buttonHoanDoi);
        buttonLuu = view.findViewById(R.id.buttonLuu);

        dbHelper = new HistoryDbHelper(requireContext());
        unitTypesArray = getResources().getStringArray(R.array.unit_types);

        setupTabs();
        setupListeners();

        if (tabLayoutLoaiDonVi.getTabCount() > 0) {
            TabLayout.Tab firstTab = tabLayoutLoaiDonVi.getTabAt(0);
            if (firstTab != null) {
                firstTab.select();
                // onTabSelected sẽ được gọi và cập nhật
            }
        } else {
            Log.e(TAG_FRAGMENT, "No tabs available to select default.");
        }
        return view;
    }

    private void setupTabs() {
        for (String type : unitTypesArray) {
            tabLayoutLoaiDonVi.addTab(tabLayoutLoaiDonVi.newTab().setText(type));
        }

        tabLayoutLoaiDonVi.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab != null && tab.getText() != null) {
                    currentUnitTypeString = tab.getText().toString(); // Gán tên loại đơn vị
                    Log.d(TAG_FRAGMENT, "Tab selected: " + currentUnitTypeString);
                    updateSpinnersForSelectedType();
                    editTextGiaTriNguon.setText("");
                    textViewKetQua.setText("");
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void updateSpinnersForSelectedType() {
        if (currentUnitTypeString == null || currentUnitTypeString.isEmpty()) {
            Log.e(TAG_FRAGMENT, "currentUnitTypeString is null or empty in updateSpinners.");
            return;
        }
        Log.d(TAG_FRAGMENT, "Updating spinners for type: " + currentUnitTypeString);

        int arrayResourceId = 0;

        if (currentUnitTypeString.equals(unitTypesArray[0])) { // Độ dài
            arrayResourceId = R.array.length_units;
        } else if (currentUnitTypeString.equals(unitTypesArray[1])) { // Khối lượng
            arrayResourceId = R.array.weight_units;
        } else if (currentUnitTypeString.equals(unitTypesArray[2])) { // Nhiệt độ
            arrayResourceId = R.array.temperature_units;
        } else if (currentUnitTypeString.equals(unitTypesArray[3])) { // Thời gian
            arrayResourceId = R.array.time_units;
        } else if (currentUnitTypeString.equals(unitTypesArray[4])) { // Diện tích
            arrayResourceId = R.array.area_units;
        } else {
            Toast.makeText(getContext(), getString(R.string.error_unknown_unit_type), Toast.LENGTH_SHORT).show();
            Log.e(TAG_FRAGMENT, "Unknown unit type: " + currentUnitTypeString);
            spinnerDonViNguon.setAdapter(null);
            spinnerDonViDich.setAdapter(null);
            return;
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                arrayResourceId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDonViNguon.setAdapter(adapter);
        spinnerDonViDich.setAdapter(adapter);

        if (adapter.getCount() > 1) {
            spinnerDonViNguon.setSelection(0, false);
            spinnerDonViDich.setSelection(1, false);
        } else if (adapter.getCount() > 0) {
            spinnerDonViNguon.setSelection(0, false);
            spinnerDonViDich.setSelection(0, false);
        }

        spinnerDonViNguon.post(this::performConversion);
    }

    private void setupListeners() {
        editTextGiaTriNguon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performConversion();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                performConversion();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                textViewKetQua.setText("");
            }
        };
        spinnerDonViNguon.setOnItemSelectedListener(spinnerListener);
        spinnerDonViDich.setOnItemSelectedListener(spinnerListener);

        buttonHoanDoi.setOnClickListener(v -> {
            int nguonPos = spinnerDonViNguon.getSelectedItemPosition();
            int dichPos = spinnerDonViDich.getSelectedItemPosition();
            if (nguonPos != Spinner.INVALID_POSITION && dichPos != Spinner.INVALID_POSITION) {
                spinnerDonViNguon.setSelection(dichPos); // Listener sẽ tự gọi performConversion
                spinnerDonViDich.setSelection(nguonPos);
            }
        });

        buttonLuu.setOnClickListener(v -> {
            String giaTriNguonStr = editTextGiaTriNguon.getText().toString();
            String ketQuaStr = textViewKetQua.getText().toString();

            if (!giaTriNguonStr.isEmpty() && !ketQuaStr.isEmpty() &&
                    !ketQuaStr.equals(getString(R.string.text_result_error)) &&
                    !ketQuaStr.equals(getString(R.string.text_result_invalid)) &&
                    spinnerDonViNguon.getSelectedItem() != null && spinnerDonViDich.getSelectedItem() != null &&
                    currentUnitTypeString != null)
            {
                String donViNguon = spinnerDonViNguon.getSelectedItem().toString();
                String donViDich = spinnerDonViDich.getSelectedItem().toString();
                boolean success = dbHelper.insertHistory(currentUnitTypeString, giaTriNguonStr, donViNguon, ketQuaStr, donViDich);
                if (success) {
                    Toast.makeText(getContext(), getString(R.string.toast_history_saved), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), getString(R.string.toast_history_save_error), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), getString(R.string.toast_no_valid_data_to_save), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performConversion() {
        String giaTriNguonStr = editTextGiaTriNguon.getText().toString();
        if (giaTriNguonStr.isEmpty() || giaTriNguonStr.equals(".") || giaTriNguonStr.equals("-") || giaTriNguonStr.equals("+")) {
            textViewKetQua.setText("");
            return;
        }

        if (spinnerDonViNguon.getSelectedItem() == null || spinnerDonViDich.getSelectedItem() == null || currentUnitTypeString == null) {
            Log.w(TAG_FRAGMENT, "Cannot convert: Input missing. currentType: " + currentUnitTypeString);
            textViewKetQua.setText("");
            return;
        }

        try {
            double giaTriNguon = Double.parseDouble(giaTriNguonStr);
            String donViNguon = spinnerDonViNguon.getSelectedItem().toString();
            String donViDich = spinnerDonViDich.getSelectedItem().toString();

            Log.d(TAG_FRAGMENT, "Performing conversion: val=" + giaTriNguon + ", from=" + donViNguon + ", to=" + donViDich + ", type=" + currentUnitTypeString);

            double ketQua = UnitConverterUtil.convert(giaTriNguon, donViNguon, donViDich, currentUnitTypeString);

            if (Double.isNaN(ketQua)) {
                textViewKetQua.setText(getString(R.string.text_result_invalid));
                Log.w(TAG_FRAGMENT, "Conversion resulted in NaN.");
            } else {
                NumberFormat numberFormatter = NumberFormat.getNumberInstance(Locale.US);
                String tempTypeString = (unitTypesArray.length > 2) ? unitTypesArray[2] : "";

                if (currentUnitTypeString.equals(tempTypeString)) { // Nhiệt độ
                    numberFormatter.setMaximumFractionDigits(2);
                } else if (Math.abs(ketQua) > 0.000001 && Math.abs(ketQua) < 1_000_000_000) {
                    numberFormatter.setMaximumFractionDigits(8);
                } else if (Math.abs(ketQua) >= 1_000_000_000) {
                    numberFormatter.setMaximumFractionDigits(2);
                } else {
                    numberFormatter.setMaximumFractionDigits(10);
                }
                numberFormatter.setMinimumFractionDigits(0);

                String formattedResult = numberFormatter.format(ketQua);
                textViewKetQua.setText(formattedResult);
                Log.d(TAG_FRAGMENT, "Conversion result: " + formattedResult);
            }

        } catch (NumberFormatException e) {
            textViewKetQua.setText("");
            Log.w(TAG_FRAGMENT, "NumberFormatException: " + giaTriNguonStr, e);
        } catch (Exception e) {
            textViewKetQua.setText(getString(R.string.text_result_error));
            Log.e(TAG_FRAGMENT, "Exception during conversion: ", e);
        }
    }

    public void ChonTabMacDinh(int tabIndex) {
        if (tabLayoutLoaiDonVi != null && tabLayoutLoaiDonVi.getTabCount() > tabIndex && tabIndex >= 0) {
            TabLayout.Tab tab = tabLayoutLoaiDonVi.getTabAt(tabIndex);
            if (tab != null) {
                tab.select();
            }
        }
    }
}
package ntu.basico.baithick_62132949;

import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UnitConverterFragment extends Fragment {

    private static final String TAG_FRAGMENT = "UnitConverterFrag";

    private TabLayout tabLayoutLoaiDonVi;
    private LinearLayout linearLayoutUnitTypeButtons; // Layout chứa các nút chọn loại đơn vị
    private TextInputEditText editTextGiaTriNguon; // Ô nhập giá trị nguồn cần chuyển đổi
    private Spinner spinnerDonViNguon; // Spinner chọn đơn vị nguồn
    private TextInputEditText textViewKetQua; // Ô hiển thị kết quả sau khi chuyển đổi
    private Spinner spinnerDonViDich; // Spinner chọn đơn vị đích
    private ImageButton buttonHoanDoi; // Nút để hoán đổi đơn vị nguồn và đích
    private MaterialButton buttonLuu; // Nút để lưu kết quả chuyển đổi vào lịch sử

    private List<View> unitTypeButtonViews = new ArrayList<>(); // Danh sách các View của nút chọn loại đơn vị
    private String[] unitTypesArray;
    private String currentUnitTypeString;

    private HistoryDbHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unit_converter, container, false);
        // khởi tạo và gọi các hàm
        linearLayoutUnitTypeButtons = view.findViewById(R.id.linearLayoutUnitTypeButtons);
        editTextGiaTriNguon = view.findViewById(R.id.editTextGiaTriNguon);
        spinnerDonViNguon = view.findViewById(R.id.spinnerDonViNguon);
        textViewKetQua = view.findViewById(R.id.textViewKetQua);
        spinnerDonViDich = view.findViewById(R.id.spinnerDonViDich);
        buttonHoanDoi = view.findViewById(R.id.buttonHoanDoi);
        buttonLuu = view.findViewById(R.id.buttonLuu);

        dbHelper = new HistoryDbHelper(requireContext());
        unitTypesArray = getResources().getStringArray(R.array.unit_types);

        setupUnitTypeButtons();
        setupListeners();

        // Chọn loại đơn vị mặc định
        if (unitTypesArray != null && unitTypesArray.length > 0) { // Kiểm tra null và độ dài
            selectUnitType(0); // Chọn loại đầu tiên
        }

        return view;
    }

    // Tạo và thêm các nút chọn loại đơn vị vào linearLayoutUnitTypeButtons
    private void setupUnitTypeButtons() {
        linearLayoutUnitTypeButtons.removeAllViews(); // Xóa các nút cũ nếu có
        unitTypeButtonViews.clear();
        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (int i = 0; i < unitTypesArray.length; i++) {
            String typeName = unitTypesArray[i];
            View buttonView = inflater.inflate(R.layout.button_unit_type_item, linearLayoutUnitTypeButtons, false);

            ImageView iconView = buttonView.findViewById(R.id.imageViewUnitTypeIcon);
            TextView nameView = buttonView.findViewById(R.id.textViewUnitTypeName);

            nameView.setText(typeName);
            // Đặt icon dựa trên typeName
            iconView.setImageResource(getIconForUnitType(typeName));

            final int index = i;
            buttonView.setOnClickListener(v -> selectUnitType(index));

            linearLayoutUnitTypeButtons.addView(buttonView);
            unitTypeButtonViews.add(buttonView);
        }
    }

    // Hàm chọn loại đơn vị và cập nhật UI
    private void selectUnitType(int index) {
        if (index < 0 || index >= unitTypesArray.length) return;

        currentUnitTypeString = unitTypesArray[index];
        Log.d(TAG_FRAGMENT, "Unit type selected: " + currentUnitTypeString);

        // Cập nhật trạng thái selected cho các nút
        for (int i = 0; i < unitTypeButtonViews.size(); i++) {
            View btnView = unitTypeButtonViews.get(i);
            ImageView icon = btnView.findViewById(R.id.imageViewUnitTypeIcon);
            TextView text = btnView.findViewById(R.id.textViewUnitTypeName);
            if (i == index) {
                // Nút được chọn: thay đổi background hoặc màu icon/text
                btnView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.app_secondary)); // Tạo màu này
                icon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.app_text_primary)); // Tạo màu này
                text.setTextColor(ContextCompat.getColor(requireContext(), R.color.app_text_primary)); // Tạo màu này
            } else {
                // Nút không được chọn: trở về trạng thái bình thường
                btnView.setBackgroundColor(Color.TRANSPARENT); // Tạo drawable này
                icon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white)); // Tạo màu này
                text.setTextColor(ContextCompat.getColor(requireContext(), R.color.white)); // Tạo màu này
            }
        }

        updateSpinnersForSelectedType(); // Hàm này giữ nguyên logic
        editTextGiaTriNguon.setText("");
        textViewKetQua.setText("");
    }

    // Hàm để lấy resource ID của icon dựa trên tên loại đơn vị
    private int getIconForUnitType(String unitTypeName) {
        if (unitTypeName.equals(unitTypesArray[0])) return R.drawable.ic_type_length; // Độ dài
        if (unitTypeName.equals(unitTypesArray[1])) return R.drawable.ic_type_weight; // Khối lượng
        if (unitTypeName.equals(unitTypesArray[2])) return R.drawable.ic_type_temperature; // Nhiệt độ
        if (unitTypeName.equals(unitTypesArray[3])) return R.drawable.ic_type_time; // Thời gian
        if (unitTypeName.equals(unitTypesArray[4])) return R.drawable.ic_type_area; // Diện tích
        return R.drawable.info; // Icon mặc định
    }

    // Cập nhật nội dung (danh sách đơn vị con) cho spinnerDonViNguon và spinnerDonViDich
    // dựa trên currentUnitTypeString (loại đơn vị vừa được chọn).
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

    // Gán các listener (TextWatcher, OnItemSelectedListener, OnClickListener) cho các View tương tác
    // như ô nhập liệu, Spinners, nút Hoán đổi, nút Lưu
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

    // Thực hiện phép chuyển đổi chính: lấy giá trị nhập, đơn vị nguồn/đích, loại đơn vị;
    // gọi UnitConverterUtil.convert(); định dạng và hiển thị kết quả hoặc thông báo lỗi.
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

    public void ChonLoaiDonViMacDinh(int typeIndex) { // Đổi tên và tham số
        if (unitTypeButtonViews != null && typeIndex >= 0 && typeIndex < unitTypeButtonViews.size()) {
            selectUnitType(typeIndex); // Gọi hàm select mới
        } else {
            Log.e(TAG_FRAGMENT, "Cannot select default unit type at index: " + typeIndex);
        }
    }

}
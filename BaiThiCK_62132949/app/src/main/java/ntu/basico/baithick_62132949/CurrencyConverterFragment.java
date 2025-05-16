package ntu.basico.baithick_62132949;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CurrencyConverterFragment extends Fragment implements CurrencyRowAdapter.OnCurrencyInteractionListener {

    private static final String TAG = "CurrencyNeoFrag";
    private static final String API_URL = "https://open.er-api.com/v6/latest/USD"; // Hoặc API của bạn

    private RecyclerView recyclerViewCurrencies;
    private CurrencyRowAdapter adapter;
    private List<CurrencyDisplayItem> displayItems = new ArrayList<>();
    private Map<String, Double> exchangeRates = new HashMap<>(); // Tỷ giá so với USD

    private TextView textViewLastUpdate;
    private ImageButton buttonRefreshRatesTop;
    private ProgressBar progressBarLoading;
    private TextView textViewErrorMessage;

    private RequestQueue requestQueue;
    private NumberFormat numberFormatter;

    // Base currency và giá trị nhập
    private String currentBaseCurrencyCode = "VND"; // Mặc định
    private String currentInputValue = "1"; // Giá trị người dùng nhập cho base currency

    // Bàn phím
    private LinearLayout customKeyboardLayout;
    private ImageButton buttonSaveHistoryTop;
    private HistoryDbHelper dbHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_currency_converter, container, false);

        textViewLastUpdate = view.findViewById(R.id.textViewLastUpdate);
        buttonRefreshRatesTop = view.findViewById(R.id.buttonRefreshRatesTop);
        progressBarLoading = view.findViewById(R.id.progressBarLoading);
        textViewErrorMessage = view.findViewById(R.id.textViewErrorMessage);
        customKeyboardLayout = view.findViewById(R.id.customKeyboardLayout);
        buttonSaveHistoryTop = view.findViewById(R.id.buttonSaveHistoryTop);

        recyclerViewCurrencies = view.findViewById(R.id.recyclerViewCurrencies);
        recyclerViewCurrencies.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CurrencyRowAdapter(requireContext(), displayItems, this);
        dbHelper = new HistoryDbHelper(requireContext());
        recyclerViewCurrencies.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(requireContext());
        numberFormatter = NumberFormat.getNumberInstance(Locale.US); // Dùng '.' làm dấu thập phân
        numberFormatter.setGroupingUsed(true); // Bật dấu phẩy/chấm hàng nghìn
        numberFormatter.setMaximumFractionDigits(2); // Tối đa 2 số lẻ cho hiển thị tiền tệ

        setupKeyboardListeners(view);
        buttonRefreshRatesTop.setOnClickListener(v -> fetchExchangeRates());

        fetchExchangeRates();
        setupSaveButtonListener();
        return view;
    }

    private void setupKeyboardListeners(View rootView) {
        View.OnClickListener numberClickListener = v -> {
            Button b = (Button) v;
            appendNumberToInput(b.getText().toString());
        };

        rootView.findViewById(R.id.buttonKey0).setOnClickListener(numberClickListener);
        rootView.findViewById(R.id.buttonKey1).setOnClickListener(numberClickListener);
        // ... (Tương tự cho các nút số khác 2-9) ...
        for (int i = 2; i <= 9; i++) {
            int resId = getResources().getIdentifier("buttonKey" + i, "id", requireActivity().getPackageName());
            if (resId != 0) rootView.findViewById(resId).setOnClickListener(numberClickListener);
        }


        rootView.findViewById(R.id.buttonKeyDecimal).setOnClickListener(v -> {
            if (!currentInputValue.contains(",")) { // Chỉ cho phép một dấu phẩy
                appendNumberToInput(",");
            }
        });

        rootView.findViewById(R.id.buttonKeyClear).setOnClickListener(v -> {
            currentInputValue = "0";
            updateConversions();
        });

        rootView.findViewById(R.id.buttonKeyBackspace).setOnClickListener(v -> {
            if (currentInputValue.length() > 1) {
                currentInputValue = currentInputValue.substring(0, currentInputValue.length() - 1);
            } else if (currentInputValue.length() == 1 && !currentInputValue.equals("0")) {
                currentInputValue = "0";
            }
            updateConversions();
        });

        rootView.findViewById(R.id.buttonKeyPercent).setEnabled(false);
        rootView.findViewById(R.id.buttonKeyDivide).setEnabled(false);
        rootView.findViewById(R.id.buttonKeyMultiply).setEnabled(false);
        rootView.findViewById(R.id.buttonKeySubtract).setEnabled(false);
        rootView.findViewById(R.id.buttonKeyAdd).setEnabled(false);
        rootView.findViewById(R.id.buttonKeyEquals).setEnabled(false);
    }

    private void appendNumberToInput(String digit) {
        if (currentInputValue.equals("0") && !digit.equals(",")) {
            currentInputValue = digit;
        } else {
            // Giới hạn độ dài nhập liệu nếu cần
            if (currentInputValue.length() < 15) { // Ví dụ giới hạn 15 ký tự
                currentInputValue += digit;
            }
        }
        updateConversions();
    }


    private void fetchExchangeRates() {
        progressBarLoading.setVisibility(View.VISIBLE);
        textViewErrorMessage.setVisibility(View.GONE);
        recyclerViewCurrencies.setVisibility(View.INVISIBLE); // Ẩn đi khi đang tải

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API_URL, null,
                response -> {
                    progressBarLoading.setVisibility(View.GONE);
                    recyclerViewCurrencies.setVisibility(View.VISIBLE);
                    try {
                        if ("success".equals(response.getString("result"))) {
                            updateTimestamp(response.getLong("time_last_update_unix"));
                            JSONObject rates = response.getJSONObject("rates");
                            exchangeRates.clear();
                            List<String> tempCurrencyCodes = new ArrayList<>(); // Danh sách mã tiền tệ tạm thời

                            Iterator<String> keys = rates.keys();
                            while (keys.hasNext()) {
                                String key = keys.next();
                                exchangeRates.put(key, rates.getDouble(key));
                                tempCurrencyCodes.add(key);
                            }
                            setupDisplayItems(filterAndSortCurrencies(tempCurrencyCodes));

                            setBaseCurrencyByCode(currentBaseCurrencyCode);
                            updateConversions(); // Tính toán lại với tỷ giá mới
                        } else {
                            handleApiError("API error: " + response.optString("error-type"));
                        }
                    } catch (Exception e) {
                        handleApiError("Error parsing API response: " + e.getMessage());
                    }
                },
                error -> {
                    progressBarLoading.setVisibility(View.GONE);
                    recyclerViewCurrencies.setVisibility(View.VISIBLE);
                    handleApiError("Network error: " + error.getMessage());
                });
        requestQueue.add(jsonObjectRequest);
    }

    // Hàm này để chọn và sắp xếp các loại tiền tệ muốn hiển thị
    private List<String> filterAndSortCurrencies(List<String> allCodes) {
        List<String> preferredCodes = new ArrayList<>();
        // Thêm các mã muốn hiển thị theo thứ tự
        if (allCodes.contains("VND")) preferredCodes.add("VND");
        if (allCodes.contains("USD")) preferredCodes.add("USD");
        if (allCodes.contains("CNY")) preferredCodes.add("CNY");
        if (allCodes.contains("KRW")) preferredCodes.add("KRW");
        if (allCodes.contains("JPY")) preferredCodes.add("JPY");
        if (allCodes.contains("EUR")) preferredCodes.add("EUR");

        return preferredCodes;
    }


    private void setupDisplayItems(List<String> codesToDisplay) {
        displayItems.clear();
        for (String code : codesToDisplay) {
            if (exchangeRates.containsKey(code)) {
                CurrencyDisplayItem item = new CurrencyDisplayItem(code, exchangeRates.get(code));
                // Logic map code to flag resource name
                displayItems.add(item);
            }
        }

        int initialBasePosition = 0;
        for(int i=0; i<displayItems.size(); i++){
            if(displayItems.get(i).getCode().equals(currentBaseCurrencyCode)){
                initialBasePosition = i;
                break;
            }
        }
        adapter.notifyDataSetChanged();
        adapter.setBaseCurrencyPosition(initialBasePosition);
        adapter.notifyDataSetChanged();
    }

    // Hàm ví dụ để lấy tên file cờ
    private String getFlagResourceNameForCode(String code) {

        switch (code.toUpperCase()) {
            case "VND": return "flag_vn";
            case "USD": return "flag_us";
            case "CNY": return "flag_cn";
            case "KRW": return "flag_kr";
            case "JPY": return "flag_jp";
            case "EUR": return "flag_eu";

            default: return "flag_default";
        }
    }


    private void updateConversions() {
        if (exchangeRates.isEmpty()) return;

        double baseValue;
        try {
            // Xử lý dấu phẩy nếu người dùng nhập kiểu châu Âu
            String cleanInput = currentInputValue.replace(",", ".");
            if (TextUtils.isEmpty(cleanInput) || cleanInput.equals(".")) {
                baseValue = 0.0;
            } else {
                baseValue = Double.parseDouble(cleanInput);
            }
        } catch (NumberFormatException e) {
            Log.e(TAG, "Invalid number format for input: " + currentInputValue);
            // Có thể hiển thị lỗi hoặc không làm gì cả
            // Cập nhật ô base để người dùng thấy giá trị hiện tại
            if (adapter.getBaseCurrencyPosition() >= 0 && adapter.getBaseCurrencyPosition() < displayItems.size()) {
                adapter.updateItemValue(adapter.getBaseCurrencyPosition(), currentInputValue);
            }
            return;
        }


        CurrencyDisplayItem baseItem = displayItems.get(adapter.getBaseCurrencyPosition());
        // Cập nhật giá trị hiển thị cho đồng tiền cơ sở
        adapter.updateItemValue(adapter.getBaseCurrencyPosition(), currentInputValue);


        double baseValueInUSD = baseValue / baseItem.getRateAgainstBase(); // Chuyển giá trị nhập về USD

        for (int i = 0; i < displayItems.size(); i++) {
            if (i == adapter.getBaseCurrencyPosition()) continue; // Bỏ qua chính nó

            CurrencyDisplayItem targetItem = displayItems.get(i);
            double targetValue = baseValueInUSD * targetItem.getRateAgainstBase();
            adapter.updateItemValue(i, numberFormatter.format(targetValue));
        }
    }


    @Override
    public void onCurrencyRowClicked(int position) {
        // Khi một hàng được click, đặt nó làm base currency
        // Adapter đã xử lý việc cập nhật isBaseCurrency và gọi notifyItemChanged
        // Ở đây, chúng ta cần cập nhật currentBaseCurrencyCode và currentInputValue
        currentBaseCurrencyCode = displayItems.get(position).getCode();
        // Lấy giá trị hiện tại của hàng mới được chọn làm base, loại bỏ định dạng
        String rawValue = displayItems.get(position).getDisplayValue().replaceAll("[^\\d,.]", "").replace(",", ".");
        try {
            Double.parseDouble(rawValue); // Kiểm tra xem có phải số không
            currentInputValue = rawValue; // Nếu là số thì gán
        } catch (NumberFormatException e){
            currentInputValue = "0"; // Nếu không phải số hợp lệ, reset về 0
        }

        Log.d(TAG, "Base currency changed to: " + currentBaseCurrencyCode + " with value: " + currentInputValue);

        updateConversions(); // Gọi lại để đảm bảo các ô khác cập nhật theo base mới
    }

    @Override
    public void onBaseCurrencyValueChanged(String newValue) {
        // Hàm này được gọi bởi TextWatcher trong Adapter khi giá trị của ô base thay đổi

    }

    private void setBaseCurrencyByCode(String code) {
        for (int i = 0; i < displayItems.size(); i++) {
            if (displayItems.get(i).getCode().equals(code)) {
                adapter.setBaseCurrencyPosition(i);
                return;
            }
        }
        // Nếu không tìm thấy, đặt item đầu tiên làm base
        if (!displayItems.isEmpty()) {
            adapter.setBaseCurrencyPosition(0);
        }
    }


    private void updateTimestamp(long unixTimestamp) {
        try {
            Date date = new Date(unixTimestamp * 1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
            textViewLastUpdate.setText(sdf.format(date));
        } catch (Exception e) {
            textViewLastUpdate.setText("N/A");
        }
    }

    private void handleApiError(String message) {
        Log.e(TAG, message);
        textViewErrorMessage.setText(message);
        textViewErrorMessage.setVisibility(View.VISIBLE);
        // Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private void setupSaveButtonListener() {
        buttonSaveHistoryTop.setOnClickListener(v -> {
            saveCurrentConversionToHistory();
        });
    }

    private void saveCurrentConversionToHistory() {
        if (displayItems.isEmpty() || exchangeRates.isEmpty()) {
            Toast.makeText(getContext(), "Không có dữ liệu để lưu.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy thông tin từ đồng tiền cơ sở (đang được nhập)
        int basePosition = adapter.getBaseCurrencyPosition();
        if (basePosition < 0 || basePosition >= displayItems.size()) {
            Toast.makeText(getContext(), "Lỗi xác định đồng tiền cơ sở.", Toast.LENGTH_SHORT).show();
            return;
        }

        CurrencyDisplayItem baseCurrencyItem = displayItems.get(basePosition);
        String sourceValueStr = currentInputValue; // Giá trị người dùng đang nhập cho base currency
        String sourceUnit = baseCurrencyItem.getCode();

        // Người dùng có thể muốn lưu chuyển đổi sang MỘT đồng tiền cụ thể khác
        // Thay vì lưu tất cả. Để đơn giản, ta sẽ lưu chuyển đổi từ base sang một đồng tiền "đích" nổi bật
        // Ví dụ: nếu base là VND, đích có thể là USD. Hoặc bạn có thể cho người dùng chọn đích để lưu.

        // --- Lựa chọn 1: Lưu chuyển đổi từ base sang đồng tiền đầu tiên KHÁC base trong danh sách ---
        String resultValueStr = "";
        String resultUnit = "";
        boolean foundTarget = false;

        for (int i = 0; i < displayItems.size(); i++) {
            if (i == basePosition) continue; // Bỏ qua chính nó

            CurrencyDisplayItem targetItem = displayItems.get(i);

            resultValueStr = targetItem.getDisplayValue();
            resultUnit = targetItem.getCode();
            foundTarget = true;

            Log.d(TAG, "Saving conversion: " + sourceValueStr + " " + sourceUnit + " -> " + resultValueStr + " " + resultUnit);
            break; // Lưu chuyển đổi đầu tiên tìm thấy
        }

        if (!foundTarget) {
            Toast.makeText(getContext(), "Không có đồng tiền đích để lưu.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Loại bỏ các ký tự không phải số và dấu thập phân/phẩy để đảm bảo giá trị gốc
        String cleanSourceValue = sourceValueStr.replaceAll("[^\\d.,]", "").replace(",",".");
        String cleanResultValue = resultValueStr.replaceAll("[^\\d.,]", "").replace(",",".");


        if (TextUtils.isEmpty(cleanSourceValue) || TextUtils.isEmpty(cleanResultValue) ||
                cleanSourceValue.equals(".") || cleanResultValue.equals(".")) {
            Toast.makeText(getContext(), getString(R.string.toast_no_valid_data_to_save), Toast.LENGTH_SHORT).show();
            return;
        }


        String conversionType = "Tiền tệ";


        boolean success = dbHelper.insertHistory(conversionType, sourceValueStr, sourceUnit, resultValueStr, resultUnit);

        if (success) {
            Toast.makeText(getContext(), getString(R.string.toast_history_saved), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), getString(R.string.toast_history_save_error), Toast.LENGTH_SHORT).show();
        }
    }
}
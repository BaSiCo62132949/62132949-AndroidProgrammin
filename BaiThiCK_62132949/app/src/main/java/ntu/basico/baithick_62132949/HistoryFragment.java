package ntu.basico.baithick_62132949;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import ntu.basico.baithick_62132949.R;
import ntu.basico.baithick_62132949.HistoryAdapter;
import ntu.basico.baithick_62132949.HistoryDbHelper;
import ntu.basico.baithick_62132949.HistoryItem;

public class HistoryFragment extends Fragment implements HistoryAdapter.OnHistoryItemInteractionListener {

    private static final String TAG = "HistoryFragment";
    private RecyclerView recyclerViewHistory; // Hiển thị danh sách các mục lịch sử
    private TextView textViewNoHistory; // Hiển thị thông báo khi không có lịch sử
    private MaterialButton buttonDeleteAllHistory; // Nút để xóa toàn bộ lịch sử
    private HistoryAdapter historyAdapter; // Adapter để hiển thị danh sách
    private List<HistoryItem> historyItemList; // Danh sách các mục lịch sử
    private HistoryDbHelper dbHelper;     // CSDL để lưu trữ lịch sử



    private SearchView searchViewHistory; // SearchView để tìm kiếm
    private List<HistoryItem> originalHistoryList; // Danh sách gốc để lọc

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        // Khởi tạo, ánh xạ và gọi các hàm
        recyclerViewHistory = view.findViewById(R.id.recyclerViewHistory);
        textViewNoHistory = view.findViewById(R.id.textViewNoHistory);
        buttonDeleteAllHistory = view.findViewById(R.id.buttonDeleteAllHistory);
        searchViewHistory = view.findViewById(R.id.searchViewHistory);

        dbHelper = new HistoryDbHelper(requireContext());
        historyItemList = new ArrayList<>(); // Danh sách hiển thị
        originalHistoryList = new ArrayList<>(); // Danh sách gốc để lọc

        setupRecyclerView(); // cấu hình RecyclerView
        setupDeleteAllButton(); // gắn sự kiện cho nút xóa toàn bộ
        setupSearchView(); // gắn sự kiện cho SearchView

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Khi Fragment trở lại, tải lại dữ liệu từ CSDL
        loadHistoryData();
        Log.d(TAG, "onResume: History data reloaded.");
    }

    // Cấu hình RecyclerView : hiển thị danh sách các mục lịch sử
    private void setupRecyclerView() {
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        historyAdapter = new HistoryAdapter(getContext(), historyItemList, this);
        recyclerViewHistory.setAdapter(historyAdapter);
    }
    // gắn sự kiện cho nút xóa toàn bộ
    private void setupDeleteAllButton() {
        buttonDeleteAllHistory.setOnClickListener(v -> {
            // Kiểm tra xem có dữ liệu lịch sử không
            if (historyItemList == null || historyItemList.isEmpty()) {
                Toast.makeText(getContext(), "Không có lịch sử để xóa.", Toast.LENGTH_SHORT).show();
                return;
            }
            showDeleteAllConfirmationDialog();
        });
    }
    // tải lại dữ liệu từ CSDL, cập nhật UI, và kiểm tra xem có dữ liệu lịch sử không
    private void loadHistoryData() {
        Log.d(TAG, "loadHistoryData: Loading data from database...");
        List<HistoryItem> itemsFromDb = dbHelper.getAllHistoryItems();

        originalHistoryList.clear();// Xóa dữ liệu cũ trước khi thêm mới
        originalHistoryList.addAll(itemsFromDb); // Thêm dữ liệu mới từ CSDL
        Log.d(TAG, "loadHistoryData: Loaded " + originalHistoryList.size() + " original items.");

        String currentQuery = searchViewHistory.getQuery().toString();
        filterHistoryList(currentQuery); // Lọc và cập nhật adapter + UI

        buttonDeleteAllHistory.setEnabled(!originalHistoryList.isEmpty());
    }

    // Xử lý sự kiện khi nút xóa trên một item được nhấn (từ interface)
    @Override
    public void onDeleteClicked(HistoryItem item) {
        Log.d(TAG, "onDeleteClicked: Requesting delete for item ID: " + item.getId());
        showDeleteItemConfirmationDialog(item);
    }

    // Hiển thị dialog xác nhận xóa một mục
    private void showDeleteItemConfirmationDialog(final HistoryItem item) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa mục lịch sử này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    int deletedRows = dbHelper.deleteHistoryItem(item.getId());
                    if (deletedRows > 0) {
                        Toast.makeText(getContext(), "Đã xóa mục lịch sử", Toast.LENGTH_SHORT).show();
                        loadHistoryData(); // Load lại danh sách sau khi xóa
                    } else {
                        Toast.makeText(getContext(), "Lỗi khi xóa mục lịch sử", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    // Hiển thị dialog xác nhận xóa tất cả
    private void showDeleteAllConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận xóa tất cả")
                .setMessage("Bạn có chắc chắn muốn xóa TOÀN BỘ lịch sử chuyển đổi không? Hành động này không thể hoàn tác.")
                .setPositiveButton("Xóa tất cả", (dialog, which) -> {
                    int deletedRows = dbHelper.deleteAllHistory();
                    Toast.makeText(getContext(), "Đã xóa " + deletedRows + " mục lịch sử", Toast.LENGTH_SHORT).show();
                    loadHistoryData(); // Load lại danh sách sau khi xóa
                })
                .setNegativeButton("Hủy", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void setupSearchView() {
        searchViewHistory.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterHistoryList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Người dùng đang gõ - lọc ngay lập tức
                filterHistoryList(newText); // Lọc theo text mới nhập
                return true;
            }
        });
    }

    private void filterHistoryList(String query) {
        Log.d(TAG, "Filtering list with query: '" + query + "'");
        historyItemList.clear(); // Xóa danh sách hiển thị hiện tại

        if (query == null || query.trim().isEmpty()) {
            // Nếu query rỗng, hiển thị toàn bộ danh sách gốc
            historyItemList.addAll(originalHistoryList);
            Log.d(TAG, "Query is empty, showing all " + historyItemList.size() + " items.");
        } else {
            String lowerCaseQuery = query.toLowerCase().trim();

            for (HistoryItem item : originalHistoryList) {

                boolean typeMatch = item.getConversionType().toLowerCase().contains(lowerCaseQuery);
                boolean sourceUnitMatch = item.getSourceUnit().toLowerCase().contains(lowerCaseQuery);
                boolean resultUnitMatch = item.getResultUnit().toLowerCase().contains(lowerCaseQuery);

                if (typeMatch || sourceUnitMatch || resultUnitMatch  ) {
                    historyItemList.add(item);
                }
            }
            Log.d(TAG, "Query applied, found " + historyItemList.size() + " matching items.");
        }

        // Cập nhật RecyclerView
        if (historyAdapter != null) {
            historyAdapter.notifyDataSetChanged();
        } else {
            Log.e(TAG, "HistoryAdapter is null during filtering!");
        }


        // Hiển thị/ẩn thông báo "Không có lịch sử" dựa trên kết quả lọc
        if (historyItemList.isEmpty()) {
            if (originalHistoryList.isEmpty()) {
                textViewNoHistory.setText("Chưa có lịch sử chuyển đổi nào.");
            } else {
                textViewNoHistory.setText("Không tìm thấy kết quả nào khớp với '" + query + "'");
            }
            textViewNoHistory.setVisibility(View.VISIBLE);
            recyclerViewHistory.setVisibility(View.GONE);
        } else {
            textViewNoHistory.setVisibility(View.GONE);
            recyclerViewHistory.setVisibility(View.VISIBLE);
        }
    }

}
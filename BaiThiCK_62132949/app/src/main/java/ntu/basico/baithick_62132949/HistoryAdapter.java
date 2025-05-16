package ntu.basico.baithick_62132949;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ntu.basico.baithick_62132949.R;
import ntu.basico.baithick_62132949.HistoryItem;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<HistoryItem> historyItemList;
    private Context context;
    private OnHistoryItemInteractionListener listener;

    // Interface để Fragment xử lý sự kiện xóa
    public interface OnHistoryItemInteractionListener {
        void onDeleteClicked(HistoryItem item);
    }

    public HistoryAdapter(Context context, List<HistoryItem> historyItemList, OnHistoryItemInteractionListener listener) {
        this.context = context;
        this.historyItemList = historyItemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryItem currentItem = historyItemList.get(position);

        holder.textViewType.setText(currentItem.getConversionType());

        String conversionTextToDisplay; // Biến để lưu chuỗi cuối cùng sẽ hiển thị

        if ("Phép tính".equals(currentItem.getConversionType())) { // Sử dụng equals() cho so sánh chuỗi
            // Hiển thị cho lịch sử phép tính: VD "5+5 = 10"
            // currentItem.getSourceUnit() ở đây là dấu "="
            conversionTextToDisplay = currentItem.getSourceValue() + " " +
                    currentItem.getSourceUnit() + " " +
                    currentItem.getResultValue();
        } else {
            // Hiển thị cho lịch sử chuyển đổi đơn vị/tiền tệ (có định dạng số)
            NumberFormat numberFormatter = NumberFormat.getNumberInstance(Locale.US); // Hoặc Locale.getDefault()
            // Điều chỉnh định dạng tùy theo loại
            if ("Tiền tệ".equals(currentItem.getConversionType())) {
                numberFormatter.setMinimumFractionDigits(2);
                numberFormatter.setMaximumFractionDigits(2);
            } else { // Các loại đơn vị khác
                numberFormatter.setMaximumFractionDigits(6); // Tối đa 6 số lẻ, có thể điều chỉnh
                numberFormatter.setMinimumFractionDigits(0); // Không bắt buộc có số lẻ
            }

            String formattedSourceValue = currentItem.getSourceValue();
            String formattedResultValue = currentItem.getResultValue();

            try {
                // Cố gắng định dạng nếu là số
                double sourceDouble = Double.parseDouble(currentItem.getSourceValue().replace(",", ".")); // Xử lý dấu phẩy nếu có
                formattedSourceValue = numberFormatter.format(sourceDouble);

                double resultDouble = Double.parseDouble(currentItem.getResultValue().replace(",", "."));
                formattedResultValue = numberFormatter.format(resultDouble);
            } catch (NumberFormatException e) {
                // Nếu không phải số (ví dụ: "Lỗi", "Không hợp lệ"), giữ nguyên giá trị gốc
                Log.w("HistoryAdapter", "Could not format history value as number for type: " +
                        currentItem.getConversionType() + ", value: " + currentItem.getSourceValue() + " or " + currentItem.getResultValue());
            }

            conversionTextToDisplay = String.format("%s %s → %s %s",
                    formattedSourceValue,
                    currentItem.getSourceUnit(),
                    formattedResultValue,
                    currentItem.getResultUnit());
        }

        holder.textViewConversion.setText(conversionTextToDisplay); // Gán chuỗi đã xử lý

        // Định dạng timestamp (giữ nguyên)
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.getDefault());
            String formattedDate = sdf.format(new Date(currentItem.getTimestamp()));
            holder.textViewTimestamp.setText(formattedDate);
        } catch (Exception e) {
            holder.textViewTimestamp.setText("N/A");
        }

        // Gán sự kiện click cho nút xóa (giữ nguyên)
        holder.buttonDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClicked(currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyItemList == null ? 0 : historyItemList.size();
    }

    // Hàm để cập nhật dữ liệu cho Adapter
    public void updateData(List<HistoryItem> newItems) {
        historyItemList.clear();
        if (newItems != null) {
            historyItemList.addAll(newItems);
        }
        notifyDataSetChanged(); // Thông báo cho RecyclerView cập nhật
    }

    // ViewHolder class
    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewType;
        TextView textViewConversion;
        TextView textViewTimestamp;
        ImageButton buttonDelete;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewType = itemView.findViewById(R.id.textViewHistoryType);
            textViewConversion = itemView.findViewById(R.id.textViewHistoryConversion);
            textViewTimestamp = itemView.findViewById(R.id.textViewHistoryTimestamp);
            buttonDelete = itemView.findViewById(R.id.buttonDeleteHistoryItem);
        }
    }
}

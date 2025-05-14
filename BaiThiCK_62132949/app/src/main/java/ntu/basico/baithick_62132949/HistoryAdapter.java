package ntu.basico.baithick_62132949;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        // Ghép chuỗi hiển thị chuyển đổi
        String conversionText = String.format("%s %s → %s %s",
                currentItem.getSourceValue(),
                currentItem.getSourceUnit(),
                currentItem.getResultValue(),
                currentItem.getResultUnit());
        holder.textViewConversion.setText(conversionText);

        // Định dạng timestamp
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.getDefault());
            String formattedDate = sdf.format(new Date(currentItem.getTimestamp()));
            holder.textViewTimestamp.setText(formattedDate);
        } catch (Exception e) {
            holder.textViewTimestamp.setText("N/A"); // Hoặc timestamp raw
        }

        // Gán sự kiện click cho nút xóa
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

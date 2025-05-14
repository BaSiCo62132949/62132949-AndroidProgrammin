package ntu.basico.baithick_62132949;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CurrencyRowAdapter extends RecyclerView.Adapter<CurrencyRowAdapter.CurrencyViewHolder> {

    private List<CurrencyDisplayItem> currencyItems;
    private Context context;
    private OnCurrencyInteractionListener listener;
    private int baseCurrencyPosition = 0; // Vị trí của đồng tiền cơ sở

    public interface OnCurrencyInteractionListener {
        void onCurrencyRowClicked(int position); // Khi một hàng được click để làm base
        void onBaseCurrencyValueChanged(String newValue); // Khi giá trị của base currency thay đổi
    }

    public CurrencyRowAdapter(Context context, List<CurrencyDisplayItem> currencyItems, OnCurrencyInteractionListener listener) {
        this.context = context;
        this.currencyItems = currencyItems;
        this.listener = listener;
    }

    public void setBaseCurrencyPosition(int position) {
        if (baseCurrencyPosition != position) {
            // Bỏ trạng thái base cũ
            if (baseCurrencyPosition >= 0 && baseCurrencyPosition < currencyItems.size()) {
                currencyItems.get(baseCurrencyPosition).setBaseCurrency(false);
                notifyItemChanged(baseCurrencyPosition);
            }
            // Đặt trạng thái base mới
            this.baseCurrencyPosition = position;
            if (baseCurrencyPosition >= 0 && baseCurrencyPosition < currencyItems.size()) {
                currencyItems.get(baseCurrencyPosition).setBaseCurrency(true);
                notifyItemChanged(baseCurrencyPosition);
                listener.onCurrencyRowClicked(position); // Thông báo cho fragment
            }
        }
    }

    public int getBaseCurrencyPosition() {
        return baseCurrencyPosition;
    }


    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_currency_row, parent, false);
        return new CurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder holder, int position) {
        CurrencyDisplayItem item = currencyItems.get(position);
        holder.textViewCurrencyCode.setText(item.getCode());
        holder.editTextCurrencyValue.setText(item.getDisplayValue());

        // hieen thi co
        String flagName = item.getFlagResourceName();
        if (flagName != null && !flagName.isEmpty()) {
            try {
                Resources res = context.getResources();
                // Lấy ID của drawable từ tên resource (không có phần mở rộng file)
                int resourceId = res.getIdentifier(flagName, "drawable", context.getPackageName());

                if (resourceId != 0) { // Nếu tìm thấy resource
                    holder.imageViewFlag.setImageResource(resourceId);
                } else {
                    // Không tìm thấy resource, có thể log lỗi và dùng cờ mặc định
                    Log.w("CurrencyRowAdapter", "Flag resource not found: " + flagName + " for currency " + item.getCode());
                    // Thử load cờ mặc định nếu bạn có (ví dụ: flag_default)
                    int defaultFlagResId = res.getIdentifier("flag_default", "drawable", context.getPackageName());
                    if (defaultFlagResId != 0) {
                        holder.imageViewFlag.setImageResource(defaultFlagResId);
                    } else {
                        holder.imageViewFlag.setImageResource(R.mipmap.ic_launcher); // Fallback cuối cùng
                    }
                }
            } catch (Exception e) {
                Log.e("CurrencyRowAdapter", "Error loading flag: " + flagName, e);
                holder.imageViewFlag.setImageResource(R.mipmap.ic_launcher); // Fallback nếu có lỗi
            }
        } else {
            // Không có tên cờ, dùng cờ mặc định hoặc ẩn ImageView
            holder.imageViewFlag.setImageResource(R.mipmap.ic_launcher); // Fallback
        }


        // Xóa TextWatcher cũ để tránh lỗi khi ViewHolder được tái sử dụng
        if (holder.textWatcher != null) {
            holder.editTextCurrencyValue.removeTextChangedListener(holder.textWatcher);
        }

        if (position == baseCurrencyPosition) {
            // Đây là đồng tiền cơ sở, cho phép chỉnh sửa và làm nổi bật
            holder.editTextCurrencyValue.setTypeface(null, Typeface.BOLD);
            holder.editTextCurrencyValue.setBackgroundResource(R.drawable.currency_value_background_focused); // Tạo drawable này
            // Gắn TextWatcher CHỈ cho ô base currency
            holder.textWatcher = new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (holder.getAdapterPosition() == baseCurrencyPosition) { // Chỉ xử lý nếu vẫn là base
                        listener.onBaseCurrencyValueChanged(s.toString());
                    }
                }
                @Override public void afterTextChanged(Editable s) {}
            };
            holder.editTextCurrencyValue.addTextChangedListener(holder.textWatcher);
        } else {
            // Các đồng tiền khác, không cho sửa, kiểu chữ thường
            holder.editTextCurrencyValue.setTypeface(null, Typeface.NORMAL);
            holder.editTextCurrencyValue.setBackgroundResource(R.drawable.currency_value_background);
        }

        // Xử lý click vào một hàng để đặt nó làm base currency
        holder.itemView.setOnClickListener(v -> {
            setBaseCurrencyPosition(holder.getAdapterPosition());
        });
        // Xử lý click vào EditText để đặt nó làm base (nếu cần)
        holder.editTextCurrencyValue.setOnClickListener(v -> {
            setBaseCurrencyPosition(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return currencyItems.size();
    }

    public void updateCurrencyValues(List<CurrencyDisplayItem> newItems) {
        this.currencyItems.clear();
        this.currencyItems.addAll(newItems);
        notifyDataSetChanged();
    }

    // Cập nhật giá trị của một item cụ thể và chỉ refresh item đó
    public void updateItemValue(int position, String newValue) {
        if (position >= 0 && position < currencyItems.size()) {
            currencyItems.get(position).setDisplayValue(newValue);
            notifyItemChanged(position, "VALUE_UPDATE"); // Chỉ cập nhật payload này để tránh redraw toàn bộ
        }
    }


    static class CurrencyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewFlag;
        TextView textViewCurrencyCode;
        EditText editTextCurrencyValue;
        TextWatcher textWatcher; // Giữ tham chiếu đến TextWatcher

        public CurrencyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFlag = itemView.findViewById(R.id.imageViewFlag);
            textViewCurrencyCode = itemView.findViewById(R.id.textViewCurrencyCode);
            editTextCurrencyValue = itemView.findViewById(R.id.editTextCurrencyValue);
        }
    }
}
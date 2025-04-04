package ntu.basico.ontap1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MonAnAdapter extends RecyclerView.Adapter<MonAnAdapter.MonAnViewHolder> {

    private Context context;
    private ArrayList<MonAn> monAnList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Constructor
    public MonAnAdapter(Context context, ArrayList<MonAn> monAnList) {
        this.context = context;
        this.monAnList = monAnList;
    }

    // Tạo ViewHolder mới (được gọi bởi LayoutManager)
    @NonNull
    @Override
    public MonAnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout cho một item từ XML
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_monan, parent, false);
        return new MonAnViewHolder(view, listener); // Truyền listener vào ViewHolder
    }

    // Gắn dữ liệu vào ViewHolder (được gọi bởi LayoutManager)
    @Override
    public void onBindViewHolder(@NonNull MonAnViewHolder holder, int position) {
        MonAn currentMonAn = monAnList.get(position);

        // Lấy dữ liệu từ object MonAn
        String ten = currentMonAn.getTenMonAn();
        String moTa = currentMonAn.getMoTa();
        int hinhAnh = currentMonAn.getHinhAnhResource();

        // Gắn dữ liệu lên các View trong ViewHolder
        holder.textViewTen.setText(ten);
        holder.textViewMoTa.setText(moTa);
        holder.imageViewHinh.setImageResource(hinhAnh);
    }

    // Trả về số lượng item trong danh sách (được gọi bởi LayoutManager)
    @Override
    public int getItemCount() {
        return monAnList.size();
    }

    // Lớp ViewHolder
    public static class MonAnViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewHinh;
        TextView textViewTen;
        TextView textViewMoTa;

        public MonAnViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            // Ánh xạ các View từ layout item
            imageViewHinh = itemView.findViewById(R.id.imageViewMonAn);
            textViewTen = itemView.findViewById(R.id.textViewTenMonAn);
            textViewMoTa = itemView.findViewById(R.id.textViewMoTaMonAn);

            // Thiết lập sự kiện click cho toàn bộ item view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition(); // Lấy vị trí của item được click
                        if (position != RecyclerView.NO_POSITION) { // Đảm bảo vị trí hợp lệ
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
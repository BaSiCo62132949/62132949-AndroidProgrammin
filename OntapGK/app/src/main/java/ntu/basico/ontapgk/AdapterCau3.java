package ntu.basico.ontapgk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdapterCau3 extends RecyclerView.Adapter<AdapterCau3.ViewHolder> {

    private List<DataObject> dataList;
    private OnItemClickListener listener;

    public AdapterCau3(List<DataObject> dataList) {
        this.dataList = dataList;
    }

    public interface OnItemClickListener {
        void onItemClick(DataObject data);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataObject data = dataList.get(position);
        holder.textTitle.setText(data.getTitle());
        holder.textDescription.setText(data.getDescription());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        TextView textDescription;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(android.R.id.text1);
            textDescription = itemView.findViewById(android.R.id.text2);
        }
    }
}
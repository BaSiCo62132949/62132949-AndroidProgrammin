package ntu.basico.ontapgk;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Cau3 extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterCau3 adapter;
    private List<DataObject> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cau3);

        recyclerView = findViewById(R.id.recycler_view_cau3);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dataList = new ArrayList<>();
        dataList.add(new DataObject("Item 1", "Description 1"));
        dataList.add(new DataObject("Item 2", "Description 2"));
        dataList.add(new DataObject("Item 3", "Description 3"));

        adapter = new AdapterCau3(dataList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(data -> {
            Toast.makeText(this, "Click: " + data.getTitle(), Toast.LENGTH_SHORT).show();
                }

        );


    }
}
package ntu.basico.ontapgk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class Cau2 extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> dataList;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cau2);
        listView = findViewById(R.id.ListView);
        dataList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            dataList.add("Item " + i);
        }
        dataList.add("Bai hat 101");
        dataList.add("Bai hat 102");
        dataList.add("Bai hat 103");
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = dataList.get(position);
                Toast.makeText(Cau2.this, "Selected item: " + selectedItem, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Cau2.this, ItemActivity.class);
                intent.putExtra("data", dataList.get(position));
                startActivity(intent);
            }
        });
    }
}
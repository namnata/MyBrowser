package com.example.mybrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class History extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ImageButton btnGoHome;
    ArrayAdapter<String> itemAdapter;
    ListView listView;
    ArrayList<String> history = new ArrayList<>();
    ImageButton btnRemoveHistory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        btnGoHome = (ImageButton) findViewById(R.id.imageButtonHome);
        btnRemoveHistory = (ImageButton) findViewById(R.id.imageButtonRemoveHistory);


        // take data from intent
        Intent intent = getIntent();
        Bundle historyBundle = getIntent().getExtras();
        if (historyBundle == null) {
        } else {
            history = historyBundle.getStringArrayList("History_URL");

            itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, history);
            listView = (ListView) findViewById(R.id.listViewHistory);
            listView.setAdapter(itemAdapter);
            listView.setOnItemClickListener((AdapterView.OnItemClickListener) this);
        }

        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                Intent intentBackToMain = new Intent(History.this, MainActivity.class);
//                startActivity(intentBackToMain);
            }
        });

        btnRemoveHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("History", MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();
                history.clear();
                itemAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String url = parent.getItemAtPosition(position).toString();
        Intent openUrlIntent = new Intent(History.this, MainActivity.class);
        Intent intent = openUrlIntent.putExtra("URL_His",url); //pass url to Main through intent
        startActivity(openUrlIntent);

    }
}

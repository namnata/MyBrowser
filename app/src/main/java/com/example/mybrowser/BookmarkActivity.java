package com.example.mybrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class BookmarkActivity extends AppCompatActivity {

    ImageButton btnGoHome, btnRemoveBookmarks;
    ArrayList<String> bookmarks = new ArrayList<>();
    ArrayAdapter<String> itemAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        btnGoHome = (ImageButton) findViewById(R.id.imageButtonHome);
        btnRemoveBookmarks = (ImageButton) findViewById(R.id.imageButtonRemoveBookmarks);

        loadBookmarks();


        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBackToMain = new Intent(BookmarkActivity.this, MainActivity.class);
                startActivity(intentBackToMain);
            }

        });

        btnRemoveBookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("Bookmarks", MODE_PRIVATE);
                sharedPreferences.edit().clear().apply();
                bookmarks.clear();
                itemAdapter.notifyDataSetChanged();
            }
        });
    }

    private void loadBookmarks() {

        SharedPreferences bookmarkPrefers = getSharedPreferences("BOOKMARKS", Context.MODE_PRIVATE);
        String json = bookmarkPrefers.getString("collections", null);


            Gson gson = new Gson();
            bookmarks = gson.fromJson(json, new TypeToken<ArrayList<String>>() {
            }.getType());


            }
        }










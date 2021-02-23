package com.example.mybrowser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    ImageButton btnBack, btnForward, btnHome, btnMore, btnSearch, btnBookmark, btnHistory, btnBookmarksCollection;
    EditText editTextUrl;
    WebView webView;
    //    Button btnRefresh;
    List<BookmarkItem> bookmarks = new ArrayList<>();
    ListAdapter listAdapter;


    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = (ImageButton) findViewById(R.id.btnSearch);
        btnBack = (ImageButton) findViewById(R.id.imageButtonBack);
        btnForward = (ImageButton) findViewById(R.id.imageButtonForward);
        btnHome = (ImageButton) findViewById(R.id.imageButtonHome);
        btnMore = (ImageButton) findViewById(R.id.imageButtonMore);
        btnBookmark = (ImageButton) findViewById(R.id.imageButtonBookmark);
        btnHistory = (ImageButton) findViewById(R.id.imageButtonHistory);
//        btnRefresh = (Button)  findViewById(R.id.btnRefresh);
        editTextUrl = (EditText) findViewById(R.id.editTextUrl);
        btnBookmarksCollection = (ImageButton) findViewById(R.id.imageButtonCollections);


        webView = (WebView) findViewById(R.id.webView);


        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState);
            String savedurl = savedInstanceState.getString("currentUrl");
            webView.loadUrl(savedurl);

        } else {

            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setSupportZoom(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setDisplayZoomControls(false);
            webSettings.setUseWideViewPort(true);
            webSettings.setSupportMultipleWindows(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setSupportMultipleWindows(true);


            webView.setWebViewClient(new WebViewClient());


            webView.loadUrl("https://www.google.com");
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

            getHistory();


            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(editTextUrl.getWindowToken(), 0);
                    webView.loadUrl("https://" + editTextUrl.getText().toString());
                    editTextUrl.setText("");
                }

            });
        }


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                webView.loadUrl("https://www.google.com");
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();

                }
            }
        });

        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoForward()) {
                    webView.goForward();
                }
            }
        });


        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
                popupMenu.setOnMenuItemClickListener(MainActivity.this);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.show();
            }
        });


        btnBookmarksCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                backupCurrentPage();
                Intent intentGoBookMark = new Intent(MainActivity.this, BookmarkActivity.class);
                startActivity(intentGoBookMark);
            }
        });


        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WebBackForwardList BackForwardList = webView.copyBackForwardList();
                int currentSize = BackForwardList.getSize();
                ArrayList<String> HistoryLog = new ArrayList<>();
                for (int i = 0; i < currentSize; ++i) {
                    String url = BackForwardList.getItemAtIndex(i).getUrl();
                    String title = BackForwardList.getItemAtIndex(i).getTitle();

                    HistoryLog.add(url);
                }
                if (HistoryLog.size() != 0) {

                    //save currentURL to shared preference
                    backupCurrentPage();
                    //send history data using intent to HistoryActivity
                    Intent intentToHistory = new Intent(MainActivity.this, History.class);
                    Intent intent = intentToHistory.putStringArrayListExtra("History_URL", HistoryLog);
                    startActivity(intentToHistory);
                } else {

                }
            }


            private void backupCurrentPage() {
                SharedPreferences currentSP = getSharedPreferences("CurrentPage", MODE_PRIVATE);
                SharedPreferences.Editor ed = currentSP.edit();
                ed.putString("currentKey", webView.getUrl());
                ed.apply();
            }


        });

        btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookmarkItem bookmark = new BookmarkItem(webView.getTitle(), webView.getUrl());
                SharedPreferences collectionPrefers = getSharedPreferences("BOOKMARKS", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor;
                String list = collectionPrefers.getString("collections", null);


                //load bookmark data

                Gson gsonExist = new Gson();
                Type type = new TypeToken<List<BookmarkItem>>() {
                }.getType();
                bookmarks = gsonExist.fromJson(list, type);
                int size = bookmarks.size();
                for (int i = 0; i < size; i++) {
                    Log.d("TestAddBookmark", "Index " + Integer.toString(i) + bookmarks.get(i).getTitle());
                    if (bookmarks.get(i).getTitle().equals(bookmark.getTitle()) && bookmarks.get(i).getUrl().equals(bookmark.getUrl())) {

                        return;
                    }
                }


                if (size > 50) {

                    return;
                }
            }
//            editor = collectionPrefers.edit();
//                bookmarks.add(star);
//            Gson gson = new Gson();
//            String json = gson.toJson(bookmarks);
//                editor.putString("collections", json);
//                editor.apply();


        });
    }

    private void getHistory() {
        Intent intent = new Intent();
        String urlHistory = getIntent().getStringExtra("URL_His");
        webView.loadUrl(urlHistory);

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}














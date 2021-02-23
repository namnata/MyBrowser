package com.example.mybrowser;

public class BookmarkItem {

    String title;
    String url;

    public BookmarkItem(String title, String url){
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

}

package com.example.final_project.Listener;

import com.example.final_project.Model.NewsHeadlines;

import java.util.List;

public interface OnFetchDataListener<N> {
    void onFetchData(List<NewsHeadlines> list, String message);
    void onError(String message);
}

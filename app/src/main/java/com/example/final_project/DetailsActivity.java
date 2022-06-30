package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final_project.Model.NewsHeadlines;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    NewsHeadlines headlines;
    TextView url_tv, details_title_tv, details_author_tv, details_time_tv, details_detail, details_content;
    ImageView details_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        details_title_tv = findViewById(R.id.details_title_tv);
        details_author_tv = findViewById(R.id.details_author_tv);
        details_time_tv = findViewById(R.id.details_time_tv);
        details_detail = findViewById(R.id.details_detail);
        details_content = findViewById(R.id.details_content);
        url_tv = findViewById(R.id.url);

        details_image = findViewById(R.id.details_image);

        headlines = (NewsHeadlines) getIntent().getSerializableExtra("data");

        details_title_tv.setText(headlines.getTitle());
        details_author_tv.setText("By " + headlines.getAuthor());
        details_time_tv.setText(headlines.getPublishedAt());
        details_detail.setText(headlines.getDescription());
        details_content.setText(headlines.getContent());
        url_tv.setText("Read more.." + headlines.getUrl());
        Picasso.get().load(headlines.getUrlToImage()).into(details_image);
    }
}
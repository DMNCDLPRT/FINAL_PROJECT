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
    TextView details_title_tv, details_author_tv, details_time_tv, details_detail, details_content;
    ImageView details_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        details_title_tv = findViewById(R.id.text_title);
        details_author_tv = findViewById(R.id.details_author_tv);
        details_time_tv = findViewById(R.id.details_time_tv);
        details_detail = findViewById(R.id.details_detail);
        details_content = findViewById(R.id.details_content);

        details_image = findViewById(R.id.details_image);

        headlines = (NewsHeadlines) getIntent().getSerializableExtra("data");

        String headlines = getIntent().getStringExtra("data");

        Toast.makeText(this, headlines, Toast.LENGTH_SHORT).show();
        /*Toast.makeText(this, "qweqweqweqwe", Toast.LENGTH_SHORT).show();*/
        /*details_title_tv.setText(headlines.getTitle());
        details_author_tv.setText(headlines.getAuthor());
        details_time_tv.setText(headlines.getPublishedAt());
        details_detail.setText(headlines.getDescription());
        details_content.setText(headlines.getContent());
        Picasso.get().load(headlines.getUrlToImage()).into(details_image);*/


    }
}
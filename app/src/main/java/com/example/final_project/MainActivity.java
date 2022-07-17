package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final_project.Listener.OnFetchDataListener;
import com.example.final_project.Listener.SelectListener;
import com.example.final_project.Model.NewsApiResponse;
import com.example.final_project.Model.NewsHeadlines;
import com.example.final_project.Model.Source;
import com.example.final_project.databinding.HeadlinesListItemsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SelectListener, View.OnClickListener{

    RecyclerView recyclerView;
    CustomAdapter adapter;
    ProgressDialog dialog;

    Button business, entertainment, general, health, science, sports, technology;
    SearchView searchView;
    TextView user_name_tv;

    Button logout_button;
    Source source;
    NewsHeadlines newsHeadlines;
    String email;

    FirebaseAuth mAuth;
    boolean isInMyFavourites = false;

    HeadlinesListItemsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        user_name_tv = findViewById(R.id.user_name_tv);
        logout_button = findViewById(R.id.logout_button);

        logout_button.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dialog.setTitle("Fetching News Articles of " + query);
                dialog.show();
                RequestManager manager = new RequestManager(MainActivity.this);
                manager.getNewsHeadlines(listener, "general", query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        dialog = new ProgressDialog(this);
        dialog.setTitle("Fetching new articles. Please wait...");
        dialog.show();

        business = findViewById(R.id.btn_business);
        business.setOnClickListener(this);

        entertainment = findViewById(R.id.btn_entertainment);
        entertainment.setOnClickListener(this);

        general = findViewById(R.id.btn_general);
        general.setOnClickListener(this);

        health = findViewById(R.id.btn_health);
        health.setOnClickListener(this);

        science = findViewById(R.id.btn_science);
        science.setOnClickListener(this);

        sports = findViewById(R.id.btn_sports);
        sports.setOnClickListener(this);

        technology = findViewById(R.id.btn_technology);
        technology.setOnClickListener(this);

        RequestManager manager = new RequestManager(this);
        manager.getNewsHeadlines(listener, "general", null);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else {
            email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();

            user_name_tv.setText(currentUser.getEmail());
            user_name_tv.setOnClickListener(view ->
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class).putExtra("email", email)));
        }
    }

    private final OnFetchDataListener<NewsApiResponse> listener = new OnFetchDataListener<NewsApiResponse>() {
        @Override
        public void onFetchData(List<NewsHeadlines> list, String message) {
            if (list.isEmpty()){
                Toast.makeText(MainActivity.this, "No data found.", Toast.LENGTH_SHORT).show();
            } else {
                showNews(list);
                dialog.dismiss();
            }
        }

        @Override
        public void onError(String message) {
            Toast.makeText(MainActivity.this, "An Error Occurred. ", Toast.LENGTH_SHORT).show();
        }
    };

    private void showNews(List<NewsHeadlines> list) {
        recyclerView = findViewById(R.id.recycler_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new CustomAdapter(this, list, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void OnNewsClick(NewsHeadlines headlines) {
        startActivity(new Intent(MainActivity.this, DetailsActivity.class)
                .putExtra("data", headlines));
    }

    @Override
    public void onClick(View view) {
        Button button = (Button) view;
        String category = button.getText().toString();

        dialog.setTitle("Fetching news articles of " + category);
        dialog.show();

        RequestManager manager = new RequestManager(this);
        manager.getNewsHeadlines(listener, category, null);
    }

    public void checkFavorites(String id) {
        binding = HeadlinesListItemsBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(Objects.requireNonNull(mAuth.getUid())).child("favourites").child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        isInMyFavourites = snapshot.exists();
                        if (isInMyFavourites) {
                            binding.bookmarkTV.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_baseline_bookmark_24, 0, 0);
                            binding.bookmarkTV.setText("Remove to my Bookmark");
                        } else {
                            binding.bookmarkTV.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_baseline_bookmark_border_24, 0, 0);
                            binding.bookmarkTV.setText("Add to my Bookmark");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.bookmarkTV.setOnClickListener(view -> {

            if (isInMyFavourites) {
                removeToFavourite(MainActivity.this, source.getId());

            } else {
                addToFavourite(MainActivity.this, source.getId());
            }
        });
    }

    public void addToFavourite(Context context, String id) {
        long timestamp = System.currentTimeMillis();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", "" + source.getId());
        hashMap.put("url", "" + newsHeadlines.getUrl());
        hashMap.put("timestamp", ""+timestamp);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(Objects.requireNonNull(mAuth.getUid())).child("Favorites").child(id)
                .setValue(hashMap)
                .addOnCompleteListener(task -> Toast.makeText(context, "Added to your wishlist...", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to add due to " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public void removeToFavourite(Context context, String id) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(Objects.requireNonNull(mAuth.getUid())).child("Favorites").child(id)
                .removeValue()
                .addOnCompleteListener(task -> Toast.makeText(context, "Removed from your wishlist...", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to remove due to " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
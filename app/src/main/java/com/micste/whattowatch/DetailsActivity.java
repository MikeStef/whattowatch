package com.micste.whattowatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ImageView backdropImage = findViewById(R.id.backdropImage);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String backdropPath = extras.getString("EXTRA_BACKDROP_URL");
            String imageUrl = "https://image.tmdb.org/t/p/w780" + backdropPath;
            Glide.with(this).load(imageUrl).into(backdropImage);
        }
    }
}

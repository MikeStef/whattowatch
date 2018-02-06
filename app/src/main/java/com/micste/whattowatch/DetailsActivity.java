package com.micste.whattowatch;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.micste.whattowatch.model.MovieDetailsResponse;
import com.micste.whattowatch.netcom.ApiService;
import com.micste.whattowatch.utils.ApiHelper;
import com.micste.whattowatch.utils.SnackBarHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    private ApiService apiService;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        ImageView backdropImage = findViewById(R.id.backdropImage);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        apiService = ApiHelper.getApiService();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String backdropPath = extras.getString("EXTRA_BACKDROP_URL");
            String imageUrl = "https://image.tmdb.org/t/p/w780" + backdropPath;
            Glide.with(this).load(imageUrl).into(backdropImage);

            int mediaId = extras.getInt("EXTRA_MEDIA_ID");
            getDetails(mediaId);
        }

    }

    private void getDetails(int mediaId) {
        apiService.getMovieDetails(mediaId).enqueue(new Callback<MovieDetailsResponse>() {
            @Override
            public void onResponse(Call<MovieDetailsResponse> call, Response<MovieDetailsResponse> response) {
                if (response.isSuccessful()) {
                    MovieDetailsResponse movieDetailsResponse = response.body();
                    setTitle(movieDetailsResponse.getTitle());
                } else {
                    SnackBarHelper.showSnackBarMessage(coordinatorLayout, getString(R.string.generic_network_error));
                }
            }

            @Override
            public void onFailure(Call<MovieDetailsResponse> call, Throwable t) {
                SnackBarHelper.showSnackBarMessage(coordinatorLayout, getString(R.string.generic_network_error));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}

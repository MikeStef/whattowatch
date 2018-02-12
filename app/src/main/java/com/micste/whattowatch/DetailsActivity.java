package com.micste.whattowatch;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.micste.whattowatch.db.DatabaseHandler;
import com.micste.whattowatch.model.MovieDetailsResponse;
import com.micste.whattowatch.model.MovieLight;
import com.micste.whattowatch.netcom.ApiService;
import com.micste.whattowatch.utils.ApiHelper;
import com.micste.whattowatch.utils.SnackBarHelper;
import com.micste.whattowatch.utils.StringHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    private ApiService apiService;
    private CoordinatorLayout coordinatorLayout;
    private TextView overviewText, runtimeText, releaseDateText,
            ratingText, languageText, budgetText;
    private ProgressBar progressBar;
    private ImageView backdropImage;
    private MovieDetailsResponse movieDetails;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        overviewText = findViewById(R.id.overviewText);
        runtimeText = findViewById(R.id.runtimeText);
        releaseDateText = findViewById(R.id.releaseDateText);
        ratingText = findViewById(R.id.ratingText);
        languageText = findViewById(R.id.languageText);
        budgetText = findViewById(R.id.budgetText);
        progressBar = findViewById(R.id.progressBar);
        backdropImage = findViewById(R.id.backdropImage);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        apiService = ApiHelper.getApiService();
        databaseHandler = new DatabaseHandler(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString("EXTRA_MEDIA_TITLE");
            setTitle(title);

            int mediaId = extras.getInt("EXTRA_MEDIA_ID");
            getDetails(mediaId);
        }

    }

    private void getDetails(int mediaId) {
        progressBar.setVisibility(View.VISIBLE);

        apiService.getMovieDetails(mediaId).enqueue(new Callback<MovieDetailsResponse>() {
            @Override
            public void onResponse(Call<MovieDetailsResponse> call, Response<MovieDetailsResponse> response) {
                if (response.isSuccessful()) {
                    movieDetails = response.body();
                    setupDetails(movieDetails);
                } else {
                    SnackBarHelper.showSnackBarMessage(coordinatorLayout, getString(R.string.generic_network_error));
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MovieDetailsResponse> call, Throwable t) {
                SnackBarHelper.showSnackBarMessage(coordinatorLayout, getString(R.string.generic_network_error));
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setupDetails(MovieDetailsResponse movieDetails) {
        String backdropPath = movieDetails.getBackdropPath();
        String imageUrl = "https://image.tmdb.org/t/p/w780" + backdropPath;
        Glide.with(this).load(imageUrl).into(backdropImage);

        runtimeText.setText(StringHelper.parseRuntime(movieDetails.getRuntime(), this));
        releaseDateText.setText(movieDetails.getReleaseDate());
        ratingText.setText(String.valueOf(movieDetails.getVoteAverage()));
        languageText.setText(movieDetails.getOriginalLanguage());

        if (movieDetails.getBudget() == 0 || movieDetails.getBudget() == null) {
            budgetText.setText(getString(R.string.not_available));
        } else {
            budgetText.setText(String.valueOf(movieDetails.getBudget()));
        }

        overviewText.setText(movieDetails.getOverview());


    }

    private void addToWatchList() {
        if (movieDetails != null) {

            MovieLight movie = new MovieLight();
            movie.setMovieId(movieDetails.getId());
            movie.setPosterPath(movieDetails.getPosterPath());
            movie.setReleaseDate(movieDetails.getReleaseDate());
            movie.setTitle(movieDetails.getTitle());
            movie.setVoteAverage(String.valueOf(movieDetails.getVoteAverage()));

            if (databaseHandler.checkIfMovieExists(String.valueOf(movie.getMovieId()))) {
                SnackBarHelper.showSnackBarMessage(coordinatorLayout, getString(R.string.movie_already_added));
            } else {
                databaseHandler.addMovie(movie);
                SnackBarHelper.showSnackBarMessage(coordinatorLayout, getString(R.string.added_to_list));
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            addToWatchList();
        }

        return super.onOptionsItemSelected(item);
    }
}

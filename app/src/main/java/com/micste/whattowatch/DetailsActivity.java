package com.micste.whattowatch;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    private ApiService apiService;
    private CoordinatorLayout coordinatorLayout;
    private TextView overviewText, runtimeText, releaseDateText, titleText,
            ratingText, languageText, budgetText, genresText;
    private ProgressBar progressBar;
    private ImageView backdropImage, posterImage;
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
        genresText = findViewById(R.id.genresText);
        progressBar = findViewById(R.id.progressBar);
        backdropImage = findViewById(R.id.backdropImage);
        posterImage = findViewById(R.id.details_poster);
        titleText = findViewById(R.id.details_title);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");

        apiService = ApiHelper.getApiService();
        databaseHandler = new DatabaseHandler(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString("EXTRA_MEDIA_TITLE");
            titleText.setText(title);

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
        String backdropImageUrl = "https://image.tmdb.org/t/p/w780" + movieDetails.getBackdropPath();
        Glide.with(this).load(backdropImageUrl).into(backdropImage);

        String posterImageUrl = "https://image.tmdb.org/t/p/w342" + movieDetails.getPosterPath();
        Glide.with(this).load(posterImageUrl).into(posterImage);

        runtimeText.setText(StringHelper.parseRuntime(movieDetails.getRuntime(), this));
        releaseDateText.setText(movieDetails.getReleaseDate());
        ratingText.setText(StringHelper.appendRating(String.valueOf(movieDetails.getVoteAverage())));
        languageText.setText(movieDetails.getOriginalLanguage());
        genresText.setText(TextUtils.join(", ", movieDetails.getGenres()));

        if (movieDetails.getBudget() == 0 || movieDetails.getBudget() == null) {
            budgetText.setText(getString(R.string.not_available));
        } else {
            budgetText.setText(StringHelper.formatBudgetNumbers(movieDetails.getBudget()));
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

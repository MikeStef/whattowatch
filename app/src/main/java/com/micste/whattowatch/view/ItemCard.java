package com.micste.whattowatch.view;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.micste.whattowatch.DetailsActivity;
import com.micste.whattowatch.R;
import com.micste.whattowatch.db.DatabaseHandler;
import com.micste.whattowatch.model.MovieLight;
import com.micste.whattowatch.model.Result;
import com.micste.whattowatch.utils.SnackBarHelper;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

@NonReusable
@Layout(R.layout.item_card_view)
public class ItemCard {

    @View(R.id.posterImageView)
    private ImageView posterImageView;

    @View(R.id.titleText)
    private TextView title;

    @View(R.id.voteAverageText)
    private TextView voteAverage;

    private Result result;
    private Context context;
    private SwipePlaceHolderView swipeView;
    private DatabaseHandler databaseHandler;

    public ItemCard(Context context, Result result, SwipePlaceHolderView swipeView) {
        this.context = context;
        this.result = result;
        this.swipeView = swipeView;
        this.databaseHandler = new DatabaseHandler(context);
    }

    @Resolve
    private void onResolved() {
        String imageurl = "https://image.tmdb.org/t/p/w342" + result.getPosterPath();
        Glide.with(context).load(imageurl).into(posterImageView);
        title.setText(result.getTitle());
        voteAverage.setText(String.valueOf(result.getVoteAverage()));
    }

    @SwipeOut
    private void onSwipedOut() {

    }

    @SwipeCancelState
    private void onSwipeCancelState() {
    }

    @SwipeIn
    private void onSwipeIn() {
        if (result != null) {

            MovieLight movie = new MovieLight();
            movie.setMovieId(result.getId());
            movie.setPosterPath(result.getPosterPath());
            movie.setReleaseDate(result.getReleaseDate());
            movie.setTitle(result.getTitle());
            movie.setVoteAverage(String.valueOf(result.getVoteAverage()));

            if (!databaseHandler.checkIfMovieExists(String.valueOf(movie.getMovieId()))) {
                databaseHandler.addMovie(movie);
            }
        }
    }

    @SwipeInState
    private void onSwipeInState() {
    }

    @SwipeOutState
    private void onSwipeOutState() {
    }

    @Click(R.id.cardView)
    private void onItemClick() {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("EXTRA_MEDIA_ID", result.getId());
        intent.putExtra("EXTRA_MEDIA_TITLE", result.getTitle());
        context.startActivity(intent);
    }
}

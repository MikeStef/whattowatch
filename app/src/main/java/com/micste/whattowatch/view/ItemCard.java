package com.micste.whattowatch.view;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.micste.whattowatch.DetailsActivity;
import com.micste.whattowatch.R;
import com.micste.whattowatch.model.Result;
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

    public ItemCard(Context context, Result result, SwipePlaceHolderView swipeView) {
        this.context = context;
        this.result = result;
        this.swipeView = swipeView;
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
        Log.d("EVENT", "onSwipedOut");
        swipeView.addView(this);
    }

    @SwipeCancelState
    private void onSwipeCancelState() {
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn() {
        Log.d("EVENT", "onSwipedIn");
    }

    @SwipeInState
    private void onSwipeInState() {
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState() {
        Log.d("EVENT", "onSwipeOutState");
    }

    @Click(R.id.cardView)
    private void onItemClick() {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("EXTRA_BACKDROP_URL", result.getBackdropPath());
        context.startActivity(intent);
    }
}

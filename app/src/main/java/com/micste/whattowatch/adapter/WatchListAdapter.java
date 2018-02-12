package com.micste.whattowatch.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.micste.whattowatch.DetailsActivity;
import com.micste.whattowatch.R;
import com.micste.whattowatch.model.MovieLight;

import java.util.List;

public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.MyViewHolder> {

    private Context context;
    private List<MovieLight> list;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, rating, releaseDate;
        ImageView thumbnail;
        CardView cardView;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.card_title);
            rating = view.findViewById(R.id.card_rating);
            releaseDate = view.findViewById(R.id.card_release_date);
            thumbnail = view.findViewById(R.id.thumbnail);
            cardView = view.findViewById(R.id.watchlist_card);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    goToDetailsActivity(list.get(pos).getMovieId(), list.get(pos).getTitle());
                }
            });
        }
    }

    public WatchListAdapter(Context context, List<MovieLight> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.watchlist_item_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final MovieLight movie = list.get(position);
        holder.title.setText(movie.getTitle());
        holder.rating.setText(movie.getVoteAverage());
        holder.releaseDate.setText(movie.getReleaseDate());

        String imageUrl = "https://image.tmdb.org/t/p/w342" + movie.getPosterPath();
        Glide.with(context).load(imageUrl).into(holder.thumbnail);
    }

    private void goToDetailsActivity(int id, String title) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("EXTRA_MEDIA_ID", id);
        intent.putExtra("EXTRA_MEDIA_TITLE", title);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

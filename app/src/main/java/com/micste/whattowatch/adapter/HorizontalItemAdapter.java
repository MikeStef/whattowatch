package com.micste.whattowatch.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.micste.whattowatch.DetailsActivity;
import com.micste.whattowatch.R;
import com.micste.whattowatch.model.Result;

import java.util.List;

public class HorizontalItemAdapter extends RecyclerView.Adapter<HorizontalItemAdapter.MyViewHolder> {

    private Context context;
    private List<Result> list;

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title;
        LinearLayout linearLayout;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.item_title);
            poster = view.findViewById(R.id.item_poster);
            linearLayout = view.findViewById(R.id.item_linearlayout);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    goToDetailsActivity(list.get(pos).getId(), list.get(pos).getTitle());
                }
            });
        }
    }

    public HorizontalItemAdapter(Context context, List<Result> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Result result = list.get(position);
        holder.title.setText(result.getTitle());

        String imageUrl = "https://image.tmdb.org/t/p/w342" + result.getPosterPath();
        Glide.with(context).load(imageUrl).into(holder.poster);
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


package com.micste.whattowatch.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.micste.whattowatch.R;
import com.micste.whattowatch.adapter.WatchListAdapter;
import com.micste.whattowatch.db.DatabaseHandler;
import com.micste.whattowatch.model.MovieLight;

import java.util.ArrayList;
import java.util.List;

public class WatchListFragment extends Fragment {

    private List<MovieLight> movieList;
    private WatchListAdapter adapter;
    private RecyclerView recyclerView;
    private DatabaseHandler databaseHandler;


    public WatchListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watch_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movieList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.watchlist_recyclerview);
        databaseHandler = new DatabaseHandler(getActivity());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        setListFromDatabase();
    }

    private void setListFromDatabase() {
        movieList = databaseHandler.getAllMovies();
        adapter = new WatchListAdapter(getActivity(), movieList);
        recyclerView.setAdapter(adapter);
    }
}

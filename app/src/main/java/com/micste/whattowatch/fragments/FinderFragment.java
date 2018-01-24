package com.micste.whattowatch.fragments;


import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.micste.whattowatch.R;
import com.micste.whattowatch.model.MoviesByGenreResponse;
import com.micste.whattowatch.model.Result;
import com.micste.whattowatch.netcom.ApiService;
import com.micste.whattowatch.utils.ApiHelper;
import com.micste.whattowatch.utils.ConverterHelper;
import com.micste.whattowatch.utils.SnackBarHelper;
import com.micste.whattowatch.view.ItemCard;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.listeners.ItemRemovedListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinderFragment extends Fragment {
    private static final String ARG_GENRE = "genre";
    private static final String ARG_GENRE_ID = "genreID";

    private String genre;
    private int genreId;
    private ApiService apiService;
    private List<Result> results;
    private CoordinatorLayout coordinatorLayout;
    private SwipePlaceHolderView swipeView;
    private int marginBot = 160; // dp


    public FinderFragment() {
        // Required empty public constructor
    }

    public static FinderFragment newInstance(String genre, int id) {
        FinderFragment fragment = new FinderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_GENRE, genre);
        args.putInt(ARG_GENRE_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            genre = getArguments().getString(ARG_GENRE);
            genreId = getArguments().getInt(ARG_GENRE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_finder, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        coordinatorLayout = getActivity().findViewById(R.id.mainCoordinatorLayout);
        swipeView = view.findViewById(R.id.swipeView);

        Point windowSize = ConverterHelper.getDisplaySize(getActivity().getWindowManager());

        swipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setViewWidth(windowSize.x)
                        .setViewHeight(windowSize.y - ConverterHelper.dpToPx(marginBot))
                        .setViewGravity(Gravity.TOP)
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.item_swipe_in_view)
                        .setSwipeOutMsgLayoutId(R.layout.item_swipe_out_view));

        swipeView.addItemRemoveListener(new ItemRemovedListener() {
            @Override
            public void onItemRemoved(int count) {
                if (count == 3) {
                    //TODO Add more results to swipeview
                }
            }
        });

        apiService = ApiHelper.getApiService();
        getResults();
    }

    private void getResults() {
        apiService.getMovies(String.valueOf(genreId)).enqueue(new Callback<MoviesByGenreResponse>() {
            @Override
            public void onResponse(Call<MoviesByGenreResponse> call, Response<MoviesByGenreResponse> response) {
                if (response.isSuccessful()) {
                    results = response.body().getResults();

                    for (Result result : results) {
                        swipeView.addView(new ItemCard(getActivity(), result, swipeView));
                    }
                } else {
                    SnackBarHelper.showSnackBarMessage(coordinatorLayout, getString(R.string.generic_network_error));
                }
            }

            @Override
            public void onFailure(Call<MoviesByGenreResponse> call, Throwable t) {
                SnackBarHelper.showSnackBarMessage(coordinatorLayout, getString(R.string.generic_network_error));
            }
        });
    }
}
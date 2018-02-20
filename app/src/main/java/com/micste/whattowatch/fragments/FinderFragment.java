package com.micste.whattowatch.fragments;


import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    private static final String ARG_SORT_BY = "sortBy";

    private String genre;
    private int genreId;
    private int currentPage = 1, totalPages;
    private boolean isLastPage = false;
    private String sortBy;
    private ApiService apiService;
    private List<Result> results;
    private CoordinatorLayout coordinatorLayout;
    private SwipePlaceHolderView swipeView;
    private ProgressBar progressBar;
    private TextView errorText;


    public FinderFragment() {
        // Required empty public constructor
    }

    public static FinderFragment newInstance(String genre, int id, String sortBy) {
        FinderFragment fragment = new FinderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_GENRE, genre);
        args.putInt(ARG_GENRE_ID, id);
        args.putString(ARG_SORT_BY, sortBy);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            genre = getArguments().getString(ARG_GENRE);
            genreId = getArguments().getInt(ARG_GENRE_ID);
            sortBy = getArguments().getString(ARG_SORT_BY);
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
        progressBar = view.findViewById(R.id.progressBar);
        errorText = view.findViewById(R.id.errorText);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        Point windowSize = ConverterHelper.getDisplaySize(getActivity().getWindowManager());
        int marginBot = toolbar.getHeight() + 50;

        swipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setViewWidth(windowSize.x)
                        .setViewHeight(windowSize.y - marginBot )
                        .setViewGravity(Gravity.TOP)
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.item_swipe_in_view)
                        .setSwipeOutMsgLayoutId(R.layout.item_swipe_out_view));

        swipeView.addItemRemoveListener(new ItemRemovedListener() {
            @Override
            public void onItemRemoved(int count) {
                if (count == 3) {
                    if (!isLastPage) {
                        loadMoreItems();
                    }
                } else if (count == 0) {
                    displayErrorMessage();
                }
            }
        });

        apiService = ApiHelper.getApiService();
        getResults();
    }

    private void getResults() {
        progressBar.setVisibility(View.VISIBLE);

        apiService.getMovies(String.valueOf(genreId), sortBy, currentPage).enqueue(new Callback<MoviesByGenreResponse>() {
            @Override
            public void onResponse(Call<MoviesByGenreResponse> call, Response<MoviesByGenreResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MoviesByGenreResponse moviesByGenreResponse = response.body();
                    totalPages = moviesByGenreResponse.getTotalPages();

                    if (results != null && !results.isEmpty()) {
                        results.clear();
                    }

                    results = response.body().getResults();

                    for (Result result : results) {
                        swipeView.addView(new ItemCard(getActivity(), result, swipeView));
                    }

                }
                else if (response.code() == 422) {
                    displayErrorMessage();
                } else {
                    SnackBarHelper.showSnackBarMessage(coordinatorLayout, getString(R.string.generic_network_error));
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MoviesByGenreResponse> call, Throwable t) {
                SnackBarHelper.showSnackBarMessage(coordinatorLayout, getString(R.string.generic_network_error));
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void loadMoreItems() {
        currentPage += 1;
        if (currentPage <= totalPages) {
            getResults();
        } else {
            isLastPage = true;
        }
    }

    private void displayErrorMessage() {
        errorText.setText(getString(R.string.no_more_items));
        errorText.setVisibility(View.VISIBLE);
    }
}

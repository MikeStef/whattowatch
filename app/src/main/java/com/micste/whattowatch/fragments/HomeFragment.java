package com.micste.whattowatch.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.micste.whattowatch.R;
import com.micste.whattowatch.adapter.HorizontalItemAdapter;
import com.micste.whattowatch.model.Result;
import com.micste.whattowatch.model.ResultsResponse;
import com.micste.whattowatch.netcom.ApiService;
import com.micste.whattowatch.utils.ApiHelper;
import com.micste.whattowatch.utils.SnackBarHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private ApiService apiService;
    private List<Result> resultsNowPlaying, resultsMostPopular, resultsUpcoming;
    private RecyclerView recyclerNowPlaying, recyclerMostPopular, recyclerUpcoming;
    private HorizontalItemAdapter nowPlayingAdapter, mostPopularAdapter, upcomingAdapter;
    private CoordinatorLayout coordinatorLayout;

    //TODO Change display of data to 1 recyclerview
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        coordinatorLayout = getActivity().findViewById(R.id.mainCoordinatorLayout);
        recyclerNowPlaying = view.findViewById(R.id.now_playing_recyclerview);
        recyclerMostPopular = view.findViewById(R.id.most_popular_recyclerview);
        recyclerUpcoming = view.findViewById(R.id.upcoming_recyclerview);
        LinearLayoutManager layoutManagerNowPlaying = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,
                false);
        LinearLayoutManager layoutManagerMostPopular = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,
                false);
        LinearLayoutManager layoutManagerUpcoming = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,
                false);
        recyclerNowPlaying.setLayoutManager(layoutManagerNowPlaying);
        recyclerMostPopular.setLayoutManager(layoutManagerMostPopular);
        recyclerUpcoming.setLayoutManager(layoutManagerUpcoming);

        apiService = ApiHelper.getApiService();
        getNowPlaying();
        getMostPopular();
        getUpcoming();
    }

    private void getNowPlaying() {
        apiService.getNowPlaying().enqueue(new Callback<ResultsResponse>() {
            @Override
            public void onResponse(Call<ResultsResponse> call, Response<ResultsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultsNowPlaying = response.body().getResults();
                    nowPlayingAdapter = new HorizontalItemAdapter(getActivity(), resultsNowPlaying);
                    recyclerNowPlaying.setAdapter(nowPlayingAdapter);

                } else {
                    SnackBarHelper.showSnackBarMessage(coordinatorLayout, getString(R.string.generic_network_error));
                }
            }

            @Override
            public void onFailure(Call<ResultsResponse> call, Throwable t) {
                SnackBarHelper.showSnackBarMessage(coordinatorLayout, getString(R.string.generic_network_error));
            }
        });
    }

    private void getMostPopular() {
        apiService.getMostPopular().enqueue(new Callback<ResultsResponse>() {
            @Override
            public void onResponse(Call<ResultsResponse> call, Response<ResultsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultsMostPopular = response.body().getResults();
                    mostPopularAdapter = new HorizontalItemAdapter(getActivity(), resultsMostPopular);
                    recyclerMostPopular.setAdapter(mostPopularAdapter);
                } else {
                    SnackBarHelper.showSnackBarMessage(coordinatorLayout, getString(R.string.generic_network_error));
                }
            }

            @Override
            public void onFailure(Call<ResultsResponse> call, Throwable t) {
                SnackBarHelper.showSnackBarMessage(coordinatorLayout, getString(R.string.generic_network_error));
            }
        });
    }

    private void getUpcoming() {
        apiService.getUpcoming().enqueue(new Callback<ResultsResponse>() {
            @Override
            public void onResponse(Call<ResultsResponse> call, Response<ResultsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultsUpcoming = response.body().getResults();
                    upcomingAdapter = new HorizontalItemAdapter(getActivity(), resultsUpcoming);
                    recyclerUpcoming.setAdapter(upcomingAdapter);
                } else {
                    SnackBarHelper.showSnackBarMessage(coordinatorLayout, getString(R.string.generic_network_error));
                }
            }

            @Override
            public void onFailure(Call<ResultsResponse> call, Throwable t) {
                SnackBarHelper.showSnackBarMessage(coordinatorLayout, getString(R.string.generic_network_error));
            }
        });
    }
}

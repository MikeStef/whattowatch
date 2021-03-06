package com.micste.whattowatch.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.micste.whattowatch.R;
import com.micste.whattowatch.model.Genre;
import com.micste.whattowatch.model.GenresResponse;
import com.micste.whattowatch.netcom.ApiService;
import com.micste.whattowatch.utils.ApiHelper;
import com.micste.whattowatch.utils.CacheHelper;
import com.micste.whattowatch.utils.SnackBarHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscoverFragment extends Fragment {

    private static final String MOST_POPULAR = "popularity.desc";
    private static final String HIGHEST_RATED = "vote_average.desc";
    private static final String RELEASE_DATE = "release_date.desc";

    private ApiService apiService;
    private List<Genre> genres = new ArrayList<>();
    private Spinner genreSpinner;
    private Button buttonFind;
    private RadioGroup sortByRadioGroup;
    private CoordinatorLayout coordinatorLayout;
    private String sortBy;

    public DiscoverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        genreSpinner = view.findViewById(R.id.spinnerGenres);
        buttonFind = view.findViewById(R.id.buttonFind);
        sortByRadioGroup = view.findViewById(R.id.sortByRadioGroup);
        coordinatorLayout = getActivity().findViewById(R.id.mainCoordinatorLayout);

        apiService = ApiHelper.getApiService();
        sortBy = MOST_POPULAR; // Set default sorting

        getGenresCached();
        setupListeners();
    }

    private void setupListeners() {
        buttonFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoFinderFragment();
            }
        });

        sortByRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.radioMostPopular:
                        sortBy = MOST_POPULAR;
                        break;
                    case R.id.radioHighestRated:
                        sortBy = HIGHEST_RATED;
                        break;
                    case R.id.radioReleaseDate:
                        sortBy = RELEASE_DATE;
                        break;
                }
            }
        });
    }

    private void gotoFinderFragment() {
        int position = genreSpinner.getSelectedItemPosition();
        String genre = genres.get(position).getName();
        int genreId = genres.get(position).getId();

        Fragment finderFragment = FinderFragment.newInstance(genre, genreId, sortBy);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainContent, finderFragment).commit();
    }

    // Get genres data locally if available before network call
    private void getGenresCached() {
        String jsonGenres = CacheHelper.readGenresJsonFile(getActivity());
        if (jsonGenres != null) {
            GenresResponse genresResponse = new Gson().fromJson(jsonGenres, GenresResponse.class);
            genres = genresResponse.getGenres();
            ArrayAdapter<Genre> adapter = new ArrayAdapter<>(getActivity(),
                    R.layout.support_simple_spinner_dropdown_item, genres);
            genreSpinner.setAdapter(adapter);
        } else {
            getGenres();
        }
    }

    private void getGenres() {
        apiService.getGenres().enqueue(new Callback<GenresResponse>() {
            @Override
            public void onResponse(Call<GenresResponse> call, Response<GenresResponse> response) {
                if (response.isSuccessful()) {
                    genres = response.body().getGenres();
                    ArrayAdapter<Genre> adapter = new ArrayAdapter<>(getActivity(),
                            R.layout.support_simple_spinner_dropdown_item, genres);
                    genreSpinner.setAdapter(adapter);

                    String json = new Gson().toJson(response.body());
                    CacheHelper.createGenresFile(getActivity(), json);
                } else {
                    SnackBarHelper.showSnackBarMessage(coordinatorLayout, getString(R.string.generic_network_error));
                }
            }

            @Override
            public void onFailure(Call<GenresResponse> call, Throwable t) {
                SnackBarHelper.showSnackBarMessage(coordinatorLayout, getString(R.string.generic_network_error));
                Log.d("DiscoverFragment", "onFailure: " + t.getMessage());
            }
        });
    }

}

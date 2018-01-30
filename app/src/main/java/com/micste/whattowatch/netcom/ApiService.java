package com.micste.whattowatch.netcom;

import com.micste.whattowatch.BuildConfig;
import com.micste.whattowatch.model.GenresResponse;
import com.micste.whattowatch.model.MoviesByGenreResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("3/genre/movie/list?api_key=" + BuildConfig.API_KEY + "&language=en-US")
    Call<GenresResponse> getGenres();

    @GET("3/discover/movie?api_key=" + BuildConfig.API_KEY +
            "&language=en-US&include_adult=false&include_video=false&page=1")
    Call<MoviesByGenreResponse> getMovies(@Query("with_genres") String genre,
                                          @Query("sort_by") String sortBy);
}

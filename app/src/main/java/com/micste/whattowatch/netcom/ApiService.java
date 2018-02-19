package com.micste.whattowatch.netcom;

import com.micste.whattowatch.BuildConfig;
import com.micste.whattowatch.model.GenresResponse;
import com.micste.whattowatch.model.MovieDetailsResponse;
import com.micste.whattowatch.model.MoviesByGenreResponse;
import com.micste.whattowatch.model.ResultsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("3/genre/movie/list?api_key=" + BuildConfig.API_KEY + "&language=en-US")
    Call<GenresResponse> getGenres();

    @GET("3/discover/movie?api_key=" + BuildConfig.API_KEY +
            "&language=en-US&include_adult=false&include_video=false&page=1")
    Call<MoviesByGenreResponse> getMovies(@Query("with_genres") String genre,
                                          @Query("sort_by") String sortBy);

    @GET("3/movie/{movie_id}?api_key=" + BuildConfig.API_KEY + "&language=en-US")
    Call<MovieDetailsResponse> getMovieDetails(@Path("movie_id") int movieId);

    @GET("3/movie/now_playing?api_key=" + BuildConfig.API_KEY + "&language=en-US&page=1")
    Call<ResultsResponse> getNowPlaying();

    @GET("3/movie/popular?api_key=" + BuildConfig.API_KEY + "&language=en-US&page=1")
    Call<ResultsResponse> getMostPopular();

    @GET("3/movie/upcoming?api_key=" + BuildConfig.API_KEY + "&language=en-US&page=1")
    Call<ResultsResponse> getUpcoming();
}

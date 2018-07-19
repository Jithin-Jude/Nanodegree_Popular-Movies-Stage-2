package com.inc.mountzoft.popularmoviesstage_2;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Helper class that contains all the tmdb
 * API endpoints used for this app.
 */
public class MoviesApi {
    public interface MovieDetailVideos {
        @GET("movie/{id}/videos")
        Call<MovieVideos> getMovieVideos(@Path("id") Integer id);
    }

    public interface MovieDetailReviews {
        @GET("movie/{id}/reviews")
        Call<MovieReviews> getMovieReviews(@Path("id") Integer id, @Query("page") Integer page);
    }
}

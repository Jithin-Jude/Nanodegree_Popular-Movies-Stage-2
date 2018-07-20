package com.inc.mountzoft.popularmoviesstage_2;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TmdbRestClient {
    private static String BASE_URL = "https://api.themoviedb.org/3/";

    private MoviesApi.MovieDetailVideos movieVideos;
    private MoviesApi.MovieDetailReviews movieReviews;

    private Retrofit retrofit;

    private static TmdbRestClient instance = null;

    private TmdbRestClient() {
        initializeRetrofit();
    }

    public static TmdbRestClient getInstance() {
        if (instance == null) {
            instance = new TmdbRestClient();
        }
        return instance;
    }

    public MoviesApi.MovieDetailVideos getMovieVidoesImpl() {
        if (movieVideos == null) {
            movieVideos = retrofit.create(MoviesApi.MovieDetailVideos.class);
        }
        return movieVideos;
    }

    public MoviesApi.MovieDetailReviews getMovieReviewsImpl() {
        if (movieReviews == null) {
            movieReviews = retrofit.create(MoviesApi.MovieDetailReviews.class);
        }
        return movieReviews;
    }

    /**
     * Helper function to add the api key parameter
     * to all requests that are made through {@link Retrofit}.
     * Sets the base URL of the requests and the
     * {@link GsonConverterFactory} for parsing the
     * JSON responses.
     */
    private void initializeRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        HttpUrl url = request.url()
                                .newBuilder()
                                .addQueryParameter("api_key", BuildConfig.MY_MOVIE_DB_API_KEY)
                                .build();
                        Request.Builder builder = request.newBuilder()
                                .url(url)
                                .method(request.method(), request.body());
                        request = builder.build();
                        return chain.proceed(request);
                    }
                })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }
}

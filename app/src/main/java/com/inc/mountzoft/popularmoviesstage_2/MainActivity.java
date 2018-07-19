package com.inc.mountzoft.popularmoviesstage_2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static boolean insertData;

    public static String API_KEY;

    public static List<Movie> movies;

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private ActionBar toolbar;

    ApiInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar = getSupportActionBar();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        toolbar.setTitle("Top Rated");

        API_KEY = this.getString(R.string.THEMOVIEDB_API_KEY);

        apiService =
                ApiClient.getClient().create(ApiInterface.class);
        if(isNetworkAvailable()) {

            Call<MovieResponse> call = apiService.getPopularMovies(API_KEY);
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    movies = response.body().getResults();
                    GridAutofitLayoutManager layoutManager = new GridAutofitLayoutManager(getApplicationContext(), 200);
                    mRecyclerView.setLayoutManager(layoutManager);
                    mRecyclerView.setAdapter(new MoviesRecyclerViewAdapter(getApplicationContext(), movies, false, true));
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {}
            });
        }

        if(MoviesRecyclerViewAdapter.fav) {
            toolbar.setTitle(R.string.favorites);
            loadFavoriteMovies();
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(MoviesRecyclerViewAdapter.fav) {
            toolbar.setTitle(R.string.favorites);
            loadFavoriteMovies();
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_most_popular:
                    MoviesRecyclerViewAdapter.fav = false;
                    toolbar.setTitle(R.string.most_popular);
                    if(isNetworkAvailable()) {
                        Call<MovieResponse> callForMostPopularMovies = apiService.getPopularMovies(API_KEY);
                        callForMostPopularMovies.enqueue(new Callback<MovieResponse>() {
                            @Override
                            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                                movies = response.body().getResults();
                                GridAutofitLayoutManager layoutManager = new GridAutofitLayoutManager(getApplicationContext(), 200);
                                mRecyclerView.setLayoutManager(layoutManager);
                                mRecyclerView.setAdapter(new MoviesRecyclerViewAdapter(getApplicationContext(), movies, false, true));
                            }

                            @Override
                            public void onFailure(Call<MovieResponse> call, Throwable t) {}
                        });
                    }else {
                        mRecyclerView.setVisibility(View.GONE);
                    }
                    return true;
                case R.id.navigation_top_rated:
                    MoviesRecyclerViewAdapter.fav = false;
                    toolbar.setTitle(R.string.top_rated);
                    if(isNetworkAvailable()) {
                        Call<MovieResponse> callForTopRatedMovies = apiService.getTopRatedMovies(API_KEY);
                        callForTopRatedMovies.enqueue(new Callback<MovieResponse>() {
                            @Override
                            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                                movies = response.body().getResults();
                                GridAutofitLayoutManager layoutManager = new GridAutofitLayoutManager(getApplicationContext(), 200);
                                mRecyclerView.setLayoutManager(layoutManager);
                                mRecyclerView.setAdapter(new MoviesRecyclerViewAdapter(getApplicationContext(), movies, false, true));
                            }

                            @Override
                            public void onFailure(Call<MovieResponse> call, Throwable t) {}
                        });
                    }else {
                        mRecyclerView.setVisibility(View.GONE);
                    }
                    return true;
                    //==============================================================================
                case R.id.navigation_favorites:
                    MovieDetails.del = false;
                    MainActivity.insertData = false;
                    MoviesRecyclerViewAdapter.fav = true;
                    RoomDatabaseInitializer.populateAsync(RoomMovieDatabase.getAppDatabase(getApplicationContext()));
                    toolbar.setTitle(R.string.favorites);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    loadFavoriteMovies();
                    return true;
                    //==============================================================================
            }
            return false;
        }
    };

    public void loadFavoriteMovies(){

        if(isNetworkAvailable()){

            insertData = false;
            RoomDatabaseInitializer.populateAsync(RoomMovieDatabase.getAppDatabase(this));

            Call<MovieResponse> callForMostFavoriteMovies = apiService.getPopularMovies(API_KEY);
            callForMostFavoriteMovies.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse>call, Response<MovieResponse> response) {
                    movies = response.body().getResults();
                    GridAutofitLayoutManager layoutManager = new GridAutofitLayoutManager(getApplicationContext(), 200);
                    mRecyclerView.setLayoutManager(layoutManager);
                    mRecyclerView.setAdapter(new MoviesRecyclerViewAdapter(getApplicationContext(), true, true));
                }

                @Override
                public void onFailure(Call<MovieResponse>call, Throwable t) {}
            });
        }else {

            insertData = false;
            RoomDatabaseInitializer.populateAsync(RoomMovieDatabase.getAppDatabase(this));

            GridAutofitLayoutManager layoutManager = new GridAutofitLayoutManager(getApplicationContext(), 200);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(new MoviesRecyclerViewAdapter(getApplicationContext(), true, false));
        }
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

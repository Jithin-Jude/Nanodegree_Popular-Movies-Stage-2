package com.inc.mountzoft.popularmoviesstage_2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
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
    public static boolean popular = true;
    public static boolean topRated;

    public static String API_KEY;

    public static List<Movie> movies;

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private final String KEY_RECYCLER_STATE = "recycler_state";
    Parcelable mListState;
    GridAutofitLayoutManager mLayoutManager;

    private ActionBar toolbar;

    ApiInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar = getSupportActionBar();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        API_KEY = BuildConfig.MY_MOVIE_DB_API_KEY;

        apiService =
                ApiClient.getClient().create(ApiInterface.class);

        mLayoutManager = new GridAutofitLayoutManager(getApplicationContext(), 200);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        // Save list state
        mListState = mLayoutManager.onSaveInstanceState();
        state.putParcelable(KEY_RECYCLER_STATE, mListState);
        Log.d("reached","inside onSaveInstanceState");
    }

    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        Log.d("reached","inside onRestoreInstanceState");

        // Retrieve list state and list/item positions
        if(state != null) {
            mListState = state.getParcelable(KEY_RECYCLER_STATE);
            Log.d("reached","inside onRestoreInstanceState if");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(popular){toolbar.setTitle(R.string.most_popular);}
        if(topRated){toolbar.setTitle(R.string.top_rated);}

        Log.d("reached","inside onResume");

        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
            if(MoviesRecyclerViewAdapter.fav) {
                mRecyclerView.setAdapter(new MoviesRecyclerViewAdapter(getApplicationContext(), movies, true, true));
                toolbar.setTitle(R.string.favorites);
            }else {
                mRecyclerView.setAdapter(new MoviesRecyclerViewAdapter(getApplicationContext(), movies, false, true));
            }
            Log.d("reached","inside onResume if");
        }else {
            Call<MovieResponse> callForMostPopularMovies = apiService.getPopularMovies(API_KEY);
            callForMostPopularMovies.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    movies = response.body().getResults();
                    mRecyclerView.setAdapter(new MoviesRecyclerViewAdapter(getApplicationContext(), movies, false, true));
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {}
            });
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_most_popular:

                    popular = true;
                    MoviesRecyclerViewAdapter.fav = false;
                    topRated = false;

                    toolbar.setTitle(R.string.most_popular);
                    if(isNetworkAvailable()) {
                        loadPopularMovies();
                    }else {
                        mRecyclerView.setVisibility(View.GONE);
                    }
                    return true;
                case R.id.navigation_top_rated:

                    topRated = true;
                    MoviesRecyclerViewAdapter.fav = false;
                    popular = false;

                    toolbar.setTitle(R.string.top_rated);
                    if(isNetworkAvailable()) {
                        loadTopRatedMovies();
                    }else {
                        mRecyclerView.setVisibility(View.GONE);
                    }
                    return true;
                    //==============================================================================
                case R.id.navigation_favorites:
                    MovieDetails.del = false;
                    MainActivity.insertData = false;

                    MoviesRecyclerViewAdapter.fav = true;
                    popular = false;
                    topRated = false;

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
//=====================================loadFavoriteMovies==========================================
    public void loadFavoriteMovies(){

        if(isNetworkAvailable()){

            insertData = false;
            RoomDatabaseInitializer.populateAsync(RoomMovieDatabase.getAppDatabase(this));

            Call<MovieResponse> callForMostFavoriteMovies = apiService.getPopularMovies(API_KEY);
            callForMostFavoriteMovies.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse>call, Response<MovieResponse> response) {
                    movies = response.body().getResults();
                    mRecyclerView.setAdapter(new MoviesRecyclerViewAdapter(getApplicationContext(), true, true));
                }

                @Override
                public void onFailure(Call<MovieResponse>call, Throwable t) {}
            });
        }else {

            insertData = false;
            RoomDatabaseInitializer.populateAsync(RoomMovieDatabase.getAppDatabase(this));

            mLayoutManager = new GridAutofitLayoutManager(getApplicationContext(), 200);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(new MoviesRecyclerViewAdapter(getApplicationContext(), true, false));
        }
    }
//=================================loadPopularMovies===============================================
public void loadPopularMovies(){
    Call<MovieResponse> callForMostPopularMovies = apiService.getPopularMovies(API_KEY);
    callForMostPopularMovies.enqueue(new Callback<MovieResponse>() {
        @Override
        public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
            movies = response.body().getResults();
            mRecyclerView.setAdapter(new MoviesRecyclerViewAdapter(getApplicationContext(), movies, false, true));
        }

        @Override
        public void onFailure(Call<MovieResponse> call, Throwable t) {}
    });
}
//====================================loadTopRatedMovies========================================
public void loadTopRatedMovies(){
    Call<MovieResponse> callForTopRatedMovies = apiService.getTopRatedMovies(API_KEY);
    callForTopRatedMovies.enqueue(new Callback<MovieResponse>() {
        @Override
        public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
            movies = response.body().getResults();
            mRecyclerView.setAdapter(new MoviesRecyclerViewAdapter(getApplicationContext(), movies, false, true));
        }

        @Override
        public void onFailure(Call<MovieResponse> call, Throwable t) {}
    });
}
//==================================================================================================
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

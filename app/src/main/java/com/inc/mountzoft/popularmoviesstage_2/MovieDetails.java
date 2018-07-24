package com.inc.mountzoft.popularmoviesstage_2;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MovieDetails extends AppCompatActivity {

    int position;

    static int totalMovies;

    static List<RoomMovieEntity> movieTable;

    static int deleteMovieId;

    static boolean del;

    VideoListRecyclerViewAdapter videosListAdapter;

    @BindView(R.id.video_list_recycler_view)
    RecyclerView videosRecyclerView;

    ReviewListRecyclerViewAdapter reviewListAdapter;

    @BindView(R.id.review_list_recycler_view)
    RecyclerView reviewRecyclerView;

    @BindView(R.id.floatingActionButton)
    FloatingActionButton favBtn;

    @BindView(R.id.videos_tv)
    TextView videoTV;

    @BindView(R.id.video_list_card_view)
    CardView cardViewVideos;

    @BindView(R.id.reviews_tv)
    TextView reviewTV;

    @BindView(R.id.line2)
    View lineOne;

    @BindView(R.id.line3)
    View lineTwo;

    ApiInterface apiService;


    static int movieId;
    static String movieName;
    static String movieRating;
    static String movieReleaseDate;
    static String movieSynopsis;
    static String moviePosterImageUrl;
    static String movieBackbropImageUrl;

    Movie movie;


    @BindView(R.id.img_backdrop)
    ImageView mImageViewBackdrop;

    @BindView(R.id.img_poster)
    ImageView mImageViewPoster;

    @BindView(R.id.tv_movie_title)
    TextView mTextViewMovieTitle;

    @BindView(R.id.tv_release_date)
    TextView mTextViewReleasDate;

    @BindView(R.id.tv_avg_rating)
    TextView mTextViewAvgRating;

    @BindView(R.id.tv_summary)
    TextView mTextViewSummary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        del = false;
        MainActivity.insertData = false;
        RoomDatabaseInitializer.populateAsync(RoomMovieDatabase.getAppDatabase(this));

        Intent intent = getIntent();
        movie = intent.getParcelableExtra("INTENT_MOVIE_DETAIL");
        position = getIntent().getIntExtra(MoviesRecyclerViewHolder.POSITION_ID, 0);

        if(isNetworkAvailable()) {
            boolean setFBtn = isAlreadyFavorite();
        }

        if(MoviesRecyclerViewAdapter.fav)
            favBtn.setImageResource(R.drawable.ic_favorite_red_24dp);

        if(MoviesRecyclerViewAdapter.fav){

            videoTV.setVisibility(View.GONE);
            cardViewVideos.setVisibility(View.GONE);
            reviewTV.setVisibility(View.GONE);
            reviewRecyclerView.setVisibility(View.GONE);
            lineOne.setVisibility(View.GONE);
            lineTwo.setVisibility(View.GONE);

            if(isNetworkAvailable()) {
                mTextViewMovieTitle.setText(RoomDatabaseInitializer.roomMovieEntityList.get(position).getMovieName());
                mTextViewReleasDate.setText(RoomDatabaseInitializer.roomMovieEntityList.get(position).getMovieReleaseDate());
                mTextViewAvgRating.setText(RoomDatabaseInitializer.roomMovieEntityList.get(position).getMovieRating());
                mTextViewSummary.setText(RoomDatabaseInitializer.roomMovieEntityList.get(position).getMovieSynopsis());

                Glide.with(this)
                        .load(RoomDatabaseInitializer.roomMovieEntityList.get(position).getPosterImageUrl()).apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                        .into(mImageViewPoster);

                Glide.with(this)
                        .load(RoomDatabaseInitializer.roomMovieEntityList.get(position).getBackdropImageUrl()).apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                        .into(mImageViewBackdrop);
            }else {
                mTextViewMovieTitle.setText(RoomDatabaseInitializer.roomMovieEntityList.get(position).getMovieName());
                mTextViewReleasDate.setText(RoomDatabaseInitializer.roomMovieEntityList.get(position).getMovieReleaseDate());
                mTextViewAvgRating.setText(RoomDatabaseInitializer.roomMovieEntityList.get(position).getMovieRating());
                mTextViewSummary.setText(RoomDatabaseInitializer.roomMovieEntityList.get(position).getMovieSynopsis());

                Glide.with(this)
                        .load(RoomDatabaseInitializer.roomMovieEntityList.get(position).getPosterImageUrl()).apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                        .into(mImageViewPoster);

                Glide.with(this)
                        .load(RoomDatabaseInitializer.roomMovieEntityList.get(position).getBackdropImageUrl()).apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                        .into(mImageViewBackdrop);
            }
        }
        if(!MoviesRecyclerViewAdapter.fav) {

            videoTV.setVisibility(View.VISIBLE);
            cardViewVideos.setVisibility(View.VISIBLE);
            reviewTV.setVisibility(View.VISIBLE);
            reviewRecyclerView.setVisibility(View.VISIBLE);
            lineOne.setVisibility(View.VISIBLE);
            lineTwo.setVisibility(View.VISIBLE);

            mTextViewMovieTitle.setText(MainActivity.movies.get(position).getTitle());
            mTextViewReleasDate.setText(MainActivity.movies.get(position).getReleaseDate());
            mTextViewAvgRating.setText(String.valueOf(MainActivity.movies.get(position).getVoteAverage()));
            mTextViewSummary.setText(MainActivity.movies.get(position).getOverview());

            Glide.with(this)
                    .load(getString(R.string.poster_image_url_start) + MainActivity.movies
                            .get(position).getPosterPath())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_error_outline_white_24dp))
                    .into(mImageViewPoster);
            Glide.with(this)
                    .load(getString(R.string.backbrop_url_start) + MainActivity.movies
                            .get(position).getBackdropPath())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_error_outline_white_24dp))
                    .into(mImageViewBackdrop);


            apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            loadMovieVideos();

            LinearLayoutManager videoLinearLayoutManager = new LinearLayoutManager(this);
            videoLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            videosListAdapter = new VideoListRecyclerViewAdapter(this);
            videosRecyclerView.setLayoutManager(videoLinearLayoutManager);
            videosRecyclerView.setAdapter(videosListAdapter);

            loadMovieReviews();

            LinearLayoutManager reviewLinearLayoutManager = new LinearLayoutManager(this);
            reviewLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            reviewListAdapter = new ReviewListRecyclerViewAdapter(this);
            reviewRecyclerView.setLayoutManager(reviewLinearLayoutManager);
            reviewRecyclerView.setNestedScrollingEnabled(false);
            reviewRecyclerView.setAdapter(reviewListAdapter);
        }

    }

    private void loadMovieVideos() {

        Call<MovieVideos> call = TmdbRestClient.getInstance()
                .getMovieVidoesImpl()
                .getMovieVideos(MainActivity.movies.get(position).getId());
        Callback<MovieVideos> callback = new Callback<MovieVideos>() {
            private MovieVideo video;

            @Override
            public void onResponse(Call<MovieVideos> call, Response<MovieVideos> response) {
                if (!response.isSuccessful()) {
                    video = StateHandler.handleMovieVideoState(getApplicationContext(), Constants.SERVER_ERROR);

                } else if (response.body().getVideos().size() == 0) {
                    video = StateHandler.handleMovieVideoState(getApplicationContext(), Constants.NONE);

                } else {
                    MainActivity.movies.get(position).setMovieVideos(response.body().getVideos());
                }
                update();
            }

            @Override
            public void onFailure(Call<MovieVideos> call, Throwable t) {
                video = StateHandler.handleMovieVideoState(getApplicationContext(), Constants.NETWORK_ERROR);
                update();
            }

            private void update() {
                if (video != null) {
                    ArrayList<MovieVideo> videos = new ArrayList<>();
                    videos.add(video);
                    video = null;
                    try{
                        movie.setMovieVideos(videos);
                    }catch(Exception e){}
                }
                setupMovieVideos();
            }
        };
        call.enqueue(callback);
    }


    private void setupMovieVideos() {
        videosListAdapter.setVideos(MainActivity.movies.get(position).getMovieVideos());
        videosListAdapter.notifyDataSetChanged();
        videosRecyclerView.setHasFixedSize(true);
    }

    //=================================================================================================

    private void loadMovieReviews() {

        Call<MovieReviews> call = TmdbRestClient.getInstance()
                .getMovieReviewsImpl()
                .getMovieReviews(MainActivity.movies.get(position).getId(), 1);
        Callback<MovieReviews> callback = new Callback<MovieReviews>() {
            private MovieReview review;

            @Override
            public void onResponse(Call<MovieReviews> call, Response<MovieReviews> response) {
                if (!response.isSuccessful()) {
                    review = StateHandler.handleMovieReviewState(getApplicationContext(), Constants.SERVER_ERROR);

                } else if (response.body().getMovieReviews().size() == 0) {
                    review = StateHandler.handleMovieReviewState(getApplicationContext(), Constants.NONE);

                } else {
                    MainActivity.movies.get(position).setMovieReviews(response.body().getMovieReviews());
                }
                update();
            }

            @Override
            public void onFailure(Call<MovieReviews> call, Throwable t) {
                review = StateHandler.handleMovieReviewState(getApplicationContext(), Constants.NETWORK_ERROR);
                update();
            }

            private void update() {
                if (review != null) {
                    ArrayList<MovieReview> reviews = new ArrayList<>();
                    reviews.add(review);
                    review = null;

                    MainActivity.movies.get(position).setMovieReviews(reviews);
                }
                setupMovieReviews();
            }
        };
        call.enqueue(callback);
    }
    private void setupMovieReviews() {
        reviewListAdapter.setReviews(MainActivity.movies.get(position).getMovieReviews());
        reviewListAdapter.notifyDataSetChanged();
        reviewRecyclerView.setHasFixedSize(true);
    }

    //==============================================================================================

    public void addToFavoriteBtn(View view){
        if(isNetworkAvailable()) {

            if(MoviesRecyclerViewAdapter.fav){
                del = true;
                MainActivity.insertData = false;
                this.deleteMovieId = movieTable.get(position).getId();
                RoomDatabaseInitializer.populateAsync(RoomMovieDatabase.getAppDatabase(this));
                Toast.makeText(this, R.string.movie_delete_notify ,Toast.LENGTH_SHORT).show();
                favBtn.setImageResource(R.drawable.ic_favorite_border_red_24dp);
                del = false;
                MainActivity.insertData = false;
                RoomDatabaseInitializer.populateAsync(RoomMovieDatabase.getAppDatabase(this));
                finish();
                return;
            }
          if(isAlreadyFavorite()){
                del = true;
                MainActivity.insertData = false;
                RoomDatabaseInitializer.populateAsync(RoomMovieDatabase.getAppDatabase(this));
                this.deleteMovieId = MainActivity.movies.get(position).getId();
                Toast.makeText(this,R.string.movie_delete_notify ,Toast.LENGTH_SHORT).show();
                favBtn.setImageResource(R.drawable.ic_favorite_border_red_24dp);
                del = false;
                MainActivity.insertData = false;
                RoomDatabaseInitializer.populateAsync(RoomMovieDatabase.getAppDatabase(this));
           }else{
                movieId = MainActivity.movies.get(position).getId();
                movieName = MainActivity.movies.get(position).getTitle();
                movieRating = String.valueOf(MainActivity.movies.get(position).getVoteAverage());
                movieReleaseDate = MainActivity.movies.get(position).getReleaseDate();
                movieSynopsis = MainActivity.movies.get(position).getOverview();

                moviePosterImageUrl = getString(R.string.poster_image_url_start) + MainActivity.movies
                        .get(position).getPosterPath();

                movieBackbropImageUrl = getString(R.string.backbrop_url_start) + MainActivity.movies
                        .get(position).getBackdropPath();

                del = false;
                MainActivity.insertData = true;
                RoomDatabaseInitializer.populateAsync(RoomMovieDatabase.getAppDatabase(this));
                Toast.makeText(this, R.string.movie_add_notify, Toast.LENGTH_SHORT).show();
                favBtn.setImageResource(R.drawable.ic_favorite_red_24dp);
                del = false;
                MainActivity.insertData = false;
                RoomDatabaseInitializer.populateAsync(RoomMovieDatabase.getAppDatabase(this));
            }
        }else{
            del = true;
            MainActivity.insertData = false;
            this.deleteMovieId = movieTable.get(position).getId();
            RoomDatabaseInitializer.populateAsync(RoomMovieDatabase.getAppDatabase(this));
            Toast.makeText(this,R.string.movie_delete_notify ,Toast.LENGTH_SHORT).show();
            favBtn.setImageResource(R.drawable.ic_favorite_border_red_24dp);
            del = false;
            MainActivity.insertData = false;
            RoomDatabaseInitializer.populateAsync(RoomMovieDatabase.getAppDatabase(this));
            finish();
            return;
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean isAlreadyFavorite(){
        del = false;
        MainActivity.insertData = false;
        RoomDatabaseInitializer.populateAsync(RoomMovieDatabase.getAppDatabase(this));
        for (int i=0; i < totalMovies; i++){
            if(MainActivity.movies.get(position).getId() == movieTable.get(i).getId()){
                favBtn.setImageResource(R.drawable.ic_favorite_red_24dp);
                return true;
            }
        }
        favBtn.setImageResource(R.drawable.ic_favorite_border_red_24dp);
        return false;
    }
    //==============================================================================================
}

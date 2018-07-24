package com.inc.mountzoft.popularmoviesstage_2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

class MoviesRecyclerViewAdapter extends RecyclerView.Adapter <MoviesRecyclerViewHolder> {

    Context mContext;
    List<Movie> movies;
    static boolean fav;
    static boolean network;

    private LayoutInflater mInflater;

    // data is passed into the constructor
    MoviesRecyclerViewAdapter(Context context, List<Movie> movies, boolean fav, boolean network) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.movies = movies;
        this.fav = fav;
        this.network = network;

    }

    MoviesRecyclerViewAdapter(Context context, boolean fav, boolean network) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.fav = fav;
        this.network = network;
    }

    @Override
    public int getItemCount() {
        if(fav){
            try {
                return RoomDatabaseInitializer.roomMovieEntityList.size();
            }catch (Exception e){return 0; }
        }
        return movies.size();
    }
    @Override
    public MoviesRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = mInflater.inflate(R.layout.item_movie, viewGroup, false);
        return new MoviesRecyclerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(MoviesRecyclerViewHolder mMoviesRecyclerViewHolder, int position) {
        if(fav){
            Glide.with(mContext)
                    .load(RoomDatabaseInitializer.roomMovieEntityList.get(position).getPosterImageUrl()).apply(new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .into(mMoviesRecyclerViewHolder.mImageView);
        }else {
            Glide.with(mContext)
                    .load(mContext.getString(R.string.poster_image_url_start) + movies.get(position).getPosterPath())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_error_outline_white_24dp))
                    .into(mMoviesRecyclerViewHolder.mImageView);
        }

        mMoviesRecyclerViewHolder.setItemClickListener(new RecyclerViewClickListener() {
            @Override
            public void onItemClick(int pos) {}
        });
    }
}
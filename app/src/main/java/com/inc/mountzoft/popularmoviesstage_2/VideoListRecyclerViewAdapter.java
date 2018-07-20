package com.inc.mountzoft.popularmoviesstage_2;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

/**
 * Created by JithinJude on 15-03-2018.
 */

public class VideoListRecyclerViewAdapter extends RecyclerView.Adapter<VideoRecyclerViewHolder> {

    Context context;
    private ArrayList<MovieVideo> videos;
    private final String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg";
    int position;

    private LayoutInflater mInflater;
    private RecyclerViewClickListener mClickListener;

    // data is passed into the constructor
    VideoListRecyclerViewAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.videos = new ArrayList<>();
    }

    VideoListRecyclerViewAdapter(Context context, int position) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.videos = new ArrayList<>();
        this.position = position;
    }

    public void setVideos(ArrayList<MovieVideo> videos) {
        this.videos.addAll(videos);
    }

    // inflates the row layout from xml when needed
    @Override
    public VideoRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.video_list_recycler_view_cell, parent, false);
        return new VideoRecyclerViewHolder(view, videos, position);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(VideoRecyclerViewHolder holder, int position) {
        //MovieVideo catagory_recyclerview_content = videos.get(pos);

        this.position = position;
        holder.rowText.setText(videos.get(holder.getAdapterPosition()).getName());
        Glide.with(context)
                .load(String.format(YOUTUBE_THUMBNAIL_URL, videos.get(position).getKey()))
                .apply(new RequestOptions()
                .placeholder(R.drawable.ic_error_outline_white_24dp))
                .into(holder.imageView);

        holder.setItemClickListener(new RecyclerViewClickListener() {
            @Override
            public void onItemClick(int pos) {}
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return videos.size();
    }
}

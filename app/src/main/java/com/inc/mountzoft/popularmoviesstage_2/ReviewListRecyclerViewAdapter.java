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

public class ReviewListRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewHolder> {

    Context context;
    private ArrayList<MovieReview> movieReviews;
    int position;

    private LayoutInflater mInflater;
    private RecyclerViewClickListener mClickListener;

    // data is passed into the constructor
    ReviewListRecyclerViewAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.movieReviews =  new ArrayList<>();
    }

    public void setReviews(ArrayList<MovieReview> reviews) {
        this.movieReviews.addAll(reviews);
    }


    // inflates the row layout from xml when needed
    @Override
    public ReviewRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.review_recycler_view_row, parent, false);
        return new ReviewRecyclerViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ReviewRecyclerViewHolder holder, int position) {
        holder.reviewTitle.setText(movieReviews.get(position).getAuthor());
        holder.reviewContent.setText(movieReviews.get(position).getContent());

        this.position = position;

        holder.setItemClickListener(new RecyclerViewClickListener() {
            @Override
            public void onItemClick(int pos) {}
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return movieReviews.size();
    }
}

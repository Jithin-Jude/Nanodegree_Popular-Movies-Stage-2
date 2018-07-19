package com.inc.mountzoft.popularmoviesstage_2;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ReviewRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView reviewTitle;
    TextView reviewContent;
    RecyclerViewClickListener itemClickListener;

    Context context;

    public ReviewRecyclerViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();

        reviewTitle = (TextView) itemView.findViewById(R.id.review_title);
        reviewContent = (TextView) itemView.findViewById(R.id.text_view_review);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //Handle videolist_rv_click_here
    }

    public void setItemClickListener(RecyclerViewClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
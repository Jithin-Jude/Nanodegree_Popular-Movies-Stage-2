package com.inc.mountzoft.popularmoviesstage_2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;


public class MoviesRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    String POSITION_ID = "position_id";

    ImageView mImageView;
    RecyclerViewClickListener itemClickListener;
    Context context;
    public MoviesRecyclerViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        mImageView = (ImageView) itemView.findViewById(R.id.grid_image);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.itemClickListener.onItemClick(this.getLayoutPosition());
        Intent intent = new Intent(context,MovieDetails.class);
        intent.putExtra(POSITION_ID, getAdapterPosition());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void setItemClickListener(RecyclerViewClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}

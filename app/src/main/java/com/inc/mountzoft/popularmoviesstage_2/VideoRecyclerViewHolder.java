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


public class VideoRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ArrayList<MovieVideo> videos;
    int position;
    TextView rowText;
    ImageView imageView;
    RecyclerViewClickListener itemClickListener;

    Context context;

    public VideoRecyclerViewHolder(View itemView, ArrayList<MovieVideo> videos, int position) {
        super(itemView);
        this.context = itemView.getContext();
        this.videos = videos;
        this.position = position;

        rowText = (TextView) itemView.findViewById(R.id.text_view_name);
        imageView = (ImageView) itemView.findViewById(R.id.image_view_thumbnail);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //Handle videolist_rv_click_here
        watchYoutubeVideo(videos.get(position).getKey());
    }

    public void setItemClickListener(RecyclerViewClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private void watchYoutubeVideo(String id){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            this.context.startActivity(intent);
        }catch (ActivityNotFoundException ex){
            Intent intent=new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v="+id));
            this.context.startActivity(intent);
        }
    }
}
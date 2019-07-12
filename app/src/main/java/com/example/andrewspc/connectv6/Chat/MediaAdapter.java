package com.example.andrewspc.connectv6.Chat;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.andrewspc.connectv6.R;

import java.util.ArrayList;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {

    //Upload Image in Chats
    //Youtube: https://www.youtube.com/watch?v=oXBwHNMW9Ho&list=PLxabZQCAe5fgGQggJxp5nuI1ESzP-oMED&index=13

    ArrayList<String> mediaList;
    Context context;

    public MediaAdapter(Context context, ArrayList<String> mediaList){
        this.context = context;
        this.mediaList = mediaList;
    }

    @Override
    public MediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media, null, false);
        MediaViewHolder mediaViewHolder = new MediaViewHolder(layoutView);

        return mediaViewHolder;
    }

    @Override
    public void onBindViewHolder(MediaViewHolder holder, int position) {
        Glide.with(context).load(Uri.parse(mediaList.get(position))).into(holder.mMedia);
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public class MediaViewHolder extends RecyclerView.ViewHolder {

        ImageView mMedia;

        public MediaViewHolder(View itemView) {
            super(itemView);
            mMedia = itemView.findViewById(R.id.media);
        }
    }
}

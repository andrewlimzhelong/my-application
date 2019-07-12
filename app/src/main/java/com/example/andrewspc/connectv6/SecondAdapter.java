package com.example.andrewspc.connectv6;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SecondAdapter extends RecyclerView.Adapter<SecondAdapter.MySecondViewHolder> {

    Context context;
    ArrayList<Portfolio> portfolios;

    public SecondAdapter(Context c, ArrayList<Portfolio> p)
    {
        context = c;
        portfolios = p;
    }

    @NonNull
    @Override
    public MySecondViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int position) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.portfolioview, parent, false);
        final MySecondViewHolder vHolder = new MySecondViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MySecondViewHolder holder, final int position) {

        holder.itemName.setText(portfolios.get(position).getTitle());
        Picasso.get().load(portfolios.get(position).getPortfolioPic1()).fit().into(holder.profilePic);

                Intent intent = new Intent(context, IndividualActivity.class);
                intent.putExtra("newPost", portfolios.get(position).getPortfolioPic1());
    }

    @Override
    public int getItemCount() {
        return portfolios.size();
    }

    public static class MySecondViewHolder extends RecyclerView.ViewHolder{
        private CardView specificUser;
        private ImageView profilePic;
        private TextView itemName;

        public MySecondViewHolder(View itemView){
            super(itemView);

            profilePic = (ImageView) itemView.findViewById(R.id.portfolioImage1);
            itemName = itemView.findViewById(R.id.portfolioDescription1);

            //For Displaying the individual user profile upon click
            specificUser = (CardView) itemView.findViewById(R.id.profilePageImages);

        }

    }

}


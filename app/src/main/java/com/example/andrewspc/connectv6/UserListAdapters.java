package com.example.andrewspc.connectv6;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
/*
public class UserListAdapters extends RecyclerView.Adapter<UserListAdapters.UserListViewHolder> {

    ArrayList<UserObject> userList;

    public UserListAdapters(ArrayList<UserObject> userList){
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        UserListViewHolder rcv = new UserListViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(UserListAdapters.UserListViewHolder holder, final int position) {
        holder.mService.setText(userList.get(position).getmService());
        Picasso.get().load(userList.get(position).getmPicture()).resize(365, 333).into(holder.serviceImage);

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();

                FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                        .child("School Of Design").child("Students").child("Users").child(FirebaseAuth.getInstance().getUid())
                        .child("chat").child(key).setValue(true);
                FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                        .child("School Of Design").child("Students").child("Users").child(userList.get(position).getmUid()).
                        child("chat").child(key).setValue(true);

            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public class UserListViewHolder extends RecyclerView.ViewHolder{
        public TextView mService;
        public ImageView serviceImage;
        public LinearLayout mLayout;
        public UserListViewHolder(View view){
            super(view);
            mService = view.findViewById(R.id.service);
            serviceImage = view.findViewById(R.id.serviceImage);
        }
    }


}
*/
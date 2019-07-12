package com.example.andrewspc.connectv6.User;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.andrewspc.connectv6.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;



//////////////////////// THIS CODE HERE IS NOT IN USE ////////////////////////
/*
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    ArrayList<UserObject> userList;

    public UserListAdapter(ArrayList<UserObject> userList) {
        this.userList = userList;
    }

    @Override
    public UserListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        UserListViewHolder rcv = new UserListViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(UserListViewHolder holder, final int position) {

        holder.mName.setText(userList.get(position).getName());

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();

                //FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("chat").child(key).setValue(true);
                //FirebaseDatabase.getInstance().getReference().child("user").child(userList.get(position).getUid()).child("chat").child(key).setValue(true);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    */
/*
    public class UserListViewHolder  extends RecyclerView.ViewHolder{
        public TextView mName, mPhone;
        public LinearLayout mLayout;
        public UserListViewHolder(View view){
            super(view);
            mLayout = view.findViewById(R.id.layout);
            mName = view.findViewById(R.id.name);
        }
    }
}

*/
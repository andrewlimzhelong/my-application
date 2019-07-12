package com.example.andrewspc.connectv6.Chat;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.andrewspc.connectv6.IndividualActivity;
import com.example.andrewspc.connectv6.Portfolio;
import com.example.andrewspc.connectv6.PostImagesActivity;
import com.example.andrewspc.connectv6.Profile;
import com.example.andrewspc.connectv6.R;
import com.example.andrewspc.connectv6.SecondAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

//////////////////////////////////////////////////////////////////////////////////// OLD CODE BELOW ////////////////////////////////////////////////////////////////////////////////

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolders> {

    Context context;
    ArrayList<ChatObject> ChatList;

    //Youtube Links : https://www.youtube.com/watch?v=vpObpZ5MYSE&t=450s
    //Youtube Links : https://www.youtube.com/watch?v=Zd0TUuoPP-s
    //Youtube Links : https://www.youtube.com/watch?v=ZXoGG2XTjzU&t=64s
    //Stopped at 13:19 minutes

    //Firebase Database
    private FirebaseUser mCurrentUser;

    //Image Retrieve
    private StorageReference mImageStorage;

    private Button newPost;


    //// Database Reference For retrieving stored portfolio images
    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<ChatObject> list;
    SecondAdapter adapterTwo;

    public ChatAdapter(Context c, ArrayList<ChatObject> p)
    {
        context = c;
        ChatList = p;
    }

    @NonNull
    @Override
    public ChatAdapter.ChatViewHolders onCreateViewHolder(@NonNull ViewGroup parent, final int position) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);

        ChatViewHolders rcv = new ChatViewHolders(v);
        return rcv;
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ChatViewHolders holder, final int position) {

        //holder.mTitle.setText(ChatList.get(position).getChatID());

        holder.specificUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return ChatList.size();
    }

    public static class ChatViewHolders extends RecyclerView.ViewHolder{
        private LinearLayout specificUser;
        private TextView mTitle;
        private TextView courseName;
        private TextView sender;


        public ChatViewHolders(View itemView){
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.title);
            specificUser = (LinearLayout) itemView.findViewById(R.id.layout);

        }

    }
}



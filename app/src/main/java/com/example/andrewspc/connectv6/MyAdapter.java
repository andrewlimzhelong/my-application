package com.example.andrewspc.connectv6;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andrewspc.connectv6.Chat.ChatActivity;
import com.example.andrewspc.connectv6.Chat.ChatObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//////////////////////////////////////////////////////////////////////////////////// OLD CODE BELOW ////////////////////////////////////////////////////////////////////////////////

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Profile> profiles;

    //Youtube Links : https://www.youtube.com/watch?v=vpObpZ5MYSE&t=450s
    //Youtube Links : https://www.youtube.com/watch?v=Zd0TUuoPP-s
    //Youtube Links : https://www.youtube.com/watch?v=ZXoGG2XTjzU&t=64s
    //Stopped at 13:19 minutes

    //Firebase Database
    private FirebaseUser mCurrentUser;

    //Image Retrieve
    private StorageReference mImageStorage;

    private Button newPost;


    /// Database Reference For retrieving stored portfolio images
    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<Portfolio> list;
    SecondAdapter adapterTwo;

    public MyAdapter(Context c, ArrayList<Profile> p)
    {
        context = c;
        profiles = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int position) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.cardview, parent, false);
        final MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.name.setText(profiles.get(position).getName());
        holder.courseName.setText(profiles.get(position).getBio());
        holder.mClass.setText(profiles.get(position).getOccupation());
        holder.description.setText(profiles.get(position).getSkills());
        Picasso.get().load(profiles.get(position).getProfilePicture()).into(holder.profilePic);

        // Chat Function


        holder.specificUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, IndividualActivity.class);
                intent.putExtra("name", profiles.get(position).getName());
                intent.putExtra("price", profiles.get(position).getPricing());
                intent.putExtra("skills", profiles.get(position).getSkills());
                intent.putExtra("image", profiles.get(position).getProfilePicture());
                intent.putExtra("portfolioPic1", profiles.get(position).getPortfolioPic1());
                intent.putExtra("userIDOfClickedPerson", profiles.get(position).getUserKeyId());

                /////////////////////////////////////////////// EXTRA CODE //////////////////////////////////////////////////////
                final String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();

                FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                        .child("School Of Design").child("Students").child("Users").child(FirebaseAuth.getInstance().getUid())
                        .child("chat").child(key).setValue(true);
                FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                        .child("School Of Design").child("Students").child("Users").child(profiles.get(position).getUserKeyId())
                        .child("chat").child(key).setValue(true);

                intent.putExtra("chatID", key);
                ////////////////////////////////////////////// EXTRA CODE //////////////////////////////////////////////////////

                Intent intent2 = new Intent(context, PostImagesActivity.class);
                intent2.putExtra("userKeyId", profiles.get(position).getUserKeyId());

                // Loading the image each user posts into a recyclerview
                //intent.putExtra("newPost", profiles.get(position).getPortfolioPic1());

                // Start the new activity
                context.startActivity(intent);

                //Testing the displaying of message for the particular user we want to target
                //Toast.makeText(context, "This works!"+String.valueOf(vHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private ConstraintLayout specificUser;
        private TextView name;
        private TextView courseName;
        private TextView mClass;
        private TextView description;
        private ImageView profilePic;

        public MyViewHolder(View itemView){
            super(itemView);

            mClass = (TextView) itemView.findViewById(R.id.classDisplay);
            name = (TextView) itemView.findViewById(R.id.name);
            courseName = (TextView) itemView.findViewById(R.id.course);
            description = (TextView) itemView.findViewById(R.id.description);
            profilePic = (ImageView) itemView.findViewById(R.id.profilePic);

            //For Displaying the individual user profile upon click
            specificUser = (ConstraintLayout) itemView.findViewById(R.id.profileListView);

        }

    }

}

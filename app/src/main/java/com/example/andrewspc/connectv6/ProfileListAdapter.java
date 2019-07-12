package com.example.andrewspc.connectv6;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrewspc.connectv6.Chat.ChatActivity;
import com.example.andrewspc.connectv6.Chat.ChatObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.MyViewHolder>{

    String keyOfChat;
    private boolean match = false;
    private boolean noMatch = false;
    boolean tfBool = true;

    private String userName;

    private DatabaseReference mDatabaseRef2;

    Context context;
    ArrayList<postedServicesModelClass> postedServicesModelClass;

    //Youtube Links : https://www.youtube.com/watch?v=vpObpZ5MYSE&t=450s
    //Youtube Links : https://www.youtube.com/watch?v=Zd0TUuoPP-s
    //Youtube Links : https://www.youtube.com/watch?v=ZXoGG2XTjzU&t=64s
    //Stopped at 13:19 minutes

    //Firebase Database
    private FirebaseUser mCurrentUser;

    //Image Retrieve
    private StorageReference mImageStorage;

    private Button newPost;

    ArrayList<ChatObject> chatList;

    /// Database Reference For retrieving stored portfolio images
    DatabaseReference reference;
    RecyclerView recyclerView;

    //ArrayList<postedServicesModelClass> list;
    //ThirdAdapter adapterThird;

    ArrayList<Profile> profileInfo;

    public ProfileListAdapter(Context c, ArrayList<postedServicesModelClass> p, ArrayList<Profile> pf)
    {
        context = c;
        postedServicesModelClass = p;
        profileInfo = pf;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int position) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.profilepostscardview, parent, false);
        final MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.categoryOfService.setText(postedServicesModelClass.get(position).getCategory());
        holder.title.setText(postedServicesModelClass.get(position).getTitle());
        holder.pricing.setText(postedServicesModelClass.get(position).getPricing());
        holder.decimal.setText(postedServicesModelClass.get(position).getDecimal());
        holder.yellowText.setText(postedServicesModelClass.get(position).getProfile());
        Picasso.get().load(postedServicesModelClass.get(position).getProfilePicture()).fit().into(holder.userProfilePic);
        Picasso.get().load(postedServicesModelClass.get(position).getportfolioPic1()).fit().into(holder.servicePic);
        //Picasso.get().load(profileInfo.get(position).getProfilePicture()).into(holder.profileImageOfUser);

        holder.specificService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final Intent intent = new Intent(context, ProfilePostsView.class);
                //intent.putExtra("nameSeleceted", postedServicesModelClass.get(position).getService());
                intent.putExtra("priceOfService", postedServicesModelClass.get(position).getPricing());
                intent.putExtra("ItemTitle", postedServicesModelClass.get(position).getTitle());
                intent.putExtra("ItemDesc", postedServicesModelClass.get(position).getDetails());
                intent.putExtra("Username", postedServicesModelClass.get(position).getProfile());
                intent.putExtra("imagePosted", postedServicesModelClass.get(position).getportfolioPic1());
                intent.putExtra("contactNumber", postedServicesModelClass.get(position).getHPcontact());
                intent.putExtra("Officenumber", postedServicesModelClass.get(position).getofficeNum());
                intent.putExtra("decimalAmt", postedServicesModelClass.get(position).getDecimal());
                intent.putExtra("profileImage", postedServicesModelClass.get(position).getProfilePicture());
                intent.putExtra("locationName", postedServicesModelClass.get(position).getCountryLocation());
                intent.putExtra("stateName", postedServicesModelClass.get(position).getStateLocation());
                intent.putExtra("companyName", postedServicesModelClass.get(position).getYourCompany());
                intent.putExtra("companyDesc", postedServicesModelClass.get(position).getYourCompanyDesc());
                intent.putExtra("Date", postedServicesModelClass.get(position).getDate());
                intent.putExtra("Time", postedServicesModelClass.get(position).getTime());
                intent.putExtra("Category", postedServicesModelClass.get(position).getCategory());
                //intent.putExtra("userID", postedServicesModelClass.get(position).getUserID());

                ///////////////////////////////////////////////// EXTRA CODE //////////////////////////////////////////////////////

                /////////////////////////////////////////////// CHAT KEYS ARE ALL HERE ////////////////////////////////////////////////
                // LINK FOR THIS CODE : https://github.com/SimCoderYoutube/WhatsAppClone/blob/master/app/src/main/java/com/simcoder/whatsappclone/MainPageActivity.java

                ////////////////////////////////////////////////////////////////// NEW CODE HERE caa 29-5-2019 /////////////////////////////////////////////////////////////////////////

                String Name1 = postedServicesModelClass.get(position).getProfile();
                String ImageOfUser = postedServicesModelClass.get(position).getProfilePicture();
                String clickedItemID = postedServicesModelClass.get(position).getUserID();
                String imageKey = postedServicesModelClass.get(position).getImageUniqueKey();

                intent.putExtra("clickedItem", imageKey);
                intent.putExtra("Username", Name1);
                intent.putExtra("UserImage", ImageOfUser);

                /////////////////////////////////////////////////////// NEW CODE HERE caa 29-5-2019 //////////////////////////////////////////////////////////////////////////////////

                ////////////////////////////////////////////// EXTRA CODE //////////////////////////////////////////////////////
                // Start the new activity

                //Testing the displaying of message for the particular user we want to target
                //Toast.makeText(context, "This works!"+String.valueOf(vHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();

                context.startActivity(intent);

            }
        });

    }


    private void createChat(int position){
        String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();

        DatabaseReference userDb =  FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                .child("School Of Design").child("Students").child("Users");

        userDb.child(FirebaseAuth.getInstance().getUid()).child("chat").child(key).setValue(true);
        userDb.child(postedServicesModelClass.get(position).getUserID()).child("chat").child(key).setValue(true);
    }

    public void passinggGlobal(String globalID){
        final Intent intent = new Intent(context, ServiceSelected.class);
        intent.putExtra("chatID", globalID);
    }


    @Override
    public int getItemCount() {
        return postedServicesModelClass.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private CardView specificService;
        private TextView decimal;
        private TextView title;
        private TextView pricing;
        private TextView yellowText;
        private TextView categoryOfService;
        private ImageView userProfilePic;
        private ImageView servicePic;
        private Button deleteBtn;

        public MyViewHolder(View itemView){
            super(itemView);

            categoryOfService = (TextView) itemView.findViewById(R.id.categoryOfService);
            yellowText = (TextView) itemView.findViewById(R.id.yellowText);
            decimal = (TextView) itemView.findViewById(R.id.decimal);
            title = (TextView) itemView.findViewById(R.id.TitleOfItem);
            pricing = (TextView) itemView.findViewById(R.id.PricingDisplay);
            servicePic = (ImageView) itemView.findViewById(R.id.ServiceImageUploaded);
            userProfilePic = (ImageView) itemView.findViewById(R.id.userProfilePic);
            specificService = (CardView) itemView.findViewById(R.id.postedServicesPage);

        }

    }

}
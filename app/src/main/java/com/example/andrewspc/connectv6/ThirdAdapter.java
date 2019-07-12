package com.example.andrewspc.connectv6;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ThirdAdapter extends RecyclerView.Adapter<ThirdAdapter.MyViewHolder>{

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

    ArrayList<postedServicesModelClass> profileInfo;

    public ThirdAdapter(Context c, ArrayList<postedServicesModelClass> p, ArrayList<postedServicesModelClass> pf)
    {
        context = c;
        postedServicesModelClass = p;
        profileInfo = pf;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int position) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.postedservicecardview, parent, false);
        final MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.categoryOfService.setText(postedServicesModelClass.get(position).getCategory());
        holder.DescriptionOfPost.setText(postedServicesModelClass.get(position).getDetails());
        holder.title.setText(postedServicesModelClass.get(position).getTitle());
        holder.pricing.setText(postedServicesModelClass.get(position).getPricing());
        holder.decimal.setText(postedServicesModelClass.get(position).getDecimal());
        holder.yellowText.setText(postedServicesModelClass.get(position).getProfile());
        Picasso.get().load(postedServicesModelClass.get(position).getportfolioPic1()).fit().into(holder.servicePic);
        Picasso.get().load(postedServicesModelClass.get(position).getProfilePicture()).into(holder.userProfilePic);

        holder.specificService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final Intent intent = new Intent(context, ServiceSelected.class);
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
                intent.putExtra("category", postedServicesModelClass.get(position).getCategory());
                intent.putExtra("timeOfPost", postedServicesModelClass.get(position).getTime());
                intent.putExtra("dateOfPost", postedServicesModelClass.get(position).getDate());
                //intent.putExtra("userID", postedServicesModelClass.get(position).getUserID());

                ///////////////////////////////////////////////// EXTRA CODE //////////////////////////////////////////////////////

                /////////////////////////////////////////////// CHAT KEYS ARE ALL HERE ////////////////////////////////////////////////
                // LINK FOR THIS CODE : https://github.com/SimCoderYoutube/WhatsAppClone/blob/master/app/src/main/java/com/simcoder/whatsappclone/MainPageActivity.java

                ////////////////////////////////////////////////////////////////// NEW CODE HERE caa 29-5-2019 /////////////////////////////////////////////////////////////////////////

                String Name1 = postedServicesModelClass.get(position).getProfile();
                String ImageOfUser = postedServicesModelClass.get(position).getProfilePicture();
                String chatID = postedServicesModelClass.get(position).getUserID();

                intent.putExtra("userChatId", chatID);
                intent.putExtra("Username", Name1);
                intent.putExtra("UserImage", ImageOfUser);

                /////////////////////////////////////////////////////// NEW CODE HERE caa 29-5-2019 //////////////////////////////////////////////////////////////////////////////////

                ////////////////////////////////////////////// EXTRA CODE //////////////////////////////////////////////////////
                // Start the new activity

                    //Testing the displaying of message for the particular user we want to target
                    //Toast.makeText(context, "This works!"+String.valueOf(vHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();


                final Intent intent2 = new Intent(context, ChatActivity.class);
                intent2.putExtra("userChatId", chatID);

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
        private TextView categoryOfService;
        private TextView DescriptionOfPost;
        private TextView yellowText;
        private ImageView servicePic;
        private ImageView userProfilePic;
        private ImageView ImageForCategory;

        private TextView DateOfPost;
        private TextView TimeOfPost;

        public MyViewHolder(View itemView){
            super(itemView);

            DateOfPost = (TextView) itemView.findViewById(R.id.DateOfPost);
            TimeOfPost = (TextView) itemView.findViewById(R.id.TimeOfPost);

            yellowText = (TextView) itemView.findViewById(R.id.yellowText);
            DescriptionOfPost = (TextView) itemView.findViewById(R.id.DescriptionOfPost);
            decimal = (TextView) itemView.findViewById(R.id.decimal);
            categoryOfService = (TextView) itemView.findViewById(R.id.categoryOfService);
            title = (TextView) itemView.findViewById(R.id.TitleOfItem);
            pricing = (TextView) itemView.findViewById(R.id.PricingDisplay);
            servicePic = (ImageView) itemView.findViewById(R.id.ServiceImageUploaded);
            userProfilePic = (ImageView) itemView.findViewById(R.id.userProfilePic);
            ImageForCategory = (ImageView) itemView.findViewById(R.id.ImageForCategory);
            specificService = (CardView) itemView.findViewById(R.id.postedServicesPage);

        }

    }

}






























/*public class ThirdAdapter extends RecyclerView.Adapter<ThirdAdapter.MyViewHolder>{

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
    ArrayList<postedServicesModelClass> list;
    ThirdAdapter adapterThird;


    public ThirdAdapter(Context c, ArrayList<postedServicesModelClass> p)
    {
        context = c;
        postedServicesModelClass = p;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int position) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.postedservicecardview, parent, false);
        final MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.userId.setText(postedServicesModelClass.get(position).getUserID());
        holder.service.setText(postedServicesModelClass.get(position).getService());
        //holder.pricing.setText(postedServicesModelClass.get(position).getPricing());
        //holder.reviews.setText(postedServicesModelClass.get(position).getReviews());
        Picasso.get().load(postedServicesModelClass.get(position).getportfolioPic1()).resize(365, 333).into(holder.servicePic);


        holder.specificService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent intent = new Intent(context, ServiceSelected.class);
                intent.putExtra("nameSeleceted", postedServicesModelClass.get(position).getService());
                intent.putExtra("priceOfService", postedServicesModelClass.get(position).getPricing());
                intent.putExtra("reviewOfService", postedServicesModelClass.get(position).getReviews());
                intent.putExtra("imagePosted", postedServicesModelClass.get(position).getportfolioPic1());
                intent.putExtra("userID", postedServicesModelClass.get(position).getUserID());


                ///////////////////////////////////////////////// EXTRA CODE //////////////////////////////////////////////////////

                /////////////////////////////////////////////// CHAT KEYS ARE ALL HERE ////////////////////////////////////////////////
                // LINK FOR THIS CODE : https://github.com/SimCoderYoutube/WhatsAppClone/blob/master/app/src/main/java/com/simcoder/whatsappclone/MainPageActivity.java

                String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();

                FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                        .child("School Of Design").child("Students").child("Users").child(FirebaseAuth.getInstance().getUid())
                        .child("chat").child(key).setValue(true);
                FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                        .child("School Of Design").child("Students").child("Users").child(postedServicesModelClass.get(position).getUserID())
                        .child("chat").child(key).setValue(true);

                intent.putExtra("chatID", key);

                ////////////////////////////////////////////// EXTRA CODE //////////////////////////////////////////////////////
                // Start the new activity
                context.startActivity(intent);

                //Testing the displaying of message for the particular user we want to target
                //Toast.makeText(context, "This works!"+String.valueOf(vHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return postedServicesModelClass.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private ConstraintLayout specificService;
        private TextView service;
        private TextView pricing;
        private TextView reviews;
        private TextView userId;
        private ImageView servicePic;

        public MyViewHolder(View itemView){
            super(itemView);

            service = (TextView) itemView.findViewById(R.id.ServiceProvided);
            //pricing = (TextView) itemView.findViewById(R.id.PricingDisplayed);
            //reviews = (TextView) itemView.findViewById(R.id.ReviewEntered);
            userId = (TextView) itemView.findViewById(R.id.userEnterId);
            servicePic = (ImageView) itemView.findViewById(R.id.ServiceImageUploaded);

            //For Displaying the individual user profile upon click
            specificService = (ConstraintLayout) itemView.findViewById(R.id.postedServicesPage);
        }

    }

}
*/

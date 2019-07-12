package com.example.andrewspc.connectv6;

import android.app.Service;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrewspc.connectv6.Chat.ChatActivity;
import com.example.andrewspc.connectv6.Chat.ChatObject;
import com.facebook.login.LoginManager;
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

import java.util.ArrayList;

public class ServiceSelected extends AppCompatActivity {

    private TextView serviceName;
    private TextView servicePrice;
    private ImageView serviceImage;
    private TextView contactNum;
    private TextView Officenumber;
    private TextView decimalAmt;
    private ImageView profilePictureOfUser;
    private TextView countryLocation;
    private TextView stateLocation;
    private TextView detailDesc;
    private TextView userInputName;

    private TextView yourCompany;
    private TextView yourCompanyDesc;

    private TextView dateOfPost;
    private TextView timeOfPost;

    private Button messageBtn;

    //Firebase Database
    private DatabaseReference mDatabase;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseRefToClickedUser;

    //Image Retrieve
    private StorageReference mImageStorage;
    private ImageView backButton;

    private TextView textCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_selected);

        backButton = findViewById(R.id.backArrow);

        textCategory = findViewById(R.id.textCategory);

        //For the top app bar of the app
        /*
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Services");
        */
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        messageBtn = findViewById(R.id.messageBtn);
        contactNum = findViewById(R.id.textContactDes);
        Officenumber = findViewById(R.id.Officenumber);

        serviceName = findViewById(R.id.serviceDescription);
        servicePrice = findViewById(R.id.pricingAmt);
        //serviceReview = findViewById(R.id.reviewGiven);
        serviceImage = findViewById(R.id.postImageDisplayView);
        profilePictureOfUser = findViewById(R.id.imageSmallFlag);
        countryLocation = findViewById(R.id.countryLocation);
        stateLocation = findViewById(R.id.stateLocation);

        detailDesc = findViewById(R.id.textDetails2);
        userInputName = findViewById(R.id.textUsername);
        decimalAmt = findViewById(R.id.decimal);

        yourCompany = findViewById(R.id.yourCompany);
        yourCompanyDesc = findViewById(R.id.yourCompanyDesc);

        dateOfPost = findViewById(R.id.DateOfPost);
        timeOfPost = findViewById(R.id.TimeOfPost);

        //Retrieving the data from ThirdAdapter class
        final Intent intent = getIntent();
        String Name = intent.getExtras().getString("ItemTitle");
        String Price = intent.getExtras().getString("priceOfService");
        String descOfPost = intent.getExtras().getString("ItemDesc");
        String Image = intent.getExtras().getString("imagePosted");
        String username = intent.getExtras().getString("Username");
        String hpContact = intent.getExtras().getString("contactNumber");
        String officeNumber = intent.getExtras().getString("Officenumber");
        String decimal = intent.getExtras().getString("decimalAmt");
        String profilePicture = intent.getExtras().getString("profileImage");
        String locationName = intent.getExtras().getString("locationName");
        String stateName = intent.getExtras().getString("stateName");
        String companyName = intent.getExtras().getString("companyName");
        String companyDesc = intent.getExtras().getString("companyDesc");
        String category = intent.getExtras().getString("category");
        String datePost = intent.getExtras().getString("dateOfPost");
        String timePost = intent.getExtras().getString("timeOfPost");

        // FOR the message part of the application
        final String chatID = intent.getExtras().getString("chatID");
        final String idOfClickedUser = intent.getExtras().getString("userChatId");
        Intent intent2 = new Intent(ServiceSelected.this, ChatActivity.class);
        // FOR the message part of the application

        serviceName.setText(Name);
        servicePrice.setText(Price);
        detailDesc.setText(descOfPost);
        userInputName.setText(username);
        contactNum.setText(hpContact);
        Officenumber.setText(officeNumber);
        decimalAmt.setText(decimal);
        countryLocation.setText(locationName);
        stateLocation.setText(stateName);
        yourCompany.setText(companyName);
        yourCompanyDesc.setText(companyDesc);
        textCategory.setText(category);
        dateOfPost.setText(datePost);
        timeOfPost.setText(timePost);

        Picasso.get().load(Image).fit().into(serviceImage);
        Picasso.get().load(profilePicture).fit().into(profilePictureOfUser);

        /*
        final Intent intent38 = getIntent();
        String ImageOfUser = intent38.getExtras().getString("UserImage");
        String clickedUserId = intent38.getExtras().getString("userChatId");

        Toast.makeText(ServiceSelected.this, ImageOfUser, Toast.LENGTH_SHORT).show();
        Intent chatIntention = new Intent(ServiceSelected.this, ChatActivity.class);
        chatIntention.putExtra("chatImageOfUser", ImageOfUser);
        */

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ServiceSelected.this, PostedServices.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chatKeyGenerator();

            }
        });

    }

    public void chatKeyGenerator() {

        DatabaseReference refKey = FirebaseDatabase.getInstance().getReference().child("chat");

            refKey.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final Intent intent45 = getIntent();
                        String Name1 = intent45.getExtras().getString("Username");

                        String ImageOfUser = intent45.getExtras().getString("UserImage");
                        String clickedUserId = intent45.getExtras().getString("userChatId");

                        final String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();

                        boolean keyPresent = false;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            String currentUsersId = snapshot.child("currentUserId").getValue().toString();
                            String selectedUsersId = snapshot.child("selecteduserId").getValue().toString();

                            if(currentUsersId.equals(FirebaseAuth.getInstance().getUid()) && selectedUsersId.equals(clickedUserId)
                                    || selectedUsersId.equals(FirebaseAuth.getInstance().getUid()) && currentUsersId.equals(clickedUserId)){

                                String presentChatId = snapshot.getKey();

                                keyPresent = true;

                                Intent intent = new Intent(ServiceSelected.this, ChatActivity.class);
                                intent.putExtra("chatIDD", presentChatId);
                                intent.putExtra("chatImageOfUser", ImageOfUser);
                                intent.putExtra("chatUsername", Name1);
                                startActivity(intent);
                                break;
                            }

                        }

                        if(!keyPresent){

                            DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                                    .child("School Of Design").child("Students").child("Users").child(FirebaseAuth.getInstance().getUid())
                                    .child("chat");

                            FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                                    .child("School Of Design").child("Students").child("Users").child(clickedUserId)
                                    .child("chat").child(key).setValue(key);

                            ///////write code to pull username of the user and
                            // the profile image of the user and display it store it into this part of the database

                            ref2.child(key).setValue(key);

                            DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference().child("chat").child(key);
                            chatRef.child("currentUserId").setValue(FirebaseAuth.getInstance().getUid());
                            chatRef.child("selecteduserId").setValue(clickedUserId);

                            Intent intent = new Intent(ServiceSelected.this, ChatActivity.class);
                            intent.putExtra("chatIDD", key);
                            intent.putExtra("chatUsername", Name1);
                            intent.putExtra("chatImageOfUser", ImageOfUser);
                            startActivity(intent);

                            // Creating Information about the user you want to talk to

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                                    .child("School Of Design").child("Students").child("Users").child(FirebaseAuth.getInstance().getUid()).child("Profile");

                            final DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                                    .child("School Of Design").child("Students").child("Users").child(clickedUserId)
                                    .child("YourChat").child(key);

                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    if (snapshot.hasChild("UniqueName") && snapshot.hasChild("ProfilePicture")) {

                                        final String Name = snapshot.child("UniqueName").getValue().toString();
                                        final String ProfilePic = snapshot.child("ProfilePicture").getValue().toString();

                                        ref3.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                ref3.child("Username").setValue(Name);
                                                ref3.child("ImageOfUser").setValue(ProfilePic);
                                                ref3.child("UserUniqueID").setValue(key);

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            /*
                            ref3.child("Username").setValue(Name1);
                            ref3.child("ImageOfUser").setValue(ImageOfUser);
                            ref3.child("UserUniqueID").setValue(key);
                            */

                            DatabaseReference ref35 = FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                                    .child("School Of Design").child("Students").child("Users").child(FirebaseAuth.getInstance().getUid())
                                    .child("YourChat").child(key);

                            ref35.child("Username").setValue(Name1);
                            ref35.child("ImageOfUser").setValue(ImageOfUser);
                            ref35.child("UserUniqueID").setValue(key);

                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    //// For the top app bar of the app
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        return true;
    }

    //// For the top app bar of the app
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.item3:
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Intent loginIntent = new Intent(ServiceSelected.this, LoginPage.class);
                startActivity(loginIntent);
        }
        return super.onOptionsItemSelected(item);
    }

}

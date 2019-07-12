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

public class ProfilePostsView extends AppCompatActivity {

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

    private TextView DateOfPost;
    private TextView TimeOfPost;
    private TextView textCategory;

    //Firebase Database
    private DatabaseReference mDatabase;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseRefToClickedUser;

    //Image Retrieve
    private StorageReference mImageStorage;

    private ImageView backButton;

    private Button deleteBtn;
    String clickedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_posts_view);

        backButton = findViewById(R.id.backArrow);

        //For the top app bar of the app
        /*
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile Posts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        */

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

        DateOfPost = findViewById(R.id.DateOfPost);
        TimeOfPost = findViewById(R.id.TimeOfPost);

        textCategory = findViewById(R.id.textCategory);

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
        String Date = intent.getExtras().getString("Date");
        String Time = intent.getExtras().getString("Time");
        String Category = intent.getExtras().getString("Category");

        // FOR the message part of the application
        final String chatID = intent.getExtras().getString("chatID");
        final String idOfClickedUser = intent.getExtras().getString("userChatId");
        Intent intent2 = new Intent(ProfilePostsView.this, ChatActivity.class);
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
        DateOfPost.setText(Date);
        TimeOfPost.setText(Time);
        textCategory.setText(Category);

        Picasso.get().load(Image).fit().into(serviceImage);
        Picasso.get().load(profilePicture).fit().into(profilePictureOfUser);

        clickedPosition = getIntent().getStringExtra("clickedItem");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ProfilePostsView.this, UserProfilePage.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
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
                Intent loginIntent = new Intent(ProfilePostsView.this, LoginPage.class);
                startActivity(loginIntent);
        }
        return super.onOptionsItemSelected(item);
    }

}

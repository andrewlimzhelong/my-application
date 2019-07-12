package com.example.andrewspc.connectv6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrewspc.connectv6.Chat.ChatActivity;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class IndividualActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    private TextView indiName;
    private TextView indiPrice;
    private TextView indiSkills;
    private ImageView indiImage;

    //Firebase Database
    private DatabaseReference mDatabase;
    private FirebaseUser mCurrentUser;
    private Button messageBtn;

    //Image Retrieve
    private StorageReference mImageStorage;

    private TextView indiChatId;

    ////////////////////////////////////////////// CODE FROM HERE ONWARD IS TRYING TO LOAD POSTED IMAGES INTO EACH USERS INDIVIDUAL PROFILE /////////////////////////////////////\/\//\/\///

    DatabaseReference reference;
    DatabaseReference secReference;
    RecyclerView recyclerView;
    ArrayList<Portfolio> list;
    SecondAdapter adapterTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual);

        indiName = findViewById(R.id.indiName);
        indiPrice = findViewById(R.id.indiPrice);
        indiSkills = findViewById(R.id.indiSkill);
        indiImage = findViewById(R.id.indiDisplayPic);
        messageBtn = findViewById(R.id.messageBtn);

        //Retrieving the data from ChatAdapter class
        final Intent intent = getIntent();
        String Name = intent.getExtras().getString("name");
        String Price = intent.getExtras().getString("price");
        String Skills = intent.getExtras().getString("skills");
        String Image = intent.getExtras().getString("image");
        final String userClicked = intent.getExtras().getString("userIDOfClickedPerson");

        ///TESTING
        final String chatID = intent.getExtras().getString("chatID");
        Intent intent2 = new Intent(IndividualActivity.this, ChatActivity.class);
        //intent.putExtra("chatIDD", chatID);

        indiChatId = findViewById(R.id.chatId);
        indiChatId.setText(chatID);

        ///TESTING
        indiName.setText(Name);
        indiPrice.setText(Price);
        indiSkills.setText(Skills);
        Picasso.get().load(Image).resize(365, 365).into(indiImage);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = mCurrentUser.getUid();

        ///////////////////////////////////////// CODE FROM HERE ONWARD IS TRYING TO LOAD POSTED IMAGES INTO EACH USERS INDIVIDUAL PROFILE ///////////////////////////////////////

        recyclerView = (RecyclerView) findViewById(R.id.myRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<Portfolio>();

        reference = FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                .child("School Of Design").child("Students").child("Photos").child(uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //EXTRA CODE
                /*
                if(dataSnapshot.exists()) {
                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();
                    reference.child(dataSnapshot.getKey());
                }
                */
                //EXTRA CODE

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    Portfolio p = dataSnapshot1.getValue(Portfolio.class);
                    list.add(p);
                }

                adapterTwo = new SecondAdapter(IndividualActivity.this, list);

                recyclerView.setAdapter(adapterTwo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(IndividualActivity.this, "There is an error somewhere", Toast.LENGTH_SHORT).show();

            }
        });

        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(IndividualActivity.this, ChatActivity.class);
                intent.putExtra("chatIDD", chatID);
                startActivity(intent);

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
                Intent loginIntent = new Intent(IndividualActivity.this, LoginPage.class);
                startActivity(loginIntent);
        }
        return super.onOptionsItemSelected(item);
    }

}

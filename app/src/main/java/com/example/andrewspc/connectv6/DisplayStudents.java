package com.example.andrewspc.connectv6;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.andrewspc.connectv6.Utils.BottomNavigationViewHelper;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

public class DisplayStudents extends AppCompatActivity {


 //////////////////////////////////////////////////////////////////////////////////// OLD CODE ////////////////////////////////////////////////////////////////////////////////////

    //Youtube Link : https://www.youtube.com/watch?v=vpObpZ5MYSE&t=450s
    //Stopped at 13:19 minutes

    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<Profile> list;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_students);

        setupBottomNavigatonView();

        recyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        list = new ArrayList<Profile>();

        //For the top app bar of the app
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Services");

            reference = FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                    .child("School Of Design").child("Students").child("Users");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {
                        Profile p = dataSnapshot1.getValue(Profile.class);
                        list.add(p);
                    }
                    adapter = new MyAdapter(DisplayStudents.this, list);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(DisplayStudents.this, "Error something is not right", Toast.LENGTH_SHORT).show();
                }
            });

    }

    /*
    Bottom Navigation View Setup
    */
    private void setupBottomNavigatonView(){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(DisplayStudents.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
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
                Intent loginIntent = new Intent(DisplayStudents.this, StartActivity.class);
                startActivity(loginIntent);


        }
        return super.onOptionsItemSelected(item);
    }

}

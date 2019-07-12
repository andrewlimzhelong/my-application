package com.example.andrewspc.connectv6;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.model.Model;
import com.example.andrewspc.connectv6.Chat.ChatActivity;
import com.example.andrewspc.connectv6.Utils.BottomNavigationViewHelper;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfilePage extends AppCompatActivity {

    private TextView setUpName;
    private TextView setUpSkills;
    private ImageView accountProfilePic;
    private CircleImageView DisplayPic2;

    private TextView phoneNumberHP;
    private TextView officeNumber;

    //Firebase Database
    private FirebaseUser mCurrentUser;

    //Image Retrieve
    private StorageReference mImageStorage;

    // Database Reference from posted services page
    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<postedServicesModelClass> list;
    ProfileListAdapter adapterThree;
    ArrayList<Profile> profileList;

    /// Database Reference For retrieving stored portfolio images
    //DatabaseReference reference;
    //RecyclerView recyclerView;
    //ArrayList<Portfolio> list;
    //SecondAdapter adapterTwo;

    // Database reference for retrieving user profile information
    private DatabaseReference mDatabase;

    private Button sellButton;
    private static final int GALLERY_REQUEST = 1;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_page);

        sellButton = findViewById(R.id.sellBtn);

        //For the top app bar of the app
        /*
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        */

        setupBottomNavigatonView();

        // Setting up all the profile text field
        setUpName = findViewById(R.id.userSavedName);
        setUpSkills = findViewById(R.id.userSavedSkills);
        accountProfilePic = findViewById(R.id.userDisplayPic);
        DisplayPic2 = findViewById(R.id.DisplayPic);
        phoneNumberHP = findViewById(R.id.Hpnum);
        officeNumber =findViewById(R.id.OfficeNum);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = mCurrentUser.getUid();

        recyclerView = (RecyclerView) findViewById(R.id.myRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        list = new ArrayList<postedServicesModelClass>();

        accountProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String status_value = setUpName.getText().toString();
                String statusV2 = setUpSkills.getText().toString();

                Intent setupIntent = new Intent(UserProfilePage.this, SettingsActivity.class);
                setupIntent.putExtra("status_value", status_value);
                setupIntent.putExtra("status_value2", statusV2);
                setupIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(setupIntent);



            }
        });

        /////////////////////////////////// CODE FROM HERE ONWARDS IS FOR RETRIEVING THE INFORMATION FOR EACH USER'S PROFILE //////////////////////////////////////
        mDatabase = FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                .child("School Of Design").child("Students").child("Users").child(uid).child("Profile");

        retrieveFirebaseIntoTextView("UniqueName", setUpName, mDatabase);
        retrieveFirebaseIntoTextView("HPcontact", phoneNumberHP, mDatabase);
        retrieveFirebaseIntoTextView("officeNum", officeNumber, mDatabase);
        retrieveFirebaseIntoTextView("Bio", setUpSkills, mDatabase);
        //retrieveFirebaseIntoTextView("CountryLocation", setUpSkills, reference);
        //retrieveFirebaseIntoTextView("StateLocation", setUpSkills, reference);

        SettingsActivity settingsActivity = new SettingsActivity();
        settingsActivity.retrievingPicture("ProfilePicture", DisplayPic2, mDatabase);

        getUserPosts();

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), GALLERY_REQUEST);

            }
        });
    }

    public void getUserPosts(){

        reference = FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                .child("School Of Design").child("Students").child("Users").child(FirebaseAuth.getInstance().getUid()).child("Photos");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    postedServicesModelClass p = dataSnapshot1.getValue(postedServicesModelClass.class);
                    list.add(p);
                }
                Collections.reverse(list);
                adapterThree = new ProfileListAdapter(UserProfilePage.this, list, profileList);
                recyclerView.setAdapter(adapterThree);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(UserProfilePage.this, "Error something is not right", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(UserProfilePage.this, PostedServices.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }

    public void retrieveFirebaseIntoTextView(final String leftTitle, final TextView anotherTextView, final DatabaseReference databaseReference) {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(leftTitle)) {

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String Name = dataSnapshot.child(leftTitle).getValue().toString();
                            anotherTextView.setText(Name);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Image Cropper and posting code
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){

            filePath = data.getData();
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .setMinCropResultSize(350, 350)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                final Intent intent = new Intent(UserProfilePage.this, HomeScreen.class);
                intent.putExtra("imageSelected", resultUri);
                startActivity(intent);

                //postImage.setImageURI(resultUri);
                //filePath = resultUri;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


        /*
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){

            if (data.getClipData() != null){

                int totalItemsSelected = data.getClipData().getItemCount();

                for (int i = 0; i < totalItemsSelected; i++){

                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    String fileName = getFileName(fileUri);
                    fileDoneList.add(fileName);

                    Toast.makeText(PostedServices.this, fileDoneList.toString(), Toast.LENGTH_SHORT).show();
                }

            }else if (data.getData() != null){

                Toast.makeText(PostedServices.this, "Selected Single File", Toast.LENGTH_SHORT).show();

            }
        }
        */
    }



    // Bottom Navigation View Setup
    private void setupBottomNavigatonView(){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(UserProfilePage.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
    }

    //// For the top app bar of the app
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    //// For the top app bar of the app
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.item3:
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Intent loginIntent = new Intent(UserProfilePage.this, StartActivity.class);
                startActivity(loginIntent);
        }
        return super.onOptionsItemSelected(item);
    }

}


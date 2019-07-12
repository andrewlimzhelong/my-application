package com.example.andrewspc.connectv6;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView imageView;
    private EditText mSettingsName;
    private EditText mSettingsOccupation;
    private EditText settingsContactHP;
    private EditText settingsContactOffice;
    private EditText settingsCompanyName;
    private EditText settingsCompanyDescription;
    private EditText countryLocation;
    private EditText stateLocation;

    private Button mSavebtn;

    //Firebase
    private DatabaseReference mStatusDatabase;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseRef;

    private static final int GALLERY_PICK = 1;
    private StorageReference storageReference;
    private Uri filePath;
    //Image Uri Storage variable
    private String imageLink;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        backButton = findViewById(R.id.backArrow);

        ////////////// Firebase Image uploading database reference
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        //For the top app bar of the app
        /*
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        */

        //Name Edit Text Field
        mSettingsName = (EditText) findViewById(R.id.settingsName);
        mSettingsOccupation = (EditText) findViewById(R.id.settingsDescription);
        settingsContactHP = findViewById(R.id.handphoneNum);
        settingsContactOffice = findViewById(R.id.officeNum);
        settingsCompanyName = findViewById(R.id.CompanyName);
        settingsCompanyDescription = findViewById(R.id.CompanyDesc);
        countryLocation = findViewById(R.id.countryLocation);
        stateLocation = findViewById(R.id.stateLocation);

        mSavebtn = (Button) findViewById(R.id.SaveSettingsBtn);
        imageView = (CircleImageView) findViewById(R.id.profileImage2);

        //Firebase instances
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showFileChooser();
            }
        });

        //Referencing to specific value in the firebase database
        mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                .child("School Of Design").child("Students").child("Users").child(current_uid).child("Profile");

        retrivingDataFromFirebaseET("Bio", mSettingsName, mStatusDatabase);
        retrivingDataFromFirebaseET("Occupation", mSettingsOccupation, mStatusDatabase);
        retrivingDataFromFirebaseET("HPcontact", settingsContactHP, mStatusDatabase);
        retrivingDataFromFirebaseET("officeNum", settingsContactOffice, mStatusDatabase);
        retrivingDataFromFirebaseET("yourCompany", settingsCompanyName, mStatusDatabase);
        retrivingDataFromFirebaseET("yourCompanyDesc", settingsCompanyDescription, mStatusDatabase);
        retrivingDataFromFirebaseET("CountryLocation", countryLocation, mStatusDatabase);
        retrivingDataFromFirebaseET("StateLocation", stateLocation, mStatusDatabase);

        retrievingPicture("ProfilePicture",imageView, mStatusDatabase);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(SettingsActivity.this, UserProfilePage.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        /*
        mStatusDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("ProfilePicture")) {

                    mStatusDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String Image = dataSnapshot.child("ProfilePicture").getValue().toString();
                            Picasso.get().load(Image).resize(200,200).into(imageView);

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

        /////////////////////////////////////////////////////////////////////////////////// OLD CODE //////////////////////////////////////////////////////////////////////////////////

        /*
        mStatusDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("Bio")) {

                    mStatusDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String Name = dataSnapshot.child("Bio").getValue().toString();

                            mSettingsName.setText(Name);

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

        mStatusDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("Occupation")) {

                    mStatusDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String Name = dataSnapshot.child("Occupation").getValue().toString();

                            mSettingsOccupation.setText(Name);

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

        mStatusDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("HPcontact")) {

                    mStatusDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String Name = dataSnapshot.child("HPcontact").getValue().toString();

                            settingsContactHP.setText(Name);

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

        mStatusDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("officeNum")) {

                    mStatusDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String Name = dataSnapshot.child("officeNum").getValue().toString();

                            settingsContactOffice.setText(Name);

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

        mStatusDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("yourCompany")) {

                    mStatusDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String Name = dataSnapshot.child("yourCompany").getValue().toString();

                            settingsCompanyName.setText(Name);

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

        mStatusDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("yourCompanyDesc")) {

                    mStatusDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String Name = dataSnapshot.child("yourCompanyDesc").getValue().toString();

                            settingsCompanyDescription.setText(Name);

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
        */

        /////////////////////////////////////////////////////////////////////////////////// OLD CODE ABOVE //////////////////////////////////////////////////////////////////////////////////

        // Changing data in Firebase (Editting the database real time)
        //Tutorial 11 Entire tutorial
        // YOUTUBE : https://www.youtube.com/watch?v=60nMpGRjcns&list=PLGCjwl1RrtcQ3o2jmZtwu2wXEA4OIIq53&index=11
        mSavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String saveEditedName = mSettingsName.getText().toString();
                String userDescription = mSettingsOccupation.getText().toString();
                String enteredContactHP = settingsContactHP.getText().toString();
                String enteredContactOffice = settingsContactOffice.getText().toString();
                String enteredCompanyName = settingsCompanyName.getText().toString();
                String enteredCompanyDescription = settingsCompanyDescription.getText().toString();
                String enteredLocation = countryLocation.getText().toString();
                String enteredState = stateLocation.getText().toString();

                mStatusDatabase.child("Bio").setValue(saveEditedName);
                mStatusDatabase.child("Occupation").setValue(userDescription);
                mStatusDatabase.child("HPcontact").setValue(enteredContactHP);
                mStatusDatabase.child("officeNum").setValue(enteredContactOffice);
                mStatusDatabase.child("yourCompany").setValue(enteredCompanyName);
                mStatusDatabase.child("yourCompanyDesc").setValue(enteredCompanyDescription);
                mStatusDatabase.child("CountryLocation").setValue(enteredLocation);
                mStatusDatabase.child("StateLocation").setValue(enteredState);

                uploadFile();

                Intent settingsIntent = new Intent(SettingsActivity.this, UserProfilePage.class);
                startActivity(settingsIntent);

            }
        });

    }

    public void retrievingPicture(final String databaseLeftHeader, final ImageView imageView, final DatabaseReference imageDatabaseRef){

        imageDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(databaseLeftHeader)) {

                    imageDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String Image = dataSnapshot.child(databaseLeftHeader).getValue().toString();
                            Picasso.get().load(Image).resize(200,200).into(imageView);

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


    }

    public void retrivingDataFromFirebaseET(final String leftTitle, final EditText anotherEditText, final DatabaseReference databseReference) {

        databseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(leftTitle)) {

                    databseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String Name = dataSnapshot.child(leftTitle).getValue().toString();
                            anotherEditText.setText(Name);

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

    }

    ///////////////////////////////////// UPLOAD IMAGE ////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////// Uploading of Image to Firebase ///////////////////////////////////////////////////////////
    // Link to youtube tutorial for image uploading
    // https://www.youtube.com/watch?v=ZmgncLHk_s4&t=831s

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), GALLERY_PICK);

    }

    private void uploadFile(){

        if (filePath != null)
        {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(filePath));
            fileReference.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
            {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }

                    /// Suspect this line is not working
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>()
            {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if (task.isSuccessful())
                    {

                        Uri downloadUri = task.getResult();
                        UploadimageModelClass upload = new UploadimageModelClass(downloadUri.toString());
                        imageLink = upload.getmImageUrl();
                        mStatusDatabase.child("ProfilePicture").setValue(imageLink);

                    } else
                    {
                        Toast.makeText(SettingsActivity.this, "upload failed here2: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    ////////////////////////////////////////////////// The below method is just to get the file extension for the image //////////////////////////////////////////////////
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    ////////////////////////////////////////////////// The above method is just to get the file extension for the image //////////////////////////////////////////////////


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .setMinCropResultSize(200,200)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                imageView.setImageURI(resultUri);
                filePath = resultUri;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    /////////////////////////////////////////////////////////////////////// UPLOAD IMAGE ////////////////////////////////////////////////////////////////////////////////////

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
                Intent mainintent = new Intent(SettingsActivity.this, StartActivity.class);
                startActivity(mainintent);
        }
        return super.onOptionsItemSelected(item);
    }
}

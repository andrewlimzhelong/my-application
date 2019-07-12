package com.example.andrewspc.connectv6;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.MimeTypeFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SetUpActivity extends AppCompatActivity implements View.OnClickListener {

    /////////////////////////////////////////////////// Students SetUp Activity
    private ImageView profileImage;
    private EditText setUpEnteredName;
    private EditText bioEntered;
    private EditText skillsOrTalent;
    private EditText chargePricing;
    private EditText currentOccupation;

    //Firebase Database
    private DatabaseReference mDatabaseDesign;

    private FirebaseUser mCurrentUser;

    //Firebase Database Image Uploading
    private static final int GALLERY_PICK = 1;
    private DatabaseReference mDatabaseRef;

    //Button
    private Button createBtn;

    //Image uploading
    private Uri filePath;
    private Button buttonChoose;
    private Button buttonUpload;
    private StorageReference storageReference;

    //Image Uri Storage variable
    private String imageLink;

    //Spinner Variable
    private String selectGender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);

        //For the top app bar of the app
        Toolbar toolbar = findViewById(R.id.homeScreenToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Setup Account");


        ///////////////////// SETTING UP THE SPINNER HERE
        Spinner spinner = findViewById(R.id.setUpSchool);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        selectGender = spinner.getSelectedItem().toString();
        //////////////////// SETTING UP THE SPINNER IN THE ABOVE CODE



        //////////////////// SETTING UP THE SPINNER IN THE ABOVE CODE

        //Setting up image
        profileImage = (ImageView) findViewById(R.id.profileImage);
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        //buttonUpload = (Button) findViewById(R.id.uploadButton);

        //Setting up user information
        setUpEnteredName = (EditText) findViewById(R.id.setUpName);
        bioEntered = (EditText) findViewById(R.id.bioEntered);
        skillsOrTalent = (EditText) findViewById(R.id.skillsOrTalent);
        chargePricing = (EditText) findViewById(R.id.chargePricing);
        currentOccupation = (EditText) findViewById(R.id.currentOccupation);
        createBtn = (Button) findViewById(R.id.setUpBtn);

        //Storage Reference for uploading of images
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        String uid = mCurrentUser.getUid();

        ////////////// Firebase Image uploading database reference
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        ///////////////////////////////////////////////////////////// Uploading of Image to Firebase ///////////////////////////////////////////////////////////

        buttonChoose.setOnClickListener(this);
        //buttonUpload.setOnClickListener(this);

        ///////////////////////////////////////////////////////////// Uploading of Image to Firebase ///////////////////////////////////////////////////////////

        // Storing data into our database tutorial 9, 15 minute mark
        // Creating the Users table in our database

        // Reference to Database for Business, Design Students, Media Students and Engineering Students
        mDatabaseDesign = FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                .child("School Of Design").child("Students").child("Users").child(uid);


        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String EnteredName = setUpEnteredName.getEditableText().toString();
                final String BioEntered = bioEntered.getEditableText().toString();
                final String skillEntered = skillsOrTalent.getEditableText().toString();
                final String PricingEntered = chargePricing.getEditableText().toString();
                final String occupationEntered = currentOccupation.getEditableText().toString();
                final String gender = selectGender;
                final String uid = mCurrentUser.getUid();

                //final String imageLinkDrawn = imageLink;

                // Saving data into firebase backend
                if (!EnteredName.isEmpty() && !BioEntered.isEmpty() && !skillEntered.isEmpty() && !PricingEntered.isEmpty() && !occupationEntered.isEmpty()) {

                    HashMap<String, String> multipleData = new HashMap<>();
                    multipleData.put("name", EnteredName);
                    multipleData.put("bio", BioEntered);
                    multipleData.put("skills", skillEntered);
                    multipleData.put("pricing", PricingEntered);
                    multipleData.put("occupation", occupationEntered);
                    multipleData.put("gender", gender);
                    multipleData.put("userKeyId", uid);
                    multipleData.put("chat", "true");

                        mDatabaseDesign.setValue(multipleData);
                        uploadFile();


                    Intent setupIntent = new Intent(SetUpActivity.this, HomeScreen.class);
                    startActivity(setupIntent);

                } else {
                    Toast.makeText(SetUpActivity.this, "Please Ensure All Fields Are Filled", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

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

                        mDatabaseDesign.child("ProfilePicture").setValue(imageLink);
                    } else
                    {
                        Toast.makeText(SetUpActivity.this, "upload failed here2: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {

        if (view == buttonChoose) {
            //open file Chooser
            showFileChooser();
        }else if (view == buttonUpload){
            //uploadFile();
        }
    }
    //////////////////////////////////////////////////////////// Uploading of Image to Firebase ///////////////////////////////////////////////////////////
}

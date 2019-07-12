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

import com.example.andrewspc.connectv6.Utils.BottomNavigationViewHelper;
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
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.HashMap;

public class PostImagesActivity extends AppCompatActivity {

    //Uploading of the image to firebase
    private Button buttonChoose;
    private static final int GALLERY_PICK = 1;
    private Uri filePath;
    private ImageView postImage;
    private Button buttonUpload;
    private StorageReference storageReference;

    //uploading description of the image
    private EditText uploadDesc;
    private EditText uploadPricing;
    private EditText uploadReview;

    //Database reference
    private DatabaseReference mDatabaseDesign;
    private DatabaseReference mDatabaseRef2;
    private DatabaseReference mDatabaseRef3;
    private FirebaseUser mCurrentUser;


    //Image Uri Storage variable
    private String imageLink;
    private String userName;

    //Increase number for image uploaded
    private int imageNumber;
    private DatabaseReference detailsPosted;

    private Uri imagePostPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_images);

        //For the top app bar of the app
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Upload Activity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupBottomNavigatonView();

        buttonUpload = (Button) findViewById(R.id.postImageBtn);
        postImage = (ImageView) findViewById(R.id.postImageDisplayView);
        uploadDesc = (EditText) findViewById(R.id.serviceDescription);
        uploadPricing = (EditText) findViewById(R.id.pricingAmt);
        uploadReview = (EditText) findViewById(R.id.reviewGiven);

        //Storage Reference for uploading of images
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        mDatabaseRef2 = FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                .child("School Of Design").child("Students").child("Users").child(current_uid).child("Photos").push();

        mDatabaseDesign = FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                .child("School Of Design").child("Students").child("Photos").push();

        mDatabaseRef3 = FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                .child("School Of Design").child("Students").child("Users").child(current_uid).child("Profile");

        mDatabaseRef3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                userName = snapshot.child("UniqueName").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showFileChooser();
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadFile();

                Intent setupIntent = new Intent(PostImagesActivity.this, PostedServices.class);
                startActivity(setupIntent);
            }
        });
    }

    private void setupBottomNavigatonView() {
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(PostImagesActivity.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(2);
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
                Intent loginIntent = new Intent(PostImagesActivity.this, StartActivity.class);
                startActivity(loginIntent);

        }
        return super.onOptionsItemSelected(item);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), GALLERY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            ////////////////// CROP IMAGE CODE //////////////////// Youtube Link: https://www.youtube.com/watch?v=TG55BDSzErw
            CropImage.activity(filePath)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,2)
                    .start(this);
            ////////////////// CROP IMAGE CODE ////////////////////

            /*
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                postImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            */
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                postImage.setImageURI(resultUri);
                filePath = resultUri;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

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

                        PostActivityModelClass upload = new PostActivityModelClass(downloadUri.toString());

                        imageLink = upload.getmImageUrl();

                        final String uploadDescription = uploadDesc.getEditableText().toString();
                        final String uploadedPricing  = uploadPricing.getEditableText().toString();
                        final String uploadedReview = uploadReview.getEditableText().toString();

                        HashMap<String, String> multipleData = new HashMap<>();
                        multipleData.put("Service", uploadDescription);
                        multipleData.put("Pricing", uploadedPricing);
                        multipleData.put("Reviews", uploadedReview);
                        multipleData.put("portfolioPic1", imageLink);
                        multipleData.put("Profile", userName);
                        String uid = mCurrentUser.getUid();
                        multipleData.put("userID", uid);
                        mDatabaseDesign.setValue(multipleData);
                        mDatabaseRef2.setValue(multipleData);

                    } else
                    {
                        Toast.makeText(PostImagesActivity.this, "upload failed here2: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
}

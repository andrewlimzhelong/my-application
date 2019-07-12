package com.example.andrewspc.connectv6;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RegisterName extends AppCompatActivity {

    private StorageReference storageReference;
    private String imageLink;

    private EditText uniqueId;
    private Button setUpBtn;
    private EditText contactNumber;

    // Firebase Database Setup
    // Database reference
    private DatabaseReference mDatabaseDesign;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabasePhotos;
    private FirebaseUser mCurrentUser;

    ArrayList<uniqueUsername> list;

    /////////// Image View For editing the image
    private ImageView setUpImg;
    private Uri imageUriFilePath;

    Uri filePath;
    private static final int GALLERY_REQUEST = 1;

    private static final int GALLERY_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_name);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        setUpImg = findViewById(R.id.userSelectImage);

        uniqueId = findViewById(R.id.regName);
        contactNumber = findViewById(R.id.contactHP);
        setUpBtn = findViewById(R.id.createBtn);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        list = new ArrayList<uniqueUsername>();

        final String uid = mCurrentUser.getUid();

        mDatabaseDesign = FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                .child("School Of Design").child("Students").child("Users").child(uid).child("Profile");

        mDatabasePhotos = FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                .child("School Of Design").child("Students").child("Photos").child(uid);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Uniquely Registered IDs").child(uid).child("UniqueID");

        // For Checking if a particular data is present in firebase database in this case we are checking if the child "name" exists in the database.
        mDatabaseDesign.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("UniqueName") && snapshot.hasChild("HPcontact")) {

                    Intent intent = new Intent(RegisterName.this, RegisterAdditionDetails.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        setUpBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                final String uniqueEnterName = uniqueId.getEditableText().toString();
                final String uniqueContactNum = contactNumber.getEditableText().toString();

                if(!uniqueEnterName.isEmpty() && !uniqueContactNum.isEmpty()) {

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Uniquely Registered IDs");

                    Query query = reference.child("UniqueID").orderByChild("UniqueName").equalTo(uniqueEnterName);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {

                                uploadFile();

                                HashMap<String, String> multipleData = new HashMap<>();
                                multipleData.put("UniqueName", uniqueEnterName);
                                multipleData.put("HPcontact", uniqueContactNum);

                                multipleData.put("UniqueName", uniqueEnterName);
                                multipleData.put("HPcontact", uniqueContactNum);

                                mDatabaseDesign.setValue(multipleData);
                                mDatabaseRef.setValue(multipleData);

                                /*
                                mDatabaseDesign.setValue("UniqueName", uniqueEnterName);
                                mDatabaseDesign.setValue("HPcontact", uniqueContactNum);

                                mDatabaseRef.setValue("UniqueName", uniqueEnterName);
                                mDatabaseRef.setValue("HPcontact", uniqueContactNum);
                                */

                                Intent intent = new Intent(RegisterName.this, RegisterAdditionDetails.class);
                                startActivity(intent);
                                finish();

                                for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                    // do something with the individual "issues"
                                }
                            }else{

                                Toast.makeText(RegisterName.this, "This username already exists", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

            }

        });

        setUpImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showFileChooser();
            }
        });

    }

    /*
    @Override
    public void onBackPressed() {
        Intent i = new Intent(RegisterName.this, PostedServices.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
    */

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
                        Toast.makeText(RegisterName.this, "upload failed here2: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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

                setUpImg.setImageURI(resultUri);
                filePath = resultUri;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    /////////////////////////////////////////////////////////////////////// UPLOAD IMAGE ////////////////////////////////////////////////////////////////////////////////////

}



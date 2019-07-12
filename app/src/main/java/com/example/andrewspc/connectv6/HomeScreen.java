package com.example.andrewspc.connectv6;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class HomeScreen extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{

    private StorageReference storageReference;

    //Database reference
    private DatabaseReference mDatabaseDesign;
    private DatabaseReference mDatabaseRef2;
    private FirebaseUser mCurrentUser;

    // Database reference for retrieving user profile information
    private DatabaseReference mDatabase;

    private Button newPostBtn;
    private EditText TitleBox;
    private EditText Pricing;
    private EditText details;
    private EditText decimalAmt;

    /////////////// NEW VARIABLES
    private EditText handphoneNum;
    private EditText officeNumber;
    private EditText companyNamePA;
    private EditText companyDescPA;
    private EditText countryLocation;
    private EditText stateLocation;
    private TextView editContactDetails;
    private TextView editButtonCompany;
    private TextView editButtonLocation;
    /////////////// NEW VARIABLES

    //Image Uri Storage variable
    private String imageLink;
    private String userName;

    private String contactHP;
    private String contactOffice;

    private ImageView camera;
    private Uri imageUriFilePath;

    private String ProfileImage;

    private String categorySelected;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Spinner spinner = findViewById(R.id.categoryChooser);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        progressBar = findViewById(R.id.loadingProgressBar);
        progressBar.setVisibility(View.INVISIBLE);

        TitleBox = findViewById(R.id.TitleBox);
        newPostBtn = findViewById(R.id.clickPostBtn);
        Pricing = findViewById(R.id.priceStated);
        details = findViewById(R.id.descriptionOfItem);
        decimalAmt = findViewById(R.id.decimalNumber);

        /////////////// NEW VARIABLES
        handphoneNum = findViewById(R.id.handphoneNum);
        officeNumber = findViewById(R.id.officeNumber);
        companyNamePA = findViewById(R.id.companyNamePA);
        companyDescPA = findViewById(R.id.companyDescPA);
        countryLocation = findViewById(R.id.countryLocation);
        stateLocation = findViewById(R.id.stateLocation);

        editContactDetails = findViewById(R.id.editContactDetails);
        editButtonCompany = findViewById(R.id.editButtonCompany);
        editButtonLocation = findViewById(R.id.editButtonLocation);
        /////////////// NEW VARIABLES

        buttonEnableMethod(editContactDetails ,handphoneNum, officeNumber);
        buttonEnableMethod(editButtonCompany, companyNamePA, companyDescPA);
        buttonEnableMethod(editButtonLocation, countryLocation, stateLocation);

        //Storage Reference for uploading of images
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        mDatabaseRef2 = FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                .child("School Of Design").child("Students").child("Users").child(current_uid).child("Photos").push();

        mDatabaseDesign = FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                .child("School Of Design").child("Students").child("Photos").push();

        /////////////////////////////////// CODE FROM HERE ONWARDS IS FOR RETRIEVING THE INFORMATION FOR EACH USER'S PROFILE //////////////////////////////////////
        mDatabase = FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                .child("School Of Design").child("Students").child("Users").child(current_uid).child("Profile");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.hasChild("UniqueName") && snapshot.hasChild("HPcontact")) {
                    userName = snapshot.child("UniqueName").getValue().toString();
                    contactHP = snapshot.child("HPcontact").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //////// SETTING INFORMATION RETRIEVED FROM FIREBASE INTO THE RESPECTIVE TEXTFIELDS
        handphoneNum.setText(contactHP);

        //////// SETTING INFORMATION RETRIEVED FROM FIREBASE INTO THE RESPECTIVE TEXTFIELDS
        //For the top app bar of the app
        /*
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Post Description");
        */

        newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(TitleBox.getEditableText().toString())
                        && !TextUtils.isEmpty(Pricing.getEditableText().toString())
                        && !TextUtils.isEmpty(details.getEditableText().toString())
                        && !TextUtils.isEmpty(decimalAmt.getEditableText().toString())){

                    progressBar.setVisibility(View.VISIBLE);
                    newPostBtn.setEnabled(false);
                    uploadFile();

                }
                else{
                    if (TextUtils.isEmpty(TitleBox.getEditableText().toString())){
                        TitleBox.requestFocus();
                    }
                    if (TextUtils.isEmpty(Pricing.getEditableText().toString())){
                        Pricing.requestFocus();
                    }
                    if (TextUtils.isEmpty(details.getEditableText().toString())){
                        details.requestFocus();
                    }
                    Toast.makeText(HomeScreen.this, "Please Ensure Text Fields In Title Box Are Filled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        SettingsActivity settingsActivity = new SettingsActivity();
        settingsActivity.retrivingDataFromFirebaseET("HPcontact", handphoneNum, mDatabase);
        settingsActivity.retrivingDataFromFirebaseET("officeNum", officeNumber, mDatabase);
        settingsActivity.retrivingDataFromFirebaseET("yourCompany", companyNamePA, mDatabase);
        settingsActivity.retrivingDataFromFirebaseET("yourCompanyDesc", companyDescPA, mDatabase);
        settingsActivity.retrivingDataFromFirebaseET("CountryLocation", countryLocation, mDatabase);
        settingsActivity.retrivingDataFromFirebaseET("StateLocation", stateLocation, mDatabase);

        // camera = findViewById(R.id.cameraImage);
        // Getting image from the posted services sell button
        final Intent intent = getIntent();
        Uri imageUri = intent.getParcelableExtra("imageSelected");
        // Picasso.get().load(imageUri).resize(800,500).into(camera);

        imageUriFilePath = imageUri;

        // Getting image from the Profile user who posted it Database
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("ProfilePicture")) {

                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ProfileImage = (String) dataSnapshot.child("ProfilePicture").getValue();
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

        /*
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("HPcontact")) {

                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String handPhoneNumber = dataSnapshot.child("HPcontact").getValue().toString();
                            handphoneNum.setText(handPhoneNumber);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

                if (snapshot.hasChild("officeNum")) {

                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String OfficeNumber = dataSnapshot.child("officeNum").getValue().toString();
                            officeNumber.setText(OfficeNumber);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

                if (snapshot.hasChild("yourCompany")) {

                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String OfficeNumber = dataSnapshot.child("yourCompany").getValue().toString();
                            companyNamePA.setText(OfficeNumber);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

                if (snapshot.hasChild("yourCompanyDesc")) {

                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String OfficeNumber = dataSnapshot.child("yourCompanyDesc").getValue().toString();
                            companyDescPA.setText(OfficeNumber);
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
        */

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(HomeScreen.this, PostedServices.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }

    public void buttonEnableMethod(final TextView editButton, final EditText editTextTop, final EditText editTextBottom) {

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editTextTop.setEnabled(true);
                editTextBottom.setEnabled(true);

            }
        });

    }

    private void uploadFile(){

        if (imageUriFilePath != null)
        {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUriFilePath));
            fileReference.putFile(imageUriFilePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
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

                        final String uploadTitle = TitleBox.getEditableText().toString();
                        final String uploadPricing = Pricing.getEditableText().toString();
                        final String uploadDetails = details.getEditableText().toString();
                        final String uploadDecimal = decimalAmt.getEditableText().toString();
                        final String uploadHPcontact = handphoneNum.getEditableText().toString();
                        final String uploadOffice = officeNumber.getEditableText().toString();
                        final String uploadCompanyName = companyNamePA.getEditableText().toString();
                        final String uploadCompanyDescPA = companyDescPA.getEditableText().toString();
                        final String uploadCountryLocation = countryLocation.getEditableText().toString();
                        final String uploadStateLocation = stateLocation.getEditableText().toString();

                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
                        String time = mdformat.format(calendar.getTime());

                        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                        HashMap<String, String> multipleData = new HashMap<>();
                        multipleData.put("Title", uploadTitle);
                        multipleData.put("Pricing", uploadPricing);
                        multipleData.put("Details", uploadDetails);
                        multipleData.put("Decimal", uploadDecimal);
                        multipleData.put("portfolioPic1", imageLink);
                        multipleData.put("HPcontact", uploadHPcontact);
                        multipleData.put("officeNum", uploadOffice);
                        multipleData.put("yourCompany", uploadCompanyName);
                        multipleData.put("yourCompanyDesc", uploadCompanyDescPA);
                        multipleData.put("CountryLocation", uploadCountryLocation);
                        multipleData.put("StateLocation", uploadStateLocation);
                        multipleData.put("Profile", userName);
                        multipleData.put("ProfilePicture", ProfileImage);
                        String uid = mCurrentUser.getUid();
                        String imageKey = FirebaseDatabase.getInstance().getReference().push().getKey();
                        multipleData.put("userID", uid);
                        multipleData.put("imageUniqueKey", imageKey);
                        multipleData.put("Category", categorySelected);
                        multipleData.put("time", time);
                        multipleData.put("date", date);

                        if (categorySelected.equals("All Services")){
                            multipleData.put("CategoryImage", "https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/" +
                                    "uploads%2FCategories%20Images%2Fwhiteoutlineninesmallboxes." +
                                    "png?alt=media&token=b694748c-e61b-4414-abbc-10a12f218a4f");
                        }

                        if (categorySelected.equals("24h Services")){
                            multipleData.put("CategoryImage", "https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/" +
                                    "uploads%2FCategories%20Images%2Fwhiteoutlineninesmallboxes." +
                                    "png?alt=media&token=b694748c-e61b-4414-abbc-10a12f218a4f");
                        }

                        if (categorySelected.equals("Education")){
                            multipleData.put("CategoryImage", "https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/" +
                                    "uploads%2FCategories%20Images%2Fwhiteoutlineninesmallboxes." +
                                    "png?alt=media&token=b694748c-e61b-4414-abbc-10a12f218a4f");
                        }

                        if (categorySelected.equals("Relax")){
                            multipleData.put("CategoryImage", "https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/" +
                                    "uploads%2FCategories%20Images%2Fwhiteoutlineninesmallboxes." +
                                    "png?alt=media&token=b694748c-e61b-4414-abbc-10a12f218a4f");
                        }

                        if (categorySelected.equals("Facial")){
                            multipleData.put("CategoryImage", "https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/" +
                                    "uploads%2FCategories%20Images%2Fwhiteoutlineninesmallboxes." +
                                    "png?alt=media&token=b694748c-e61b-4414-abbc-10a12f218a4f");
                        }

                        if (categorySelected.equals("Games")){
                            multipleData.put("CategoryImage", "https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/" +
                                    "uploads%2FCategories%20Images%2Fwhiteoutlineninesmallboxes." +
                                    "png?alt=media&token=b694748c-e61b-4414-abbc-10a12f218a4f");
                        }
                        if (categorySelected.equals("Medical")){
                            multipleData.put("CategoryImage", "https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/" +
                                    "uploads%2FCategories%20Images%2Fwhiteoutlineninesmallboxes." +
                                    "png?alt=media&token=b694748c-e61b-4414-abbc-10a12f218a4f");
                        }

                        if (categorySelected.equals("Exercise")){
                            multipleData.put("CategoryImage", "https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/" +
                                    "uploads%2FCategories%20Images%2Fwhiteoutlineninesmallboxes." +
                                    "png?alt=media&token=b694748c-e61b-4414-abbc-10a12f218a4f");
                        }

                        if (categorySelected.equals("Delivery")){
                            multipleData.put("CategoryImage", "https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/" +
                                    "uploads%2FCategories%20Images%2Fwhiteoutlineninesmallboxes." +
                                    "png?alt=media&token=b694748c-e61b-4414-abbc-10a12f218a4f");
                        }

                        if (categorySelected.equals("Travel")){
                            multipleData.put("CategoryImage", "https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/" +
                                    "uploads%2FCategories%20Images%2Fwhiteoutlineninesmallboxes." +
                                    "png?alt=media&token=b694748c-e61b-4414-abbc-10a12f218a4f");
                        }

                        if (categorySelected.equals("Advertising")){
                            multipleData.put("CategoryImage", "https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/" +
                                    "uploads%2FCategories%20Images%2Fwhiteoutlineninesmallboxes." +
                                    "png?alt=media&token=b694748c-e61b-4414-abbc-10a12f218a4f");
                        }

                        if (categorySelected.equals("Property")){
                            multipleData.put("CategoryImage", "https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/" +
                                    "uploads%2FCategories%20Images%2Fwhiteoutlineninesmallboxes." +
                                    "png?alt=media&token=b694748c-e61b-4414-abbc-10a12f218a4f");
                        }

                        if (categorySelected.equals("Focus")){
                            multipleData.put("CategoryImage", "https://firebasestorage.googleapis.com/v0/b/connectv6.appspot.com/o/" +
                                    "uploads%2FCategories%20Images%2Fwhiteoutlineninesmallboxes." +
                                    "png?alt=media&token=b694748c-e61b-4414-abbc-10a12f218a4f");
                        }

                        mDatabaseDesign.setValue(multipleData);
                        mDatabaseRef2.setValue(multipleData);

                        Intent setupIntent = new Intent(HomeScreen.this, PostedServices.class);
                        startActivity(setupIntent);

                        progressBar.setVisibility(View.GONE);
                        newPostBtn.setEnabled(true);

                    } else
                    {
                        Toast.makeText(HomeScreen.this, "upload failed here2: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        categorySelected = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    ////////////////////////////////////////////////// The above method is just to get the file extension for the image //////////////////////////////////////////////////

}

package com.example.andrewspc.connectv6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterAdditionDetails extends AppCompatActivity {

    private Button skipButton;
    private Button nextButton;

    private EditText companyNamePA;
    private EditText companyDescPA;
    private EditText countryET;
    private EditText countryState;
    private EditText officeNum;

    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseDesign;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_addition_details);

        // Button Variables linking to xml
        skipButton = findViewById(R.id.skipButton);
        nextButton = findViewById(R.id.nextButton);

        // EditText Variables linking to EditText Fields in the xml
        companyNamePA = findViewById(R.id.companyNamePA);
        companyDescPA = findViewById(R.id.companyDescPA);
        officeNum = findViewById(R.id.officeNum);
        countryET = findViewById(R.id.countryET);
        countryState = findViewById(R.id.countryState);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = mCurrentUser.getUid();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Uniquely Registered IDs").child(uid).child("UniqueID");
        //mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Uniquely Registered IDs").child("UniqueID");

        mDatabaseDesign = FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                .child("School Of Design").child("Students").child("Users").child(uid).child("Profile");

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadDataIntoFirebase("yourCompany", companyNamePA);
                uploadDataIntoFirebase("yourCompanyDesc", companyDescPA);
                uploadDataIntoFirebase("officeNum", officeNum);
                uploadDataIntoFirebase("CountryLocation", countryET);
                uploadDataIntoFirebase("StateLocation", countryState);

            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RegisterAdditionDetails.this, PostedServices.class);
                startActivity(intent);
                finish();

            }
        });

    }

    public void uploadDataIntoFirebase(final String leftHeader, final EditText rightETValue){

        String uploadETValue = rightETValue.getEditableText().toString();
        mDatabaseDesign.child(leftHeader).setValue(uploadETValue);
        mDatabaseRef.child(leftHeader).setValue(uploadETValue);

        Intent intent = new Intent(RegisterAdditionDetails.this, PostedServices.class);
        startActivity(intent);
        finish();

    }

}

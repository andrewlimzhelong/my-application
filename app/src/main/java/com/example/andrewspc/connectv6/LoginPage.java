package com.example.andrewspc.connectv6;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {

    // Login Email and Password fields
    private EditText loginEmailText;
    private EditText loginPassText;

    //login Btn
    private Button loginBtn;

    //Firebase Authenication
    private FirebaseAuth mAuth;


    //Clickable link below the login btn
    private TextView signUpLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mAuth = FirebaseAuth.getInstance();

        loginEmailText = findViewById(R.id.reg_email);
        loginPassText = findViewById(R.id.reg_confirm_pass);
        loginBtn = findViewById(R.id.login_btn);

        //Sign Up link below login button
        signUpLink = findViewById(R.id.loginPageText);

        //Code for the login btn
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loginEmail = loginEmailText.getText().toString();
                String loginPass = loginPassText.getText().toString();

                if(!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPass)){

                    // This line below help to check if the enter username and password tallies with the one registered and if yes, the user can login into the app.
                    mAuth.signInWithEmailAndPassword(loginEmail, loginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                sendToMain();

                            } else {

                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(LoginPage.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();


                            }

                        }
                    });

                } else {

                    Toast.makeText(LoginPage.this, "Please Ensure All Fields Are Filled.", Toast.LENGTH_LONG).show();
                }

            }
        });


        //Clickable link below the login btn
        String text = "Sign Up Here!";

        //Clickable link below the login btn
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                Intent mainIntent = new Intent(LoginPage.this, DisplayStudents.class);
                startActivity(mainIntent);

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
            }
        };

        ss.setSpan(clickableSpan, 0, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        signUpLink.setText(ss);
        signUpLink.setMovementMethod(LinkMovementMethod.getInstance());
    }

    // Method used to check if the user is already logged on previously, if yes, they don't need to login again, they can immediate access the logged on Main Page
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){

            //sendToMain();

        }

    }

    private void sendToMain() {

        Intent mainIntent = new Intent(LoginPage.this, HomeScreen.class);
        startActivity(mainIntent);
        finish();
    }


}

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class RegisterAccount extends AppCompatActivity {

    //Code For all the Text Input fields
    private EditText reg_email;
    private EditText reg_password;
    private EditText reg_password2;

    //Register Button
    private Button reg_btn;
    private Button loginPhoneNumber;

    //Firebase Authentication
    private FirebaseAuth mAuth;

    //Google Sign In Button
    /*
    private SignInButton signIn;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 1;
    private String TAG = "registerAccount";
    */

    //Clickable link below the login btn
    private TextView loginPageLink;
    //Clickable link below the login btn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        //signIn = findViewById(R.id.sign_in_button);

        mAuth = FirebaseAuth.getInstance();

        reg_email = findViewById(R.id.regEmail);
        reg_password = findViewById(R.id.regPassword);
        reg_password2 = findViewById(R.id.regPassword2);

        //Buttons
        reg_btn = findViewById(R.id.createAccountBtn);
        //loginPhoneNumber = findViewById(R.id.phoneNumber);

        // Configure Google Sign In
        /*
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();

            }
        });
        */

        /*
        loginPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent setupIntent = new Intent(RegisterAccount.this, HomeScreen.class);
                startActivity(setupIntent);

            }
        });
        */

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = reg_email.getText().toString();
                String pass = reg_password.getText().toString();
                String confirm_pass = reg_password2.getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(confirm_pass)){

                    if(pass.equals(confirm_pass)){


                        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                    Intent setupIntent = new Intent(RegisterAccount.this, RegisterName.class);
                                    startActivity(setupIntent);
                                    finish();

                                } else {

                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(RegisterAccount.this, "Failed To Register User", Toast.LENGTH_LONG).show();

                                }

                            }
                        });

                    } else {

                        Toast.makeText(RegisterAccount.this, "Please Confirm Password Fields Match.", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(RegisterAccount.this, "Please Ensure All Fields Are Not Empty.", Toast.LENGTH_LONG).show();
                }

            }
        });



        ////////////////////////////////////////////////////////////Clickable link below the login btn
        loginPageLink = findViewById(R.id.loginPageText);
        ////////////////////////////////////////////////////////////Clickable link below the login btn

        //Clickable link below the login btn
        String text = "Login With Google Or Facebook";

        //Clickable link below the login btn
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                Intent mainIntent = new Intent(RegisterAccount.this, StartActivity.class);
                startActivity(mainIntent);

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
            }
        };

        ss.setSpan(clickableSpan, 0, 29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        loginPageLink.setText(ss);
        loginPageLink.setMovementMethod(LinkMovementMethod.getInstance());


    }

    /*
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.v(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(RegisterAccount.this, "Failed To Sign In With Google Accounts",Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        Intent setupIntent = new Intent(RegisterAccount.this, SetUpActivity.class);
        startActivity(setupIntent);
        finish();

    }
    */

    @Override
    protected void onStart() {
        super.onStart();

        //If User has an account
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            //sendToMain();

        }

    }

    private void sendToMain() {

        Intent mainIntent = new Intent(RegisterAccount.this, DisplayStudents.class);
        startActivity(mainIntent);
        finish();

    }



}

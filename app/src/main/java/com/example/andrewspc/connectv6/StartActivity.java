package com.example.andrewspc.connectv6;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
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

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class StartActivity extends AppCompatActivity {

    // YOUTUBE LINKS TO FACEBOOK LOGIN BUTTON
    // https://www.youtube.com/watch?v=mapLbSKNc6I
    private CallbackManager mCallbackManager;
    private LoginButton loginButton;

    // GOOGLE SIGN IN BUTTON
    // YOUTUBE LINKS TO GOOGLE SIGN IN LOGIN BUTTON
    // https://www.youtube.com/watch?v=-ywVw2O1pP8&t=908s
    private SignInButton mGoogleBtn;
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAGS = "RANDOM_ID";
    private FirebaseAuth.AuthStateListener mAuthListener;
    // YOUTUBE LINKS TO GOOGLE SIGN IN LOGIN BUTTON
    // https://www.youtube.com/watch?v=-ywVw2O1pP8&t=908s
    // GOOGLE SIGN IN BUTTON

    //Clickable link below the login btn
    private TextView loginPageLink;
    //Clickable link below the login btn

    //FIREBASE
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseDesign;
    private FirebaseUser mCurrentUser;

    private static final String TAG = "FACELOG";

    private Button mFacebookBtn;

    //////////////////////////////////////////////////////////////////////////////// LOGIN PAGE CODE VARIABLES ////////////////////////////////////////////////////////////////////////////

    // Login Email and Password fields
    private EditText loginEmailText;
    private EditText loginPassText;

    //login Btn
    private Button loginBtn;

    //Firebase Authenication
    private FirebaseAuth mAuthentication;

    //Clickable link below the login btn
    private TextView signUpLink;

    /////////////////////////////////////////////////////////////////////////////// LOGIN PAGE CODE VARIABLES ////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Check if user is already signed in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {

            sendToMain();

        }

        ///// FACEBOOK BTN ////////
        mFacebookBtn = (Button) findViewById(R.id.facebookBtn);
        ///// FACEBOOK BTN ////////

        ////////////////////////////////////////////////////////////Clickable link below the login btn
        loginPageLink = findViewById(R.id.loginPageText);

        //Clickable link below the login btn
        String text = "Sign Up";

        //Clickable link below the login btn
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                Intent mainIntentRegPage = new Intent(StartActivity.this, RegisterAccount.class);
                startActivity(mainIntentRegPage);
                finish();

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
            }
        };

        ss.setSpan(clickableSpan, 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        loginPageLink.setText(ss);
        loginPageLink.setMovementMethod(LinkMovementMethod.getInstance());

        ////////////////////////////////////////////////////////////Clickable link below the login btn

        // GOOGLE SIGN IN BUTTON // GOOGLE SIGN IN BUTTON // GOOGLE SIGN IN BUTTON
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() != null){

                    sendToMain();
                }
            }
        };

        mGoogleBtn = (SignInButton) findViewById(R.id.sign_in_button);
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();
            }
        });
        // GOOGLE SIGN IN BUTTON // GOOGLE SIGN IN BUTTON // GOOGLE SIGN IN BUTTON

        //Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();

        ////////// FACEBOOK BTN

        mFacebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginManager.getInstance().logInWithReadPermissions(StartActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:onError", error);
                    }
                });

            }
        });

        ////////// FACEBOOK BTN

        ///////////////////////////////////////////////////////////////////////////////////// LOGIN PAGE CODE ////////////////////////////////////////////////////////////////////////

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
                                Toast.makeText(StartActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();


                            }

                        }
                    });

                } else {

                    Toast.makeText(StartActivity.this, "Please Ensure All Fields Are Filled.", Toast.LENGTH_LONG).show();
                }

            }
        });


        //////////////////////////////////////////////////////////////////////////////////// LOGIN PAGE CODE ///////////////////////////////////////////////////////////////////////////
    }

    private void sendToMain() {

        //Storage Reference for uploading of images
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        mDatabaseDesign = FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                .child("School Of Design").child("Students").child("Users").child(current_uid).child("Profile");

        // For Checking if a particular data is present in firebase database in this case we are checking if the child "name" exists in the database.
        mDatabaseDesign.addListenerForSingleValueEvent(new ValueEventListener() {

            boolean dataEmpty = true;

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("UniqueName") && snapshot.hasChild("HPcontact")) {

                    Intent mainIntent = new Intent(StartActivity.this, PostedServices.class);
                    startActivity(mainIntent);
                    finish();

                    dataEmpty = false;

                }

                if(dataEmpty){

                    Intent mainIntent = new Intent(StartActivity.this, RegisterName.class);
                    startActivity(mainIntent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAGS, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        mAuth.addAuthStateListener(mAuthListener);

        if (currentUser != null){

            updateUI();
        }
    }

    private void updateUI() {

        Toast.makeText(StartActivity.this, " You have loggon using Facebook", Toast.LENGTH_SHORT).show();

        sendToMain();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(StartActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

}

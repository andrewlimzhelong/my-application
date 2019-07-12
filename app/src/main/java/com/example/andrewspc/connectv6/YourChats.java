package com.example.andrewspc.connectv6;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.example.andrewspc.connectv6.Chat.ChatActivity;
import com.example.andrewspc.connectv6.Chat.ChatAdapter;
import com.example.andrewspc.connectv6.Chat.ChatObject;
import com.example.andrewspc.connectv6.Utils.BottomNavigationViewHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

public class YourChats extends AppCompatActivity {

    SearchView searchView;

    private RecyclerView mChatList;
    private RecyclerView.Adapter mChatListAdapter;
    private RecyclerView.LayoutManager mChatListLayoutManager;

    ArrayList<ChatObject> chatList;
    ChatListAdapters ChatAdapterForChatList;

    private Button sellButton;
    private static final int GALLERY_REQUEST = 1;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_chats);

        sellButton = findViewById(R.id.sellBtn);

        searchView = findViewById(R.id.searchBtn);

        setupBottomNavigatonView();

        chatList = new ArrayList<ChatObject>();
        mChatList = (RecyclerView) findViewById(R.id.chatListOfChat);
        mChatList.setLayoutManager( new LinearLayoutManager(this));

        DatabaseReference mUserChatDB = FirebaseDatabase.getInstance().getReference().child("School").child("Nanyang Polytechnic")
                .child("School Of Design").child("Students").child("Users").child(FirebaseAuth.getInstance().getUid())
                .child("YourChat");

        mUserChatDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                        ChatObject chatobj = dataSnapshot1.getValue(ChatObject.class);
                        chatList.add(chatobj);
                    }

                    ChatAdapterForChatList = new ChatListAdapters(YourChats.this, chatList);
                    mChatList.setAdapter(ChatAdapterForChatList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Setting up the search function
        // Youtube Tutorial : https://www.youtube.com/watch?v=PmqYd-AdmC0
        if (searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        }

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), GALLERY_REQUEST);

                /*
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
                */

            }
        });
    }

    public void search(String str) {

        ArrayList<ChatObject> myCurrentList = new ArrayList<>();
        for (ChatObject obj : chatList)
        {
            if (obj.getUsername().toLowerCase().contains(str.toLowerCase())) {
                myCurrentList.add(obj);
            }
        }
        ChatListAdapters chatListAdapters = new ChatListAdapters(YourChats.this, myCurrentList);
        mChatList.setAdapter(chatListAdapters);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(YourChats.this, PostedServices.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
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

                final Intent intent = new Intent(YourChats.this, HomeScreen.class);
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


    private void setupBottomNavigatonView(){
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(YourChats.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
    }

}

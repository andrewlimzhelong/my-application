package com.example.andrewspc.connectv6;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ImagesSelected extends AppCompatActivity {

    private ImageView sendImage;
    private Button sendImageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_selected);

        sendImage = findViewById(R.id.sendImageView);
        sendImageBtn = findViewById(R.id.sendImageBtn);

        // Getting image from the posted services sell button
        final Intent intent = getIntent();
        Uri imageUri = intent.getParcelableExtra("imageSelected");
        String imageSent = imageUri.toString();
        Picasso.get().load(imageSent).fit().into(sendImage);

        final String chatID = getIntent().getStringExtra("chatIDD");

        Toast.makeText(ImagesSelected.this, chatID, Toast.LENGTH_SHORT).show();

        sendImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(ImagesSelected.this, chatID, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

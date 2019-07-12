package com.example.andrewspc.connectv6.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.example.andrewspc.connectv6.Chat.ChatActivity;
import com.example.andrewspc.connectv6.DisplayStudents;
import com.example.andrewspc.connectv6.HomeScreen;
import com.example.andrewspc.connectv6.PostImagesActivity;
import com.example.andrewspc.connectv6.PostedServices;
import com.example.andrewspc.connectv6.R;
import com.example.andrewspc.connectv6.SettingsActivity;
import com.example.andrewspc.connectv6.UserProfilePage;
import com.example.andrewspc.connectv6.YourChats;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BottomNavigationViewHelper {

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
        bottomNavigationViewEx.setIconTintList(0,null);
        bottomNavigationViewEx.setIconTintList(1,null);
        bottomNavigationViewEx.setIconTintList(2,null);
        bottomNavigationViewEx.setIconTintList(3,null);
        bottomNavigationViewEx.setIconTintList(4,null);

    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.ic_house:
                        Intent i2 = new Intent(context, PostedServices.class);
                        i2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(i2);
                        break;

                    case R.id.ic_message:
                        Intent i3 = new Intent(context, YourChats.class);
                        i3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(i3);
                        break;

                    case R.id.ic_profile:
                        Intent i = new Intent(context, UserProfilePage.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(i);
                        break;

                    /*
                    case R.id.ic_profile:
                        Intent intent2 = new Intent(context, UserProfilePage.class);
                        context.startActivity(intent2);
                        break;

                    case R.id.ic_house:
                        Intent intent3 = new Intent(context, PostedServices.class);
                        context.startActivity(intent3);
                        break;

                    case R.id.ic_message:
                        Intent intent4 = new Intent(context, YourChats.class);
                        context.startActivity(intent4);
                        break;
                        */
                }

                return false;
            }
        });
    }




}

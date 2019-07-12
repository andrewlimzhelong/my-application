package com.example.andrewspc.connectv6;

public class PostActivityModelClass {

    private String mImageUrl;

    public PostActivityModelClass(){

    }

    public PostActivityModelClass(String imageUrl){

        mImageUrl = imageUrl;

    }

    public String getmImageUrl() {
        return mImageUrl;

    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}

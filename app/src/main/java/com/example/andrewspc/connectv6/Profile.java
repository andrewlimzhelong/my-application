package com.example.andrewspc.connectv6;

import android.graphics.Picture;

public class Profile {

    private String name;
    private String bio;
    private String occupation;
    private String pricing;
    private String skills;
    private String ProfilePicture;
    private String userKeyId;
    private String ChatId;

    //Stting to retrieve all new posts by users into a recyclerview
    private String portfolioPic1;

    public Profile() {
    }

    public Profile(String name, String bio, String occupation, String pricing, String skills, String ProfilePicture, String portfolioPic1, String userKeyId, String ChatId) {

        this.portfolioPic1 = portfolioPic1;
        this.userKeyId = userKeyId;
        this.name = name;
        this.bio = bio;
        this.occupation = occupation;
        this.pricing = pricing;
        this.skills = skills;
        this.ProfilePicture = ProfilePicture;
        this.ChatId = ChatId;
    }

    public String getChatId() {
        return ChatId;
    }

    public void setChatId(String chatId) {
        ChatId = chatId;
    }

    public String getUserKeyId() {
        return userKeyId;
    }

    public void setUserKeyId(String userKeyId) {
        this.userKeyId = userKeyId;
    }

    public String getPortfolioPic1() {
        return portfolioPic1;
    }

    public void setPortfolioPic1(String portfolioPic1) {
        this.portfolioPic1 = portfolioPic1;
    }

    public String getProfilePicture() {
        return ProfilePicture;
    }

    public void setProfilePicture(String ProfilePicture) {
        this.ProfilePicture = ProfilePicture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPricing() {
        return pricing;
    }

    public void setPricing(String pricing) {
        this.pricing = pricing;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }


    ////////////////////////////////////////////// CODE FROM HERE ON OUT WORKS ///////////////////////////////////////////////////

    /*public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }*/

    /*public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }*/
}

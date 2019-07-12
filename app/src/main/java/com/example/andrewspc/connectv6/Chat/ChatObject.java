package com.example.andrewspc.connectv6.Chat;

public class ChatObject {

    private String Title;
    private String Username;
    private String ImageOfUser;
    private String UserUniqueID;

    public ChatObject() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }


    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getImageOfUser() {
        return ImageOfUser;
    }

    public void setImageOfUser(String imageOfUser) {
        ImageOfUser = imageOfUser;
    }

    public String getUserUniqueID() {
        return UserUniqueID;
    }

    public void setUserUniqueID(String userUniqueID) {
        UserUniqueID = userUniqueID;
    }


    /*
    public ChatObject(String ChatID) {

        this.ChatID = ChatID;
    }

    public String getChatID() {
        return ChatID;
    }

    public void setChatID(String ChatID) {
        this.ChatID = ChatID;
    }
    */


    /*private String chatIdP;

    public ChatObject() {
    }

    public ChatObject(String chatIdP) {
        this.chatIdP = chatIdP;
    }

    public String getchatIdP() {
        return chatIdP;
    }

    /*public void setChatId(String chatId) {
         chatId = chatId;
    }*/
}

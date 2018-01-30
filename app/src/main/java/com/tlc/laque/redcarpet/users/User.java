package com.tlc.laque.redcarpet.users;

import com.tlc.laque.redcarpet.database.DataBaseRead;

/**
 * Created by User on 03/12/2017.
 */
public class User {

    private String nickname;
    private String location;
    private String phoneNumber;
    private String key;
    private String privacy;
    private long numberFriends;
    private boolean friendOrNot;
    private boolean sentFriendRequest;
    private boolean gotFriendRequest;
    private int numberFriendsRequestS;
    private String rating = "0";
    private String numberVote = "0";
    private String urlPicture = "https://firebasestorage.googleapis.com/v0/b/red-carpet-6590c.appspot.com/o/users_images%2Fgeneric-user.png?alt=media&token=2ad347b4-7925-48ac-9d99-d6594518be0b";

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }
    public User(String name, String email) {
        this.nickname = name;
        this.location = email;
    }
    public User(String name, String email, String privacy) {
        this.nickname = name;
        this.location = email;
        this.privacy = privacy;
    }
    public User(String name, String email, String privacy, String urlPicture) {
        this.nickname = name;
        this.location = email;
        this.privacy = privacy;
        this.urlPicture = urlPicture;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public String getNumberVote() {
        return numberVote;
    }

    public void setNumberVote(String numberVote) {
        this.numberVote = numberVote;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }



    public int getNumberFriendsRequestS() {
        return numberFriendsRequestS;
    }

    public void setNumberFriendsRequestS(int numberFriendsRequestS) {
        this.numberFriendsRequestS = numberFriendsRequestS;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getKey() {
        return key;
    }

    public boolean isFriendOrNot() {
        return friendOrNot;
    }

    public boolean isSentFriendRequest() {
        return sentFriendRequest;
    }

    public boolean isGotFriendRequest() {
        return gotFriendRequest;
    }

    public void setGotFriendRequest(boolean gotFriendRequest) {
        this.gotFriendRequest = gotFriendRequest;
    }

    public void setSentFriendRequest(boolean sentFriendRequest) {
        this.sentFriendRequest = sentFriendRequest;
    }

    public void setFriendOrNot(boolean friendOrNot) {
        this.friendOrNot = friendOrNot;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getNumberFriends() {
        return numberFriends;
    }

    public void setNumberFriends(long numberFriends) {
        this.numberFriends = numberFriends;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNickname(){
        return this.nickname;
    }
    public String getLocation(){
        return this.location;
    }
    public String getPhoneNumber(){
        return this.phoneNumber;
    }
}
package com.tlc.laque.redcarpet.registration;

/**
 * Created by User on 03/12/2017.
 */
public class User {

    private String nickname;
    private String location;
    private String phoneNumber;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }
    public User(String name, String email) {
        this.nickname = name;
        this.location = email;
    }

    public User(String name, String email, String phoneNumber) {
        this.nickname = name;
        this.location = email;
        this.phoneNumber = phoneNumber;
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
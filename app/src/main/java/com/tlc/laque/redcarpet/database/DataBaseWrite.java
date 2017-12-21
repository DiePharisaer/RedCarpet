package com.tlc.laque.redcarpet.database;


import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tlc.laque.redcarpet.parties.Party;
import com.tlc.laque.redcarpet.users.User;
/**
 * Created by User on 03/12/2017.
 *
 * Class to get data from the dataBase
 */

public class DataBaseWrite {
    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]
    private FirebaseAuth mAuth;
    private String userID;
    DataBaseRead dr;

    public DataBaseWrite(){
        dr = new DataBaseRead();
        this.userID  = dr.getUserId();
    }

    //Add/Upload new User in the DataBase and save also the phoneNumber
    public void writeUser(User user){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser userA = mAuth.getCurrentUser();
        String userID = userA.getUid();               //GET ID FROM AUTHENTICATION
        user.setPhoneNumber(userA.getPhoneNumber());
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference("users/"+userID);
        // [END initialize_database_ref]
        mDatabase.child("nickname").setValue(user.getNickname());
        mDatabase.child("location").setValue(user.getLocation());
        mDatabase.child("phoneNumber").setValue(user.getPhoneNumber());
        mDatabase.child("privacy").setValue(user.getPrivacy());
    }

    public void registerUser(String userID, String idParty){
        mDatabase = FirebaseDatabase.getInstance().getReference("Parties/"+idParty);
        // [END initialize_database_ref]
        mDatabase.child("userAttending").child(userID).setValue(userID);
        DataBaseRead dbR = new DataBaseRead();
        mDatabase = FirebaseDatabase.getInstance().getReference("users/"+dbR.getUserId());
        // [END initialize_database_ref]
        mDatabase.child("partyAttending").child(idParty).setValue(idParty);
    }

    public void cancelRegisterUser(String userID, String idParty, String nameParty){
        mDatabase = FirebaseDatabase.getInstance().getReference("Parties/"+idParty);
        mDatabase.child("userAttending").child(userID).removeValue();
        DataBaseRead dbR = new DataBaseRead();
        mDatabase = FirebaseDatabase.getInstance().getReference("users/"+dbR.getUserId());
        mDatabase.child("partyAttending").child(idParty).removeValue();

    }

    public void addFriendAttending(String userIDF){
        mDatabase = FirebaseDatabase.getInstance().getReference("users/"+userIDF);
        mDatabase.child("friendAttending").child(userID).setValue(userID);
        mDatabase = FirebaseDatabase.getInstance().getReference("users/"+userID);
        mDatabase.child("friendSent").child(userIDF).setValue(userIDF);
    }

    public void acceptFriendRequest(String userIDF){
        mDatabase = FirebaseDatabase.getInstance().getReference("users/"+userIDF);
        mDatabase.child("friends").child(userID).setValue(userID);
        mDatabase = FirebaseDatabase.getInstance().getReference("users/"+userID);
        mDatabase.child("friends").child(userIDF).setValue(userIDF);
        cancelGetFriendRequests(userIDF);
    }

    public void cancelGetFriendRequests(String userIDF){
        mDatabase = FirebaseDatabase.getInstance().getReference("users/"+userIDF);
        mDatabase.child("friendSent").child(userID).removeValue();
        mDatabase = FirebaseDatabase.getInstance().getReference("users/"+userID);
        mDatabase.child("friendAttending").child(userIDF).removeValue();
    }

    public void cancelSentFriendRequest(String userIDF){
        mDatabase = FirebaseDatabase.getInstance().getReference("users/"+userIDF);
        mDatabase.child("friendAttending").child(userID).removeValue();
        mDatabase = FirebaseDatabase.getInstance().getReference("users/"+userID);
        mDatabase.child("friendSent").child(userIDF).removeValue();
    }

    public void unFriend(String userIDF){
        mDatabase = FirebaseDatabase.getInstance().getReference("users/"+userIDF);
        mDatabase.child("friends").child(userID).removeValue();
        mDatabase = FirebaseDatabase.getInstance().getReference("users/"+userID);
        mDatabase.child("friends").child(userIDF).removeValue();
    }

    public void refuseFriendRequest(String userIDF){
        mDatabase = FirebaseDatabase.getInstance().getReference("users/"+userIDF);
        mDatabase.child("friendSent").child(userID).removeValue();
        mDatabase = FirebaseDatabase.getInstance().getReference("users/"+userID);
        mDatabase.child("friendAttending").child(userIDF).removeValue();

    }

    public void createParty(Party p){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = database.getReference("administration/attendingParty/").push().getKey();
        mDatabase = FirebaseDatabase.getInstance().getReference("administration/attendingParty/"+key);
        // [END initialize_database_ref]
        mDatabase.child("name").setValue(p.getName());
        mDatabase.child("info").setValue(p.getInfo());
        mDatabase.child("location").setValue(p.getLocation());
        mDatabase.child("timeStart").setValue(p.getTimeStart());
        mDatabase.child("timeFinish").setValue(p.getTimeFinish());
        mDatabase.child(("url")).setValue(p.getUrl());
        mDatabase.child("rating").setValue("");

    }

    public void deleteAttendingPArty(String partyID){

        mDatabase = FirebaseDatabase.getInstance().getReference("administration/attendingParty");
        mDatabase.child(partyID).removeValue();
    }
    public void saveNewParty(Party p){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            String key = database.getReference("Parties").push().getKey();
            mDatabase = FirebaseDatabase.getInstance().getReference("Parties/"+key);
            // [END initialize_database_ref]
            mDatabase.child("name").setValue(p.getName());
            mDatabase.child("info").setValue(p.getInfo());
            mDatabase.child("location").setValue(p.getLocation());
            mDatabase.child("timeStart").setValue(p.getTimeStart());
            mDatabase.child("timeFinish").setValue(p.getTimeFinish());
            mDatabase.child(("url")).setValue(p.getUrl());
            mDatabase.child("rating").setValue("");
    }

}

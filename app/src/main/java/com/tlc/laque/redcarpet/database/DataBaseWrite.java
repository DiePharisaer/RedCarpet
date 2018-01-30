package com.tlc.laque.redcarpet.database;


import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
        mDatabase.child("rating").setValue(user.getRating());
        mDatabase.child("numberVote").setValue(user.getNumberVote());
        mDatabase.child("urlPicture").setValue(user.getUrlPicture());
    }

    public void registerUser(String userID, String idParty){
        mDatabase = FirebaseDatabase.getInstance().getReference("Parties/"+idParty);
        // [END initialize_database_ref]
        mDatabase.child("userAttending").child(userID).setValue(userID);     //Save User Id in the Party Attending
        DataBaseRead dbR = new DataBaseRead();
        mDatabase = FirebaseDatabase.getInstance().getReference("users/"+dbR.getUserId());
        // [END initialize_database_ref]
        mDatabase.child("partyAttending").child(idParty).setValue(idParty);         //Save Party Id in the User party attending
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
        mDatabase.child("organizer").setValue(p.getOrganizer());

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
             mDatabase.child("organizer").setValue(p.getOrganizer());
    }

    public void moveParty(Party p){
        /* mDatabase = FirebaseDatabase.getInstance().getReference("Parties");
        mDatabase.child(p.getKey()).removeValue();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("PartiesFinished/"+p.getKey());
        // [END initialize_database_ref]
        mDatabase.child("name").setValue(p.getName());
        mDatabase.child("info").setValue(p.getInfo());
        mDatabase.child("location").setValue(p.getLocation());
        mDatabase.child("timeStart").setValue(p.getTimeStart());
        mDatabase.child("timeFinish").setValue(p.getTimeFinish());
        mDatabase.child(("url")).setValue(p.getUrl());
        mDatabase.child("rating").setValue("");
        mDatabase.child("organizer").setValue(p.getOrganizer());*/
        DatabaseReference mDataBaseFrom = FirebaseDatabase.getInstance().getReference("Parties/"+p.getKey());
        DatabaseReference mDataBaseTo = FirebaseDatabase.getInstance().getReference("PartiesFinished/"+p.getKey());
        moveParty(mDataBaseFrom, mDataBaseTo, p.getKey());
    }

    public void uploadPictureUser(String url){

        mDatabase = FirebaseDatabase.getInstance().getReference("users/"+userID);
        // [END initialize_database_ref]
        mDatabase.child("urlPicture").setValue(url);
    }


    //Vote Party
    public void voteParty(Party p,String keyOrganizer, String oldrate, float newRate, String numberVote){
        float newR = Float.valueOf(oldrate);
        int numberV = Integer.parseInt(numberVote);
        numberV = numberV + 1;            //increment Vote by 1
        newR = newR + newRate;              //Incremente Rating with the new one
        mDatabase = FirebaseDatabase.getInstance().getReference("users/"+keyOrganizer);         //Get reference og the organizer user
        // [END initialize_database_ref]
        mDatabase.child("rating").setValue(newR);           // Save new Rating
        mDatabase.child("numberVote").setValue(numberV);       //Save new  numberVote incremented by 1
        mDatabase = FirebaseDatabase.getInstance().getReference("PartiesFinished/"+p.getKey()+"/userAttending");
        mDatabase.child(keyOrganizer).removeValue();
    }
    private void moveParty(final DatabaseReference fromPath, final DatabaseReference toPath, final String key) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                        if (firebaseError != null) {
                            System.out.println("Copy failed");
                        } else {
                            System.out.println("Success");
                            mDatabase = FirebaseDatabase.getInstance().getReference("Parties");
                            mDatabase.child(key).removeValue();
                            mDatabase = FirebaseDatabase.getInstance().getReference("users/"+dr.getUserId()+"partyAttending/");
                            mDatabase.child(key).removeValue();

                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

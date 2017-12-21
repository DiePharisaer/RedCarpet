

package com.tlc.laque.redcarpet.database;


import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tlc.laque.redcarpet.R;
import com.tlc.laque.redcarpet.parties.Party;
import com.tlc.laque.redcarpet.settings.UserSettingActivity;
import com.tlc.laque.redcarpet.users.ListAdapterUser;
import com.tlc.laque.redcarpet.users.ListUsers;
import com.tlc.laque.redcarpet.users.User;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * DataBaseRead class
 * method the get data from the Database (maybe will be useless?)
 */

public class DataBaseRead {
    private User user;
    private DatabaseReference mDatabase;
    private String userId;
    private ArrayList parties;
    public boolean ready = false;
    private FirebaseAuth mAuth;
    private ArrayList users;
    private ArrayList<String> arrayUsersId;


    public DataBaseRead() {
        arrayUsersId = new ArrayList<>();

    }

    public String getUserId() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser userA = mAuth.getCurrentUser();
        String userID = userA.getUid();
        return userID;
    }

    public ArrayList<Party> getAllParties(DataSnapshot dataSnapshot) {
        parties = new ArrayList();
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            parties.add(getParty(postSnapshot));
        }
        return parties;

    }
    public ArrayList<String> getIdUser(DataSnapshot dataSnapshot){
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            if(postSnapshot.getValue() == null){}                    //If user exist show the old informations
            else {
                arrayUsersId.add(postSnapshot.getKey().toString());
            }
        }
        return arrayUsersId;
    }

    public User getUser(DataSnapshot postSnapshot){

        User u = new User();
        u.setNickname(postSnapshot.child("nickname").getValue().toString());
        u.setLocation(postSnapshot.child("location").getValue().toString());
        u.setPhoneNumber(postSnapshot.child("phoneNumber").getValue().toString());
        u.setNumberFriends(postSnapshot.child("friends").getChildrenCount());
        u.setKey(postSnapshot.getKey().toString());
        u.setFriendOrNot(postSnapshot.child("friends/"+getUserId()).exists());
        u.setSentFriendRequest(postSnapshot.child("friendAttending/"+getUserId()).exists());
        u.setGotFriendRequest(postSnapshot.child("friendSent/"+getUserId()).exists());
        u.setPrivacy((postSnapshot.child("privacy").getValue().toString()));
        u.setNumberFriendsRequestS((int)postSnapshot.child("friendAttending").getChildrenCount());

        return u;
    }
    public ArrayList<User> getAllUsers(DataSnapshot dataSnapshot){
        users = new ArrayList();

        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            if(!(postSnapshot.getValue() == null))                    //If user exist show the old informations
            users.add(getUser(postSnapshot));
        }
        return users;
    }

     public Party getParty(DataSnapshot postSnapshot){
         Party p = new Party();
         p.setLocation(postSnapshot.child("location").getValue().toString());
         p.setInfo(postSnapshot.child("info").getValue().toString());
         p.setTimeStart(postSnapshot.child("timeStart").getValue().toString());
         p.setTimeFinish((postSnapshot.child("timeFinish").getValue().toString()));
         p.setName((postSnapshot.child("name").getValue().toString()));
         p.setRating(postSnapshot.child("rating").getValue().toString());
         p.setUrl((postSnapshot.child("url").getValue().toString()));
         p.setKey(postSnapshot.getKey().toString());
         p.setNumUserAttending(countUsers(postSnapshot.child("userAttending")));
         return p;
     }
     private int countUsers(DataSnapshot dt){
         int n = 0;
            for(DataSnapshot t : dt.getChildren()){
                n++;
         }
         return n;
     }

    public ArrayList getParties() {
        return parties;
    }

    public  void  checkExist(final User user, User userOld, final Context context) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query;
        query = mDatabase.child("users").orderByChild("nickname/").equalTo("" + user.getNickname());

        if (user.getNickname().equalsIgnoreCase(userOld.getNickname())) {
            DataBaseWrite dr = new DataBaseWrite();
            dr.writeUser(user);
        } else {
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null) {
                        DataBaseWrite dr = new DataBaseWrite();
                        dr.writeUser(user);
                    } else {
                        Toast.makeText(context, "NickName already exist", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}

package com.tlc.laque.redcarpet.database;

import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tlc.laque.redcarpet.MainActivity;
import com.tlc.laque.redcarpet.registration.User;
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


    //Add/Upload new User in the DataBase and save also the phoneNumber
    public void writeUser(User user){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser userA = mAuth.getCurrentUser();
        String userID = userA.getUid();               //GET ID FROM AUTHENTICATION
        user.setPhoneNumber(userA.getPhoneNumber());;
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        // [END initialize_database_ref]
        mDatabase.child(userID).setValue(user);
    }


}

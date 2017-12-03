

package com.tlc.laque.redcarpet.database;


import com.google.firebase.database.DatabaseReference;
import com.tlc.laque.redcarpet.registration.User;
/**
 * DataBaseRead class
 * method the get data from the Database (maybe will be useless?)
 */

public class DataBaseRead {
    private User user;
    private DatabaseReference mDatabase;
    private String userId;

    public DataBaseRead(String userId){

    }

    public User getUser() {
        return user;
    }
}

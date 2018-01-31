package com.tlc.laque.redcarpet;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tlc.laque.redcarpet.database.DataBaseRead;
import com.tlc.laque.redcarpet.users.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]
    private FirebaseAuth mAuth;
    private User user;
    private User user2;
    DataBaseRead dr;
    private String userID;
    private Context instrumentationCtx;

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.tlc.laque.redcarpet", appContext.getPackageName());
    }

    @Before
    public void setUp() {
        dr = new DataBaseRead();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser userA = mAuth.getCurrentUser();
        userID = userA.getUid();               //GET ID FROM AUTHENTICATION
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference("users/" + userID);
        // [END initialize_database_ref]
        mDatabase.child("location").setValue("mondercange");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                }                    //If user exist show the old information
                else {
                    DataBaseRead dr = new DataBaseRead();
                    //user = getData(dataSnapshot, userId);
                    user = dr.getUser(dataSnapshot);
                    try {

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //Checking when I upload new information about the user are correctly saved in the databse
    @Test
    public void uploadWord_Test() throws Exception {
        while(user==null){}
        assertEquals("mondercange", user.getLocation());
        assertEquals(userID, dr.getUserId());
    }

    //Checking if the User already exist
    @Test
    public void checkAlreadyExist_Test() throws Exception {
        while(user==null){}
        User user3 = new User("test3333", "testlocation", "33", "33");
        instrumentationCtx = InstrumentationRegistry.getContext();
        user.setNickname("test");
        dr.checkExist(user, user3, instrumentationCtx);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                }                    //If user exist show the old information
                else {
                    DataBaseRead dr = new DataBaseRead();
                    //user = getData(dataSnapshot, userId);
                     user2 = dr.getUser(dataSnapshot);
                    try {

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        while(user2 == null){}
        assertNotEquals("test", user2.getNickname());
        assertEquals("Hamzaaa", user2.getNickname());
    }
}




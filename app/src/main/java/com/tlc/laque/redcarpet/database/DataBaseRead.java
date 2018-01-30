

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    private Party p;


    public DataBaseRead() {
        arrayUsersId = new ArrayList<>();

    }

    public String getUserId() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser userA = mAuth.getCurrentUser();
        String userID = userA.getUid();
        return userID;
    }

    public ArrayList<Party> getAllParties(DataSnapshot dataSnapshot) throws ParseException {
        parties = new ArrayList();
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            getParty(postSnapshot);
            if(!p.isPartyFinished())
                parties.add(p);
            else{
                DataBaseWrite dw = new DataBaseWrite();
                dw.moveParty(p);
            }
        }
        return parties;

    }
    public ArrayList<Party> getAllPartiesAttended(DataSnapshot dataSnapshot) throws ParseException {
        parties = new ArrayList();
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                parties.add(getParty(postSnapshot));
            }

        return parties;

    }

    public ArrayList<String> getIdUser(DataSnapshot dataSnapshot) {
        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            if (postSnapshot.getValue() == null) {
            }                    //If user exist show the old informations
            else {
                arrayUsersId.add(postSnapshot.getKey().toString());
            }
        }
        return arrayUsersId;
    }

    public User getUser(DataSnapshot postSnapshot) {

        User u = new User();
        u.setNickname(postSnapshot.child("nickname").getValue().toString());
        u.setLocation(postSnapshot.child("location").getValue().toString());
        u.setPhoneNumber(postSnapshot.child("phoneNumber").getValue().toString());
        u.setNumberFriends(postSnapshot.child("friends").getChildrenCount());
        u.setKey(postSnapshot.getKey().toString());
        u.setFriendOrNot(postSnapshot.child("friends/" + getUserId()).exists());
        u.setSentFriendRequest(postSnapshot.child("friendAttending/" + getUserId()).exists());
        u.setGotFriendRequest(postSnapshot.child("friendSent/" + getUserId()).exists());
        u.setPrivacy((postSnapshot.child("privacy").getValue().toString()));
        u.setNumberFriendsRequestS((int) postSnapshot.child("friendAttending").getChildrenCount());
        u.setRating(postSnapshot.child("rating").getValue().toString());
        u.setNumberVote(postSnapshot.child("numberVote").getValue().toString());
        u.setUrlPicture(postSnapshot.child("urlPicture").getValue().toString());

        return u;
    }

    public ArrayList<User> getAllUsers(DataSnapshot dataSnapshot) {
        users = new ArrayList();

        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
            if (!(postSnapshot.getValue() == null))                    //If user exist show the old informations
                users.add(getUser(postSnapshot));
        }
        return users;
    }

    public Party getParty(DataSnapshot postSnapshot) throws ParseException {
        p = new Party();
        p.setLocation(postSnapshot.child("location").getValue().toString());
        p.setInfo(postSnapshot.child("info").getValue().toString());
        p.setTimeStart(postSnapshot.child("timeStart").getValue().toString());
        p.setTimeFinish((postSnapshot.child("timeFinish").getValue().toString()));
        p.setName((postSnapshot.child("name").getValue().toString()));
        p.setRating(postSnapshot.child("rating").getValue().toString());
        p.setUrl((postSnapshot.child("url").getValue().toString()));
        p.setKey(postSnapshot.getKey().toString());
        p.setOrganizer(postSnapshot.child("organizer").getValue().toString());
        p.setNumUserAttending(countUsers(postSnapshot.child("userAttending")));
        checkTime(p.getTimeStart(),p.getTimeFinish());
        return p;
    }

    private int countUsers(DataSnapshot dt) {
        int n = 0;
        for (DataSnapshot t : dt.getChildren()) {
            n++;
        }
        return n;
    }

    public ArrayList getParties() {
        return parties;
    }

    public void checkExist(final User user, final User userOld, final Context context) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query;
        query = mDatabase.child("users").orderByChild("nickname/").equalTo("" + user.getNickname());

        if (user.getNickname().equalsIgnoreCase(userOld.getNickname())) {
            DataBaseWrite dr = new DataBaseWrite();
            userOld.setLocation(user.getLocation());
            userOld.setPrivacy(user.getPrivacy());
            userOld.setUrlPicture(user.getUrlPicture());
            dr.writeUser(userOld);
        } else {
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null) {
                        DataBaseWrite dr = new DataBaseWrite();
                        userOld.setLocation(user.getLocation());
                        userOld.setNickname(user.getNickname());
                        userOld.setPrivacy(user.getPrivacy());
                        userOld.setUrlPicture(user.getUrlPicture());
                        dr.writeUser(userOld);
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

    private void checkTime(String a, String b) throws ParseException {
        b = b.replace("Hour:","");
        b = b+ ":00";
        a = a.replace("Hour:","");
        a = a+ ":00";
        Date today = new Date();
        int myYearS = Integer.parseInt(a.substring(6, 10));
        int myMonthS = Integer.parseInt(a.substring(3, 5));
        int myDayS = Integer.parseInt(a.substring(0, 2));
        int myYearF = Integer.parseInt(b.substring(6, 10));
        int myMonthF = Integer.parseInt(b.substring(3, 5));
        int myDayF = Integer.parseInt(b.substring(0, 2));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss");
        java.util.Date myDateS = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                .parse(a);
        java.util.Date myDateF = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                .parse(b);
      //  Date myDate = new Date(myYear, myMonth - 1, myDay);
        if (today.compareTo(myDateS) < 0) {
            System.out.println("Today Date is Lesser than my Date");
            p.setPartyStarted(false);
            p.setPartyFinished(false);
        }
        else if (today.compareTo(myDateS) > 0 && today.compareTo(myDateF) < 0 ){
            System.out.println("Today Date is Greater than my date Start and Lesser than mt Date Finish");
            p.setPartyFinished(false);
            p.setPartyStarted(true);
        }
        else {
            System.out.println("Both Dates are equal");
            p.setPartyStarted(false);
            p.setPartyFinished(true);
        }
    }
}

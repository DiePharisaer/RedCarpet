package com.tlc.laque.redcarpet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.tlc.laque.redcarpet.registration.PhoneAuthActivity;

/*
This is the launcher activity.
It's checking is the user has already logged in or not
if it's yes is moving to the activityMain
else it's is riderecting to the registrationpart
// */

public class StartActivity extends AppCompatActivity {
    private boolean logB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        logB = sharedPref.getBoolean("logB", false);

        //CHECK REFERENCES IF THE USERS HAS ALREADY LOGGED IN (TIMER 2 SECONDS)
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    if(logB) {
                        //mainActivity, already logged in
                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                        startActivity(intent);     //MOVE TO MAINACTIVITY
                        finish();
                    }
                    else {
                        Intent intent = new Intent(StartActivity.this, PhoneAuthActivity.class);
                        startActivity(intent);     //MOVE TO PhoneVerification
                        finish();
                    }
                }
            }
        };
        timerThread.start();
    }

}

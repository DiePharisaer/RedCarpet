package com.tlc.laque.redcarpet.parties;

import java.util.ArrayList;

/**
 * Created by User on 05/12/2017.
 * set aLl information about the Party
 */

public class Party {
    private String key;                 //Key of the PArty
    private String location;            //Location of the Party
    private String info;                //Information of the Party
    private String rating;              //Rating of the PArty
    private String timeStart;           //Time Start(Date and time) of the Party
    private String timeFinish;          //Time Finish(Data and time) of the Party
    private String name;                //Name of the Party (Different from the Key)
    private int  numUserAttending;      //number of User Attending the Party
    private String url;                 //Url download picture of the party
    private boolean partyStarted;       //if the party is already started
    private boolean partyFinished;
    private String organizer;
    public  Party(){
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public boolean isPartyStarted() {
        return partyStarted;
    }

    public boolean isPartyFinished() {
        return partyFinished;
    }

    public void setPartyFinished(boolean partyFinished) {
        this.partyFinished = partyFinished;
    }

    public void setPartyStarted(boolean partyStarted) {
        this.partyStarted = partyStarted;
    }

    public String getKey() {
        return key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getNumUserAttending() {
        return numUserAttending;
    }

    public void setNumUserAttending(int numUserAttending) {
        this.numUserAttending = numUserAttending;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeFinish() {
        return timeFinish;
    }

    public void setTimeFinish(String timeFinish) {
        this.timeFinish = timeFinish;
    }
}

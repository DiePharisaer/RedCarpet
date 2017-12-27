package com.tlc.laque.redcarpet.inputs;

/**
 * Created by User on 27/12/2017.
 */

public class Contact {
    private String contactName;
    private String contactNumber;

    public Contact(){

    }

    public Contact(String contactName, String contactNumber){
        this.contactName=contactName;
        this.contactNumber=contactNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
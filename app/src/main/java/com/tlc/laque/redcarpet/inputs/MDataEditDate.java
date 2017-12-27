package com.tlc.laque.redcarpet.inputs;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import org.w3c.dom.Text;

import java.util.Calendar;

/**
 * Created by User on 26/12/2017.
 */

public class MDataEditDate implements TextWatcher {
     private EditText ed;
    public MDataEditDate(EditText ed){
        this.ed = ed;
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String working = s.toString();
        boolean isValid = true;
        if (working.length() == 2 && before == 0) {
            if (Integer.parseInt(working) < 1 || Integer.parseInt(working) > 31) {
                isValid = false;
            } else {
                working += "/";
                ed.setText(working);
                ed.setSelection(working.length());
            }
        } else if (working.length() == 5 && before == 0) {
            String enteredMonth = working.substring(3);
            if (Integer.parseInt(enteredMonth) < 1 || Integer.parseInt(enteredMonth) > 12) {
                isValid = false;
            } else {
                working += "/";
                ed.setText(working);
                ed.setSelection(working.length());
            }
        } else if (working.length() == 10 && before == 0) {
            String enteredYear = working.substring(6);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            if (Integer.parseInt(enteredYear) < currentYear) {
                isValid = false;
            }
            else{
                working += " Hour:";
                ed.setText(working);
                ed.setSelection(working.length());
            }
        } else if (working.length() == 18 && before == 0) {
            String enteredHour = working.substring(16);
            if (Integer.parseInt(enteredHour) < 0 || Integer.parseInt(enteredHour) > 24) {
                isValid = false;}
            else{
                working += ":";
                ed.setText(working);
                ed.setSelection(working.length());
            }
        } else if (working.length() == 21 && before == 0) {
            String enteredMinutes = working.substring(19);
            if (Integer.parseInt(enteredMinutes) < 0 || Integer.parseInt(enteredMinutes) > 60) {
                isValid = false;}
        } else if (working.length() != 21) {
            isValid = false;
        }

        if (!isValid) {
            ed.setError("Enter a valid date: DD/MM/YYYY and Time HH:MM");
        } else {
            ed.setError(null);
        }

    }
    @Override
    public void afterTextChanged(Editable s) {}

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
}

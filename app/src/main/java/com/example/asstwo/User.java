/*
TODO:
    - add code to manage your data base more efficiently and effectively after
    - password should not be the responsibility of the user but, it should be the
    respoinsbility of the application which is going to be the graph data structure
 */
package com.example.asstwo;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.lang.String;
import java.io.Serializable;

public abstract class User implements Serializable {

    //PRIVATE CLASS FIELDS
    private String firstName;
    private String lastName;
    protected ArrayList<Integer> phoneNumbers;
    protected ArrayList<String> emailAddresses;
    protected ArrayList<TestHistory> history;
    private Avatar avatar;

    private static final String TAG = "user.";

    public User() {
        firstName = "John";
        lastName = "Doe";
        avatar = new Avatar();
        history = new TestHistory();
        phoneNumbers = new ArrayList<>();
        emailAddresses = new ArrayList();
    }

    public User(String inFirstName, String inLastName) {
        if (validateName(inFirstName) && validateName(inLastName)) {
            firstName = inFirstName;
            lastName = inLastName;

            //all these  class fields should be changed through mutators as the original values should be always nothing
            phoneNumbers = new ArrayList<>();
            emailAddresses = new ArrayList<>();
            history = new TestHistory();
            avatar = new Avatar();
        }
    }

    //ACCESSOR METHODS
    public String getFirstName() {
        return new String(firstName);
    }

    public String getLastName() {
        return new String(lastName);
    }

    public List<String> getEmails() {
        return emailAddresses;
    }

    public List<Integer> getNumbers() {
        return phoneNumbers;
    }

    public ArrayList<TestHistory> getHistory()
    {
        /*TODO: you will need to make a function which is going to sort the history entries */
        return history;
    }


    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar inAvatar) {
        avatar = inAvatar;
    }


    public void addHistoryEntry(String inHistory) {
        //TODO: come back and actually implement this method when you get to that part of the functionality
    }

    public void setLastName(String inLastName) {

        if (validateName(inLastName)) {
            lastName = inLastName;
        }
    }

    public void setFirstName(String inFirstName) {

        if (validateName(inFirstName)) {
            firstName = inFirstName;
        }
    }

    public void addEmail(String inEmail) {
        if (validateEmail(inEmail)) {
            if (validateEmailLength()) {
                emailAddresses.add(inEmail);
            }
        }
    }

    public void setAvatar(String avatarName, int drawableID) {
        avatar = new Avatar(avatarName, drawableID);
    }

    public String getType() {
        return "USER";
    }


    /***********************************************************************************************
     * ASSERTION: the function is going to break if an empty string is given as its input
     ***********************************************************************************************/
    protected boolean validateName(String inName) {
        boolean valid = true;

        if (inName.length() == 0) {
            throw new IllegalArgumentException("ERROR: invalid name: " + inName);
        }
        return valid;

    }


    /***********************************************************************************************
     * ASSERTION: function will break if the email is not in the format of string followed by the
     * "@" symbol another string followed by .com
     ***********************************************************************************************/
    protected boolean validateEmail(String inEmail) {

        /*this code is going to be adapted from the following
            source: https://howtodoinjava.com/java/regex/java-regex-validate-email-address/
            data accessed: 7th of September 2021
            publish data: 26th of December 2020
            Author: Lokesh Gupta
         */

        //the string must match this following pattern otherwise, the string is not valid
        boolean valid = true;
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inEmail);

        if (!(matcher.matches())) {
            throw new IllegalArgumentException("ERROR: invalid email: " + inEmail);
        }

        return valid;
    }

    protected boolean validateEmailLength() {
        boolean valid = true;
        if (emailAddresses.size() > 10) {
            throw new IllegalArgumentException("ERROR: Only 10 email address are allowed for the user");
        }

        return valid;
    }

    protected void sortHistory()
    {
        for (TestHistory currHist : history)
        {
        }
    }
}

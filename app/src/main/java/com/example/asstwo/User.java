/*
TODO:
    - add code to manage your data base more efficiently and effectively after
    - password should not be the responsibility of the user but, it should be the
    respoinsbility of the application which is going to be the graph data structure
 */
package com.example.asstwo;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.lang.String;
import java.io.Serializable;

public abstract class User implements Serializable {

    //PRIVATE CLASS FIELDS
    private String firstName;
    private String lastName;
    protected ArrayList<String> phoneNumbers;
    protected ArrayList<String> emailAddresses;
    protected ArrayList<TestHistory> history;
    private Avatar avatar;

    private static final String TAG = "user.";

    public User() {
        firstName = "John";
        lastName = "Doe";
        avatar = new Avatar();
        history = new ArrayList<>();
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
            history = new ArrayList<>();
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

    public ArrayList<String> getEmails() {
        return emailAddresses;
    }

    public ArrayList<String> getNumbers() {
        return phoneNumbers;
    }

    public String getName()
    {
        return firstName + " " + lastName;
    }

    public ArrayList<TestHistory> getHistory()
    {
        return history;
    }

    public int indexOfTitle(String inTitle)
    {
        int index = -1;

        for  (TestHistory test : history)
        {
            index++;
            if (test.getTestTitle().equals(inTitle))
            {
                // once the test has being found the algorithm will now waste time
                //looking for the remaining tests
                return index;
            }
        }

        return index;
    }


    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar inAvatar) {
        avatar = inAvatar;
    }


    public void addHistoryEntry(TestHistory inEntry) {
        Log.e(TAG, "History has being added: " + inEntry.getScore());
        this.history.add(inEntry);
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

    public void addEmail(String inEmail)
    {
        if (validateEmail(inEmail)) {
            if (validateEmailLength()) {
                emailAddresses.add(inEmail);
            }
        }
    }

    public void addPhoneNum(String inNum)
    {
        if (validatePhoneNumber(inNum))
        {
            if(validatePhoneNumberLength())
            {
                phoneNumbers.add(inNum);
            }

        }
    }

    public void setEmails(ArrayList<String> inEmails)
    {
        emailAddresses = new ArrayList<>(inEmails);
    }

    public void setPhoneNumbers(ArrayList<String> inPhoneNums)
    {
        phoneNumbers = new ArrayList<>(inPhoneNums);
    }

    //to check if the current user has at least more than one phone number available
    public boolean checkPhoneNumbers()
    {
        boolean valid = false;

        if(phoneNumbers.size() > 0)
        {
            valid = true;
        }

        return  valid;
    }

    //checking if the user has more at least more than one email address available to use
    public boolean checkEmailAddresses()
    {
        boolean valid = false;

        if(emailAddresses.size() > 0)
        {
            valid = true;
        }

        return valid;
    }

    //TODO: you will need to refactor this so it's going to be a bitmap because all the images which
    //you have saved are going to be a bit map
    public void setAvatar(String avatarName, Bitmap image) {
        avatar = new Avatar(avatarName, image);
    }

    public String getType() {
        return "USER";
    }

    public String toString()
    {
        throw new IllegalArgumentException("Error: to be implemented");
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
        if (emailAddresses.size() == 10) {
            throw new IndexOutOfBoundsException("ERROR: Only 10 email address are allowed for the user");
        }

        return valid;
    }

    protected boolean validatePhoneNumber(String inNum)
    {
        boolean valid = true;
        int currLength = inNum.length();
        if(!(currLength >= 10 && currLength <= 15))
        {
            throw new IllegalArgumentException("ERROR: the phone number must be between 10 and 15 digits long");
        }

        return valid;
    }

    protected boolean validatePhoneNumberLength()
    {
        boolean valid = true;

        if(phoneNumbers.size() ==  10)
        {
            throw new IndexOutOfBoundsException("Error: you're alllowed a maximum of 10 phone numbers per person");
        }
        return valid;
    }
}

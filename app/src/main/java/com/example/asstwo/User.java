/*
TODO:
    - add code to manage your data base more efficiently and effectively after
    - password should not be the responsibility of the user but, it should be the
    respoinsbility of the application which is going to be the graph data structure
 */
package com.example.asstwo;
import android.util.Log;

import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.lang.String;
import java.io.Serializable;

public abstract class User implements Serializable
{

    //PRIVATE CLASS FIELDS
    private String name;
    //assuming username is going to be the staff id or student id
    private String userName;
    private String email;
    private String country;
    protected HashMap<String, TestHistory> practicals;
    private int password;
    private Avatar avatar;

    private static final String TAG = "user.";

    public User()
    {
        name = "John Doe";
        userName = "12345678";
        email = "johnDoe@curtin.edu.au";
        country = "AUSTRALIA";
        password = 1234;
        practicals = new HashMap<String, TestHistory>();
    }

    public User(String inName, String inUserName, String inEmail, String inCountry)
    {
        if(validateName(inName) && validateUserName(inUserName) && validateEmail(inEmail) &&
            validateCountry(inCountry))
        {
            name = inName;
            userName = inUserName;
            email = inEmail;
            country = inCountry;
            password = 1234;
            practicals = new HashMap<String, TestHistory>();
        }
    }

    //ACCESSOR METHODS
    public String getName()
    {
        return new String(name);
    }

    public String getUserName()
    {
        return new String(userName);
    }

    public String getEmail()
    {
        return new String(email);
    }

    public String getCountry()
    {
        return new String(country);
    }

    public Avatar getFlag()
    {
        return avatar;
    }

    public void setFlag(Avatar inAvatar)
    {
        avatar = inAvatar;
    }

    public TestHistory getPrac(String inTitle)
    {
        String titleKey = myUtils.cleanString(inTitle);
        //if this doesn't exist it should be picked up the dictonary object
        return practicals.get(titleKey);

    }

    public HashMap<String, TestHistory> getPracticals()
    {
        return new HashMap<>(practicals);
    }

    public String dispPrac(String title)
    {
        String pracDetails = "";
        if(validateName(title))
        {
            if (practicals.isEmpty())
            {
                throw new IllegalArgumentException("Error: no practicals added for current student: " + practicals.size());
            }
            else
            {
                //cleaning any white spaves, and making the title case insensitive
                title = myUtils.cleanString(title);
                TestHistory currPrac = practicals.get(title);
                pracDetails = currPrac.toString();
            }
        }

        return pracDetails;
    }

    public void addPrac(TestHistory inPrac)
    {
        String pracName = myUtils.cleanString(inPrac.getTitle());
        Log.e(TAG, "Practical added to user: " + inPrac.getTitle());
        Log.e(TAG, "the added practical object: " + inPrac);
        practicals.put(pracName, inPrac);
    }

    public int getPassword()
    {
        return password;
    }

    //MUTATOR METHODS
    public void setName(String inName)
    {
        if(validateName(inName))
        {
            name = inName;
        }
    }

    public void setUserName(String inUserName)
    {

        if(validateUserName(inUserName))
        {
            userName = inUserName;
        }

    }

    public void setEmail(String inEmail)
    {
        if(validateEmail(inEmail))
        {
            email = inEmail;
        }
    }

    public void setCountry(String inCountry)
    {
        if (validateCountry(inCountry))
        {
            country = inCountry;
        }
    }

    public void setPracticals(HashMap<String, TestHistory> inPracs)
    {
        practicals = inPracs;
    }

    public void setPassword(int inPassword)
    {
        password = inPassword;
    }

    public String getType()
    {
        return "USER";
    }


    /***********************************************************************************************
     * ASSERTION: always return a string which is going to be in the following format
     * [name user], [user id], [user email], [country]
     ***********************************************************************************************/
    public String toString()
    {
        return  name +","+ userName +","+ email +","+ country;
    }

    /***********************************************************************************************
     * ASSERTION: the function is going to break if an empty string is given as its input
     ***********************************************************************************************/
    protected boolean validateName(String inName)
    {
        boolean valid = true;

        if(inName.length() == 0)
        {
            throw new IllegalArgumentException("ERROR: invalid name: " + inName);
        }
        return valid;

    }


    /***********************************************************************************************
     * ASSERTION: function will break if the email is not in the format of string followed by the
     * "@" symbol another string followed by .com
     ***********************************************************************************************/
    protected boolean validateEmail(String inEmail)
    {

        /*this code is going to be adapted from the following
            source: https://howtodoinjava.com/java/regex/java-regex-validate-email-address/
            data accessed: 7th of September 2021
            publish data: 26th of December 2020
            Author: Lokesh Gupta
         */

        //the string must match this following pattern otherwise, the string is not valid
        boolean valid = true;
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern  pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inEmail);

        if(!(matcher.matches()))
        {
            throw new IllegalArgumentException("ERROR: invalid email: " + inEmail);
        }

        return valid;
    }


    /***********************************************************************************************
     * ASSERTION: a valid user name is going to have 6 numbers, followed by a captial letter for the
     * staff id
     ***********************************************************************************************/
    protected boolean validateUserName(String inUserName)
    {
        boolean valid = true;
        //what we will want to match
        String regex = "[0-9]{6}[A-Z]{1}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inUserName);

        if(!(matcher.matches()))
        {
            throw new IllegalArgumentException("ERROR: invalid staff id: " + inUserName);
        }

        return valid;

    }

    protected boolean validateCountry(String inCountry)
    {
        boolean valid = true;

        if (inCountry.length() == 0)
        {
            throw new IllegalArgumentException("Error: country can't be an empty string" + inCountry);
        }
        return valid;
    }

    protected boolean managePassWord(int inPassword)
    {
        boolean valid = true;
        if (validatePassword(inPassword))
        {
            //if the password is going to be valid

            //TODO: you will need to add code to hash your password so it's more secure to store your password

            //adding the password into my database

        }

        return valid;
    }

    protected boolean validatePassword(int inPassword)
    {
        boolean valid = true;
        int numDigits = String.valueOf(inPassword).length();
        if (numDigits != 4)
        {
            throw new IllegalArgumentException("ERROR: you must have four digits for your pin");
        }
        return valid;
    }
}

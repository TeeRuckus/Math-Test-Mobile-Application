/* TODO:
    - you will need to over ride the  validate user name method because the way which
    a student and a staff's usert name is going to be stored is going to be really different
 */
package com.example.asstwo;
import android.util.Log;

import java.util.*;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Student extends User implements Serializable
{
    //the classfield which are going to be unique to the student
    private String currInstructor;
    private static final String TAG = "Student.";

    public Student()
    {
        super();
        currInstructor = "Admin";
    }

    //we will need an alternate constructor when the admin will create student
    public Student(String inName, String inUserName, String inEmail, String inCountry, String inInstructor)
    {
        super(inName, inUserName, inEmail, inCountry);
        if (super.validateName(inInstructor))
        {
            currInstructor = inInstructor;
        }
    }


    public String getInstructor()
    {
        return new String(currInstructor);
    }

    public void setInstructor(String inInstructor)
    {
        if(validateInstructor(inInstructor))
        {
            currInstructor = inInstructor;
        }

    }

    public ArrayList<TestHistory> pracLoad()
    {
        ArrayList<TestHistory> retList = new ArrayList<>();
        Set<String> keys = practicals.keySet();

        // creating an arrayList as it's a mutable data structure and can actually perform sorting and
        // can actually retrieve objects based on interger of the object
        List<String> keysOrdered = new ArrayList<>();

        Log.e(TAG, "the current keys of the student: " + keys);
        Log.e(TAG, "I am inside the prac load");

        // transferring keys to keysOrdered
        for (String currKey : keys)
        {
            Log.e(TAG, "Curr Key: " + currKey);
            keysOrdered.add(currKey);
        }

        // we want to sort the keys before we retrieve them from teh hash table
        Collections.sort(keysOrdered);

        for (String currKey : keysOrdered)
        {
            Log.e(TAG, "current key: " + currKey);
            Log.e(TAG, "retrieved object: " + practicals.get(currKey));
            retList.add(practicals.get(currKey));
        }

        return retList;
    }

    @Override
    public String getType()
    {
        return "STUDENT";
    }
    private boolean validateInstructor(String inInstructor)
    {
        boolean valid = true;

        if (inInstructor.length() == 0)
        {
            throw new IllegalArgumentException("ERROR: make sure that the instructor has more than one letter");
        }

        return valid;
    }

    @Override
    protected boolean validateUserName(String inUserName)
    {
        boolean valid = true;
        //the pattern which we will want to be matching for the student email address

        String regex = "[0-9]{8}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inUserName);

        if(!(matcher.matches()))
        {
            throw new IllegalArgumentException("ERROR: invali student id" + inUserName);
        }

        return  valid;
    }

    public String toString()
    {
        return "Student," + super.toString() + ",instructor: " + currInstructor;
    }
}

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
        currInstructor = "Current Admin";
    }

    public String getInstructor()
    {
        return new String(currInstructor);
    }

    public void setInstructor(String inInstructor)
    {
        throw new IllegalArgumentException("ERROR: functionality is not in use for this application");
    }

    //we will need an alternate constructor when the admin will create student
    public Student(String inFirstName, String inLastName)
    {
        super(inFirstName, inLastName);
    }

    @Override
    public String getType()
    {
        return "STUDENT";
    }
}

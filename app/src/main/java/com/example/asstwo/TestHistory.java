/*
TODO:
    - you will need ot implement a function which is going to go through all the pracitcals
    and find out what your mark was for that practical. I.e. what percentage you actuall got for that
    week
    - I am also really considiring chaing the scoredMarks from a float to a double
    - I should also be able to find out how much they scored in each section from this class whcih I am
    making at the current moment
 */
package com.example.asstwo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;


public class TestHistory implements Serializable
{
    private String testName;
    private ArrayList<MenuItem> questions;


    //default constructor
    public TestHistory()
    {
        testName = "Test 1";
        questions = new ArrayList<>();
    }

    //ALTERNATE CONSTRUCTOR
    public TestHistory(String inTitle, ArrayList<MenuItem> inQuestions)
    {
        if(validateTestName(inTitle))
        {
            this.testName = inTitle;
            this.questions = inQuestions;
        }
    }

    //ACCESSORS
    public String getTestTitle()
    {
        return testName;
    }

    public ArrayList<MenuItem> getQuestions()
    {
        return questions;
    }

    //MUTATORS
    public void setTitle(String inTitle)
    {
        this.testName = inTitle;
    }

    public void setQuestions(ArrayList<MenuItem> inQuestions)
    {
        this.questions = inQuestions;
    }

    //private methods
    private boolean validateTestName(String inTitle)
    {
        boolean valid = true;

        if (inTitle.length() == 0)
        {
            throw new IllegalArgumentException("Error: you can't have an emptry string as a title");
        }

        return valid;
    }
}
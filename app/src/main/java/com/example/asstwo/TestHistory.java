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

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;


public class TestHistory implements Serializable, Comparable
{
    private final String TAG = "TestHistory";
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

    public int getScore()
    {

        //the total score which the student received for the current test is going to be always the
        //question which they did for the last question as the questions are going to always set the
        //cumalitive score of the tests
        int size = questions.size();
        MenuItem lastQuestion = questions.get(size - 1);
        int score = lastQuestion.getScore();
        return lastQuestion.getScore();
    }

    //MUTATORS
    public void setTitle(String inTitle)
    {
        this.testName = new String(inTitle);
    }

    public void setQuestions(ArrayList<MenuItem> inQuestions)
    {
        this.questions = new ArrayList<MenuItem>(inQuestions);
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

    @Override
    public int compareTo(Object o) {
        //this funciton is to sort the inputted values in order. Therefore the highest one first, and
        //then the lowest one is going to be next as well
        TestHistory inHistory = (TestHistory) o;

        int returnValue = 0;
        if(this.getScore() == inHistory.getScore())
        {
            returnValue = 0;
        }
        else if (this.getScore() > inHistory.getScore())
        {
            returnValue = -1;
        }
        else
        {
            returnValue = 1;
        }

        return returnValue;
    }
}
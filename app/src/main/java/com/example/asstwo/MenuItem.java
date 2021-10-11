package com.example.asstwo;

import java.util.ArrayList;
import java.util.Arrays;

public class MenuItem {

    //private class fields for the class
    private String question;
    private int time;
    private int answer;
    private int[] options;
    private String response;
    private int currScore;

    //default constructor of the class
    public MenuItem()
    {
        question = "What is 1 + 1";
        time = 60;
        answer = 2;
        options = new int[] {3,2,4,11};
        response = "DID NOT ANSWER";
        currScore = 0;
    }

    //alternate constructor of the class
    public  MenuItem(String inQuestion, int inTime, int inAnswer, int[] inOptions)
    {
        // I am not going to validate the return results from the server as I am going to assume
        // they're going to be correct for the time being
        this.question = new String(inQuestion);
        this.time = inTime;
        this.answer = inAnswer;
        options = inOptions;
        response = "DID NOT ANSWER";
        currScore = 0;
    }

    //accessor methods for each menu item
    public String getQuestion()
    {
        return this.question;
    }

    public int getTime()
    {
        return this.time;
    }

    public int getAnswer()
    {
        return this.answer;
    }

    public int[] getOptions()
    {
        return options;
    }

    public void setAnswer(String inAnswer)
    {
        response = new String(inAnswer);
    }

    public void setCurrScore(int inScore)
    {
        currScore += inScore;
    }

    // this is going to tell us how many sections of answers are avaiable in sets of 4 so that it can
    // be displayed back to the user

    // this function is going to seperate the options into sets of  4 in their own individual indexes
    // in an array list
}

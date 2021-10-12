package com.example.asstwo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class MenuItem implements Serializable {

    //private class fields for the class
    private String question;
    private int time;
    private int answer;
    private int[] options;
    private String response;
    private int currScore;
    private int elapsedTime;

    //default constructor of the class
    public MenuItem()
    {
        question = "What is 1 + 1";
        time = 60;
        answer = 2;
        options = new int[] {3,2,4,11};
        response = "DID NOT ANSWER";
        currScore = 0;
        elapsedTime = 0;
    }

    //alternate constructor of the class
    public  MenuItem(String inQuestion, int inTime, int inAnswer, int[] inOptions)
    {
        // I am not going to validate the return results from the server as I am going to assume
        // they're going to be correct for the time being
        this.question = new String(inQuestion);
        this.answer = inAnswer;
        this.options = inOptions;
        this.response = "DID NOT ANSWER";
        this.currScore = 0;
        this.time = inTime;
        this.elapsedTime = 0;
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

    public int getScore()
    {
        return this.currScore;
    }

    public String getResponse()
    {
        return response;
    }


    public void setAnswer(String inAnswer)
    {
        response = new String(inAnswer);
    }

    public void setCurrScore(int inScore)
    {
        currScore += inScore;
    }

    public void setTime(int inTime)
    {
        time = inTime;
    }

    public void setElapsedTime(int inTime)
    {
        elapsedTime += inTime;
    }

    public void setResponse(String inResponse)
    {
        this.response = new String(inResponse);
    }

}

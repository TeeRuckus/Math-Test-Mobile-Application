package com.example.asstwo;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Set;

/*
    my utility class which are going to have common functions which are used in the programme
 */

//need to make it inheret from appcompat activity, so I can use some of the app functionalities
//public final class myUtils
public class myUtils extends AppCompatActivity
{
    //a look up table which we can use to find what the file code is for the current flag

    private SQLiteDatabase db;

    public myUtils()
    {
        //we want it to load with nothing in it
    }


    /*
    code was adapted from the following source: https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android
    Date accessed : 7/10/21 @ 1:00
     */
    public static Bitmap getImageStorage(String path)
    {
        android.graphics.Bitmap image = null;

        try
        {
            File file=new File(path, "profile.jpg");
            image = BitmapFactory.decodeStream(new FileInputStream(file));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return image;
    }

    //look up table for the country name which correspond to which country ID
    public static String cleanString(String inString)
    {
        //deleting all leading and lagging white spaces;
        inString = inString.trim();
        inString = inString.toUpperCase();
        return inString;
    }


    public static char getType(String currUserName, Graph inGraph)
    {
        Graph.Vertex currUser= inGraph.getVertex(currUserName);
        String userType = currUser.getValue().getType();
        return Character.toUpperCase(userType.charAt(0));
    }


    //-----------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    //joining two strings integers together to make a single integer
    public static int makePassword(int a, int b, int c, int d)
    {
        String s1 = Integer.toString(a);
        String s2 = Integer.toString(b);
        String s3 = Integer.toString(c);
        String s4 = Integer.toString(d);

        //joining all the numbers togger
        String password = s1 + s2 + s3 + s4;

        int retPassword = Integer.parseInt(password);

        return retPassword;

    }

    //these are going to be all database retrievals to make sure that the inputted password, matches
    //the inputted password
    public static boolean validatePassword(Admin inAdmin, int inPassword)
    {
        boolean valid = false;
        int actualPassword = getPassword(inAdmin);
        if ( actualPassword == inPassword)
        {
            valid = true;
        }
        return valid;
    }


    public static boolean validatePassword(Student inStudent, int inPassword)
    {
        boolean valid = false;
        int actualPassword = getPassword(inStudent);
        if ( actualPassword == inPassword)
        {
            valid = true;
        }
        return valid;
    }

    //database operations for creating the password inside the data base
    public void createPassword(Admin inAdmin, int inPassword)
    {
        String userName = cleanString(inAdmin.getName());

    }

    public void createPassword(Student inStudent, int inPassword)
    {
    }

    //these are  all going to be database access to set the pass word for the inputted user
    //TODO: you will need a cursor for getting the password  from your data base
    public static int getPassword(Admin inAdmin)
    {
        int password = 0;

        return  password;
    }


    public static int getPassword(Student inStudent)
    {
        int password = 0;

        return  password;
    }


    //only the methods which this class needs to kow about
    private static boolean validateRetrival(Object inObj, String lookUpKey)
    {
        boolean valid = true;
        if (inObj == null) {
            throw new IllegalArgumentException("ERROR: " + lookUpKey + " is not a registerd country");
        }
        return valid;
    }
}

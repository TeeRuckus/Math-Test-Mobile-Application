/*
TODO:
    - you will need to add code her for validating all your passwords, to make sure that the
    right passwords were set
 */
package com.example.asstwo;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.HashMap;
import java.util.Set;

/*
    my utility class which are going to have common functions which are used in the programme
 */

//need to make it inheret from appcompat activity, so I can use some of the app functionalities
//public final class myUtils
public class myUtils extends AppCompatActivity
{
    private static final String TAG = "myUtils.";

    //a look up table which we can use to find what the file code is for the current flag
    public static final HashMap<String, String> countryLookUp = new HashMap<String, String>() {{
        put("ANDORRO", "ad");
        put("UNITED ARAB EMIRATES", "ae");
        put("AFGHANISTAN", "af");
        put("ANTIGUA AND BARBUDA", "ag");
        put("ANGUILLA", "ai");
        put("ALBANIA", "al");
        put("ARMENIA", "am");
        put("ARGENTINA", "ar");
        put("AUSTRIA", "at");
        put("AUSTRALIA", "au");
        put("AZERBAIJAN", "az");
        put("BOSNIA AND HERZEGOVINA", "ba");
        put("BELGIUM", "bd");
        put("BURKINA FASO", "bf");
        put("BULGARIA", "bg");
        put("BRAZIL", "br");
        put("CANADA", "ca");
        put("SWITZERLAND", "ch");
        put("CHINA", "cn");
        put("CZECK REPUBLIC", "cz");
        put("GERMANY", "de");
        put("DENMARK", "dk");
        put("SPAIN", "es");
        put("FRANCE", "fr");
        put("GREAT BRITAIN", "gb");
        put("GEORGIA", "ge");
        put("GREECE", "gr");
        put("HONG KONG", "hk");
        put("ITALY", "it");
        put("JAPAN", "jp");
        put("LITHUANIA", "lt");
        put("MEXICO", "mx");
        put("MALAYSIA", "my");
        put("NEEHTERLANDS", "nl");
        put("POLLAND", "pl");
        put("QATAR", "qa");
        put("RUSSIA", "ru");
        put("UNITED KINGDOM", "uk");
        put("USA", "us");
        put("VIET NAM", "vn");
        put("BANGLADESH", "bd");
        put("BELGIUM", "be");
    }};

    private SQLiteDatabase db;

    public myUtils()
    {
        //we want it to load with nothing in it
    }


    public static String[] getCountryNames()
    {
        int amountNames = countryLookUp.size();
        String [] retCountrys = new String[amountNames];

        Set<String> keySet =  countryLookUp.keySet();
        int ii = 0;
        for(String currCountry: keySet)
        {
            retCountrys[ii] = currCountry;
            ii++;
        }
        return retCountrys;
    }
    //look up table for the country name which correspond to which country ID
    public static String cleanString(String inString)
    {
        //deleting all leading and lagging white spaces;
        inString = inString.trim();
        inString = inString.toUpperCase();
        return inString;
    }

    public static String getCountryCode(String country)
    {
        country = cleanString(country);
        String retString = countryLookUp.get(country);
        validateRetrival(retString, country);
        return retString;

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

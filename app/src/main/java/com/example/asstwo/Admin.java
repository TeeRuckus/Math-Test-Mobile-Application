package com.example.asstwo;
import java.io.Serializable;
public class Admin extends User implements Serializable
{

    public Admin()
    {
        super();
    }

    //TOOD: you might not even need this method, you're going to load your data from a data base anyways
    //so you can use the laod method to just load all your data into this data strcutre
    public Admin(String inName, String inUserName, String inEmail, String inCountry)
    {
        super(inName, inUserName, inEmail, inCountry);
    }

    public void addPrac(TestHistory inPrac)
    {
        String titleKey = myUtils.cleanString(inPrac.getTitle());
        practicals.put(titleKey, inPrac);
    }


    public TestHistory delPrac(String pracName)
    {
        TestHistory removedPrac = null;

        if(super.validateName(pracName))
        {
            if (practicals.isEmpty())
            {
                throw new IllegalArgumentException("Error: no practicals have being added as yet: " + practicals.size());
            }
            else
            {
                //cleaning any white spaces, and making the look up name case insensitive
                pracName = myUtils.cleanString(pracName);
                removedPrac = practicals.remove(pracName);
            }
        }

        return removedPrac;
    }

    @Override
    public String getType()
    {
        return "ADMIN";
    }

    public String toString()
    {
        return "Admin," + super.toString();
    }
}

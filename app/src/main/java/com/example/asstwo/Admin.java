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
    public Admin(String inFirstName, String inLastName)
    {
        super(inFirstName, inLastName);
    }

    @Override
    public String getType()
    {
        return "ADMIN";
    }
}

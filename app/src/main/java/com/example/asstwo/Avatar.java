package com.example.asstwo;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class Avatar implements Serializable
{
    private String name;
    private Drawable image;

    public Avatar()
    {
        name = "user profile";
        image = null;
    }

    public Avatar(String inName, Drawable inImage)
    {
        this.name = inName;
        this.image = inImage;
    }

    public String getName()
    {
        return new String(name);
    }

    public Drawable getImage()
    {
        return image;
    }

    /*
    don't really need to do validation here because the flags will always be
    selected from a drop down menu hence, the flags will always be correct
     */
    public void setName(String inName)
    {
        this.name = inName;
    }

    public void setImage(Drawable image)
    {
        this.image = image;
    }
}

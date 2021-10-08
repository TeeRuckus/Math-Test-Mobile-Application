package com.example.asstwo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class Avatar implements Serializable
{
    private String name;
    private byte[] image;

    public Avatar()
    {
        name = "user profile";
        image = null;
    }

    public Avatar(String inName, Bitmap inImage)
    {
        this.name = inName;
        this.image = convertToBytes(inImage);
    }

    public String getName()
    {
        return new String(name);
    }

    public Bitmap getImage()
    {
        return convertToImage(image);
    }

    /*
    don't really need to do validation here because the flags will always be
    selected from a drop down menu hence, the flags will always be correct
     */
    public void setName(String inName)
    {
        this.name = inName;
    }

    public void setImage(Bitmap image)
    {
        this.image = convertToBytes(image);
    }

    private Bitmap convertToImage(byte[] image)
    {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    private byte[] convertToBytes(Bitmap inImage)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return  stream.toByteArray();

    }
}

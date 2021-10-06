package com.example.asstwo;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class UserPhoto extends AppCompatActivity {

    private static final String TAG = "UserPhoto.";
    static final int REQUEST_THUMBNAIL = 1;

    private ImageButton takePictureBttn;
    private ImageButton libraryImportBttn;
    private ImageButton internetImageBttn;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_THUMBNAIL)
        {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            String path = saveToMemory(image);
            if (image != null) {
                Intent intent = new Intent(UserPhoto.this, Register.class);
                intent.putExtra("imagePath", path);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_photo);

        loadUI();

        takePictureBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_THUMBNAIL);
            }
        });

        // if they is going to be a photo saved in this activity grab it, and start the
        // registration activity again
        if(savedInstanceState != null)
        {
        }
    }

    /*
    this code is adapted from the following source: https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android
    Date accessed: 7/10/21 @ 00:50
     */
    protected String saveToMemory(Bitmap image)
    {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File dir = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File path=new File(dir,"profile.jpg");

        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(path);
            // Use the compress method on the BitMap object to write image to the OutputStream
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found in the system: " + e.getMessage());
            e.printStackTrace();
        } finally
        {
            try
            {
                fos.close();
            }
            catch (IOException e)
            {
                Log.e(TAG, "Something went wrong in reading the file: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return dir.getAbsolutePath();
    }

    protected void loadUI()
    {
        takePictureBttn = findViewById(R.id.takePictureBttn);
        libraryImportBttn = findViewById(R.id.libraryPhotoBttn);
        internetImageBttn = findViewById(R.id.internetBttn);
    }
}
package com.example.asstwo;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class UserPhoto extends AppCompatActivity {

    private final int REQUEST_THUMBNAIL = 1234;
    private final int REQUEST_SD_CARD = 5678;

    private ImageButton takePictureBttn;
    private ImageButton libraryImportBttn;
    private ImageButton internetImageBttn;
    private String name;
    private enum state {
        details,
        register
    }

    private static state currState;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //making sure that I am stagin the static variables so that no memory leaks will happen
        currState = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        String path = null;

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_THUMBNAIL)
        {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            path = saveToMemory(image);
            showText(path);

            if (image != null) {

                switch(currState)
                {
                    case register:
                        startActivityWithPicture(path);
                        break;
                    case details:
                        startDetailsWithPicture(path);
                        break;
                }
            }
        }
        else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_SD_CARD)
        {
            if (data != null) {
                Uri uri = data.getData();
                Bitmap imageSD = null;

                //displaying the path which was opened on the phone
                showText(uri.getPath());

                //getting the image which was taken sending it to the activity which started it, and
                //displaying it as the avatar for the current character
                try
                {
                    imageSD = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                //if we actually succesfully get an image we should go back to the previous activity
                //otherwise, this should re-prompt the user to select another image
                if (imageSD != null)
                {
                    path = saveToMemory(imageSD);

                    switch (currState)
                    {
                        case register:
                            startActivityWithPicture(path);
                            break;

                        case details:
                            startDetailsWithPicture(path);
                            break;
                    }
                }
            }
        }
    }

    protected void startActivityWithPicture(String path)
    {
        Intent intent = new Intent(UserPhoto.this, Register.class);
        intent.putExtra("imagePath", path);
        startActivity(intent);
    }

    protected void startDetailsWithPicture(String path)
    {
        Intent intent = new Intent(UserPhoto.this, Details.class);
        intent.putExtra("iamgePath", path);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    protected void showText(CharSequence text)
    {
        Context cntx = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(cntx, text, duration);
        toast.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_photo);
        loadUI();

        name = getIntent().getStringExtra("name");

        takePictureBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        libraryImportBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhotoSD();
            }
        });

        internetImageBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                switch (currState)
                {
                    case register:
                        intent = new Intent(UserPhoto.this, InternetPhotos.class);
                        InternetPhotos.register();
                        startActivity(intent);
                        break;

                    case details:
                        intent = new Intent(UserPhoto.this, InternetPhotos.class);
                        InternetPhotos.details();
                        intent.putExtra("name", name);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    public static void details()
    {
        currState = state.details;
    }

    public static void register()
    {
        currState = state.register;
    }

    protected void takePicture()
    {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_THUMBNAIL);
    }

    protected void choosePhotoSD()
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_SD_CARD);
    }

    /*
    this code is adapted from the following source: https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android
    Date accessed: 7/10/21 @ 00:50
     */
    private String saveToMemory(Bitmap image)
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
            e.printStackTrace();
        } finally
        {
            try
            {
                fos.close();
            }
            catch (IOException e)
            {
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
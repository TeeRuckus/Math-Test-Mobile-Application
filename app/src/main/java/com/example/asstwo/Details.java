package com.example.asstwo;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Details extends AppCompatActivity {

    private static final String TAG = "Details.";
    private Graph mathTestGraph;
    private static String name;

    private ImageButton studentPicture;
    private EditText firstNameBox;
    private EditText lastNameBox;
    private TextView emailNumberBanner;
    private Button emailToggleBttn;
    private Button updateBttn;
    private Button testBttn;

    private TextView studentFirstnameError;
    private TextView studentLastNameError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mathTestGraph = new Graph();
        mathTestGraph = mathTestGraph.load(Details.this);
        name = getIntent().getStringExtra("name");
        //making it static so I can access it inside on click listeners

        if (name != null)
        {
            //Toast.makeText(Details.this, name, Toast.LENGTH_LONG).show();
            Graph.Vertex currUser = mathTestGraph.getVertex(name);
            loadUI();

            //getting the current details of the user, and displaying them on the screen
            //TODO: set this as the background
            studentPicture.setImageBitmap(currUser.getValue().getAvatar().getImage());
            firstNameBox.setHint(currUser.getValue().getFirstName());
            lastNameBox.setHint(currUser.getValue().getLastName());


            FragmentManager fm = getSupportFragmentManager();
            ItemViewRecycler frag = (ItemViewRecycler) fm.findFragmentById(R.id.framePhoneNumbers);

            //if they is nothing attached to the fragment attach something to it now
            if (frag == null)
            {
                //originally load the view in the phone number view
                ItemViewRecycler.numbersViewing();
                frag = new ItemViewRecycler();
                fm.beginTransaction()
                        .add(R.id.framePhoneNumbers, frag)
                        .commit();
            }

            emailToggleBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String currentButton = emailToggleBttn.getText().toString();
                    currentButton = myUtils.cleanString(currentButton);
                    FragmentManager fm = getSupportFragmentManager();
                    ItemViewRecycler fragNew = (ItemViewRecycler)  fm.findFragmentById(R.id.framePhoneNumbers);

                    Log.e(TAG, "The current text of the button: " + currentButton);
                    if (currentButton.equals("EMAIL"))
                    {
                        emailNumberBanner.setText("Email Addresses");
                        emailToggleBttn.setText("Numbers");
                        fragNew = new ItemViewRecycler();
                        ItemViewRecycler.addressesViewing();
                        fm.beginTransaction()
                                .add(R.id.framePhoneNumbers, fragNew)
                                .commit();


                    }
                    else
                    {
                        emailNumberBanner.setText("Phone Numbers");
                        emailToggleBttn.setText("Email");
                        fragNew = new ItemViewRecycler();
                        ItemViewRecycler.numbersViewing();
                        fm.beginTransaction()
                                .add(R.id.framePhoneNumbers, fragNew)
                                .commit();
                    }
                }
            });

        }
        else
        {
            Toast.makeText(Details.this, "Name not received", Toast.LENGTH_LONG).show();
        }
    }

    public static String getName()
    {
        return name;
    }

    private void loadUI()
    {
        studentPicture = findViewById(R.id.userImageDetails);
        firstNameBox = findViewById(R.id.firstNameDetails);
        studentFirstnameError = findViewById(R.id.firstNameDetailsError);
        lastNameBox = findViewById(R.id.lastNameDetails);
        studentLastNameError = findViewById(R.id.lastNameDetailsError);
        emailNumberBanner = findViewById(R.id.numbersHeader);
        emailToggleBttn = findViewById(R.id.togglEmailPhoneBttn);
        updateBttn = findViewById(R.id.updateDetails);
        testBttn = findViewById(R.id.takeTestDetails);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mathTestGraph.save(Details.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mathTestGraph.save(Details.this);
    }
}
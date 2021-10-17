package com.example.asstwo;

import android.content.ClipData;
import android.content.Intent;
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

import java.util.ArrayList;

public class Details extends AppCompatActivity implements ItemViewRecycler.onClickRowListener {

    private Graph mathTestGraph;
    private static String name;
    private static final String TAG = "Details";

    private ImageButton studentPicture;
    private EditText firstNameBox;
    private EditText lastNameBox;
    private TextView emailNumberBanner;
    private Button emailToggleBttn;
    private Button updateBttn;
    private Button testBttn;
    private Button viewTestHistory;
    private Button addMore;
    private Button addMoreTablet;
    private TextView addMoreError;
    private EditText addMoreTextBox;

    private String imagePath;
    private Bitmap currUserImage;

    private TextView studentFirstnameError;
    private TextView studentLastNameError;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(Details.this, StudentViewing.class);
        StudentViewing.students();
        StudentViewing.HomePage();
        startActivity(intent);
    }

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
            User student = currUser.getValue();

            loadUI();

            //getting the current details of the user, and displaying them on the screen
            //TODO: set this as the background
            studentPicture.setImageBitmap(currUser.getValue().getAvatar().getImage());
            firstNameBox.setHint(currUser.getValue().getFirstName());
            lastNameBox.setHint(currUser.getValue().getLastName());
            studentPicture.buildDrawingCache();
            currUserImage = studentPicture.getDrawingCache();



            FragmentManager fm = getSupportFragmentManager();
            ItemViewRecycler frag = (ItemViewRecycler) fm.findFragmentById(R.id.framePhoneNumbers);

            //if they is nothing attached to the fragment attach something to it now
            if (frag == null)
            {
                //originally load the view in the phone number view

                frag = new ItemViewRecycler();
                //ItemViewRecycler.numbersViewing();
                ItemViewRecycler.numbersViewing();
                fm.beginTransaction()
                        .add(R.id.framePhoneNumbers, frag)
                        .commit();
            }

            //only attach the emails on the same screen if it's going to be a tablet
            boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
            if(tabletSize)
            {
                FragmentManager fmTab = getSupportFragmentManager();
                ItemViewRecyclerTablet fragTab = (ItemViewRecyclerTablet) fmTab.findFragmentById(R.id.frameEmails);

                if (fragTab == null)
                {
                    emailToggleBttn.setVisibility(View.GONE);
                    fragTab = new ItemViewRecyclerTablet();
                    ItemViewRecyclerTablet.addressesViewing();
                    //ItemViewRecycler.addressesViewing();
                    fmTab.beginTransaction()
                            .add(R.id.frameEmails, fragTab)
                            .commit();
                }
            }

            emailToggleBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String currentButton = emailToggleBttn.getText().toString();
                    currentButton = myUtils.cleanString(currentButton);
                    FragmentManager fm = getSupportFragmentManager();
                    ItemViewRecycler fragNew = (ItemViewRecycler)  fm.findFragmentById(R.id.framePhoneNumbers);

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

            addMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String currentButton = emailToggleBttn.getText().toString();
                    currentButton = myUtils.cleanString(currentButton);
                    User currStudent = mathTestGraph.getVertex(name).getValue();


                    if (currentButton.equals("EMAIL"))
                    {
                        try
                        {

                            currStudent.addPhoneNum(addMoreTextBox.getText().toString());
                            //cleaing the text if they was an error message which was displayed before hand
                            addMoreError.setText("");
                            Toast.makeText(Details.this, "Number added", Toast.LENGTH_LONG).show();
                            addMoreTextBox.setText("");
                            mathTestGraph.save(Details.this);
                        }
                        catch (IllegalArgumentException e)
                        {
                            addMoreError.setText("Inavalid Phone Number");
                        }
                        catch (IndexOutOfBoundsException e)
                        {
                            addMoreError.setText("can only have a maximum of 10 phone numbers");
                        }

                    }
                    else
                    {
                        try
                        {
                            currStudent.addEmail(addMoreTextBox.getText().toString());
                            //clearing the text if they was an error message displayed before hand
                            addMoreError.setText("");
                            Toast.makeText(Details.this, "Email added", Toast.LENGTH_LONG).show();
                            //we will need to update the data inside of the recycler view of the progrogramme
                            addMoreTextBox.setText("");
                            mathTestGraph.save(Details.this);
                        }
                        catch (IllegalArgumentException e)
                        {
                            addMoreError.setText("Invalid Email address");
                        }
                        catch (IndexOutOfBoundsException e)
                        {
                            addMoreError.setText("can only have a maximum of 10 email addresses");
                        }
                    }
                }
            });

            updateBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String currEnteredFirstName = firstNameBox.getText().toString();
                    String currEnteredLastName = lastNameBox.getText().toString();
                    Graph.Vertex currVert = mathTestGraph.getVertex(name);
                    User currUser = currVert.getValue();
                    studentPicture.buildDrawingCache();
                    currUserImage = studentPicture.getDrawingCache();
                    currUser.getAvatar().setImage(currUserImage);

                    try {
                        if (!(currEnteredFirstName.equals("")) && !(currEnteredLastName.equals(""))) {
                            currUser.setFirstName(currEnteredFirstName);
                            currUser.setLastName(currEnteredLastName);
                            mathTestGraph.delVertex(name);
                            //in this application the user is almost going to be always a student
                            mathTestGraph.addVertex((Student) currUser);
                            name = currUser.getName();
                            firstNameBox.setText("");
                            lastNameBox.setText("");
                            firstNameBox.setHint(currEnteredFirstName);
                            lastNameBox.setHint(currEnteredLastName);
                            Toast.makeText(Details.this, "Student Updated", Toast.LENGTH_SHORT).show();
                            mathTestGraph.save(Details.this);
                        } else if (!(currEnteredFirstName.equals(""))) {
                            //getting what they old last name would have being in the programme
                            String oldLastName = currUser.getLastName();
                            currUser.setFirstName(currEnteredFirstName);
                            currUser.setLastName(oldLastName);

                            mathTestGraph.delVertex(name);
                            mathTestGraph.addVertex((Student) currUser);
                            name = currUser.getName();
                            firstNameBox.setText("");
                            lastNameBox.setText("");
                            firstNameBox.setHint(currEnteredFirstName);
                            lastNameBox.setHint(oldLastName);
                            Toast.makeText(Details.this, "Student Updated", Toast.LENGTH_SHORT).show();
                            mathTestGraph.save(Details.this);
                        } else if (!(currEnteredLastName.equals(""))) {
                            String oldFirstName = currUser.getFirstName();
                            currUser.setFirstName(oldFirstName);
                            currUser.setLastName(currEnteredLastName);
                            mathTestGraph.delVertex(name);
                            mathTestGraph.addVertex((Student) currUser);
                            name = currUser.getName();
                            firstNameBox.setText("");
                            lastNameBox.setText("");
                            firstNameBox.setHint(oldFirstName);
                            lastNameBox.setHint(currEnteredLastName);
                            Toast.makeText(Details.this, "Student Updated", Toast.LENGTH_SHORT).show();
                            mathTestGraph.save(Details.this);
                        }
                    }
                    catch (IllegalArgumentException e)
                    {
                        studentFirstnameError.setText("User already exists");
                        studentLastNameError.setText("User already exists");
                    }
                }
            });

            studentPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Details.this, UserPhoto.class);
                    UserPhoto.details();
                    intent.putExtra("name", name);
                    startActivity(intent);
                }
            });

            testBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Details.this, TakeTest.class);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }
            });

            viewTestHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    User currUser = mathTestGraph.getVertex(name).getValue();
                    ArrayList<TestHistory> currHistory = currUser.getHistory();

                    if(currHistory.size() > 0)
                    {
                        Intent intent = new Intent(Details.this, StudentViewing.class);
                        StudentViewing.test();
                        StudentViewing.DetailsPage();
                        intent.putExtra("name", name);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(Details.this, "User hasn't done any tests", Toast.LENGTH_LONG).show();
                    }

                }
            });

            imagePath = getIntent().getStringExtra("imagePath");

            if (imagePath != null)
            {
                //put the  image on the image Button
                currUserImage = myUtils.getImageStorage(imagePath);
                studentPicture.setImageBitmap(currUserImage);
                Drawable dImage = new BitmapDrawable(getResources(), currUserImage);
                studentPicture.setBackground(dImage);
            }
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
        viewTestHistory = findViewById(R.id.viewHistoryDetails);
        addMore = findViewById(R.id.addMoreDetails);
        addMoreError = findViewById(R.id.addMoreDetailsError);
        addMoreTextBox = findViewById(R.id.addMoreEmailPhone);

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize)
        {

            addMoreTablet = findViewById(R.id.addMoreDetailsEmail);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mathTestGraph.save(Details.this);
        //ensuring static variables are going to be staged for garbage collection with the programme
        name = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mathTestGraph.save(Details.this);
    }

    @Override
    public void onListSelected(CharSequence currTitle) {
        // do nothing for the current being
    }
}
package com.example.asstwo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ContentResolver.*;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Register extends AppCompatActivity {


    private static final String TAG = "Register.";
    private static final int REQUEST_READ_CONTACT_PERMISSION = 4;


    private ImageButton studentPicture;
    private ImageButton importFromContactsBttn;
    private EditText studentFirstName;
    private TextView studentFirstNameError;
    private EditText studentLastName;
    private TextView studentLastNameError;
    private Spinner phNumSpinner;
    private EditText phNumInput;
    private TextView phoneNumError;
    private Button addMorePhone;
    private EditText emailInput;
    private TextView emailError;
    private Button addMoreEmail;
    private Spinner emailSpinner;
    private Button registerBttn;
    private Graph mathTestGraph;
    private Student currStdnt;
    private int contactId;
    private String imagePath;
    private Bitmap currUserImage;

    //class fields to hold the information which wsa in the register form just in case the user
    //switches activities
    private static String firstNameSave;
    private static String lastNameSave;
    private static String phNumSave;
    private static String emailSave;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //when the back press button is clicked in this activity we should go back to the main activity
        Intent intent = new Intent(Register.this, MainActivity.class);
        //setting them up for deleting with java's garbage collection
        firstNameSave = null;
        lastNameSave = null;
        phNumSave = null;
        emailSave = null;
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loadGraph();
        loadUIElements();

        if (firstNameSave != null)
        {
            studentFirstName.setText(firstNameSave);
        }

        if(lastNameSave != null)
        {
            studentLastName.setText(lastNameSave);
        }

        if(phNumSave != null)
        {
            phNumInput.setText(phNumSave);
        }

        if(emailSave != null)
        {
            //TODO: you will need to find out how you can make all the email addressed which they
            //have inputted into the app actually persistent
            emailInput.setText(emailSave);
        }

        imagePath = getIntent().getStringExtra("imagePath");

        if (imagePath != null)
        {
            Log.e(TAG, "the path I received: " + imagePath);
            //put the  image on the image Button
            currUserImage = myUtils.getImageStorage(imagePath);
            studentPicture.setImageBitmap(currUserImage);
            //Drawable dImage = new BitmapDrawable(getResources(), currUserImage);
            //studentPicture.setBackground(dImage);
        }

        Log.i(TAG, "Current Graph Object: " + mathTestGraph);
        currStdnt = new Student();
        //importCheck = 0;


        //adding text watchers to the first name and the last name, so callback checks can be done to
        //see if contact is going to exist in the current data base or not
        studentFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //don't really care about this one or need it but we're required to have this any ways
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //don't really care about this one as well but we're required to have it any ways

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e(TAG, "HEY YOU HAVE CHANGED THE FIRST NAME: ");
                //searchContactList();
                firstNameSave = studentFirstName.getText().toString();
            }
        });

        studentLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // don't really care about this one as well but it's required
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //I don't really care about this one as well but it's required
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e(TAG, "HEY YOU HAVE CHANGED THE LAST NAME: ");
                //searchContactList();
                lastNameSave = studentLastName.getText().toString();
            }
        });

        phNumInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                phNumSave = phNumInput.getText().toString();

            }
        });

        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                emailSave =  emailInput.getText().toString();
            }
        });


        registerBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getting all the elements from the UI, and adding them into the current graph which we have created
                boolean valid = registerUser(currStdnt);

                if (valid)
                {
                    //once a new student has being created resetting everything else
                    try
                    {
                        //TODO: you will need ask sajib on how you're going to go on with this
                        studentPicture.buildDrawingCache();
                        Bitmap image = studentPicture.getDrawingCache();
                        Avatar tempAvatar = new Avatar("profile picture", image);
                        currStdnt.setAvatar(tempAvatar);
                        mathTestGraph.addVertex(currStdnt);
                        //making sure whenever a user is saved we're going to save it to the graph
                        mathTestGraph.save(Register.this);

                        Context cntx = getApplicationContext();
                        CharSequence text = "student created";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(cntx, text, duration);
                        toast.show();
                        clearText();
                        currStdnt = new Student();
                    }
                    catch(IllegalArgumentException e)
                    {
                        Log.e(TAG, e.getMessage());
                        // the user already exists in the system
                        studentFirstNameError.setText("User already Exists");
                        studentLastNameError.setText("User already Exists");
                    }
                }
            }
        });

        addMoreEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    currStdnt.addEmail(emailInput.getText().toString());
                    emailInput.setText("");
                    //if an error had occur set the error dialogue to blank after correct input
                    emailError.setText("");
                }
                catch(IllegalArgumentException err)
                {
                    Log.e(TAG, err.getMessage());
                    emailError.setText("Invalid email format");
                }
                catch(IndexOutOfBoundsException err)
                {
                    Log.e(TAG, err.getMessage());
                    emailError.setText("maxium of 10 emails reached");
                }

            }
        });

        addMorePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                try
                {
                    currStdnt.addPhoneNum(phNumInput.getText().toString());
                    phNumInput.setText("");
                    //if an error had occurred set the error dialogue to blank after correct input
                    phoneNumError.setText("");
                }
                catch (IllegalArgumentException err)
                {
                    Log.e(TAG, err.getMessage());
                    phoneNumError.setText("Invalid Number format");
                }

                catch (IndexOutOfBoundsException err)
                {
                    Log.e(TAG, "Maximum of 10 numbers reached");
                    phoneNumError.setText("maximum of 10 numbers reached");
                }
            }
        });

        //TODO: you might need to refactor this so you can actually choose a contact from the contact list
        importFromContactsBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context cntx = getApplicationContext();
                CharSequence text = "Importing Contact";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(cntx, text, duration);
                toast.show();
                importPhoneNumber();
                importEmailAddress();
            }
        });


        studentPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, UserPhoto.class);
                UserPhoto.register();
                startActivity(intent);
            }
        });


    }

    /*
    code was adapted from the following source: https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android
    Date accessed : 7/10/21 @ 1:00
     */

    @SuppressLint("Range")
    public void searchContactList()
    {
        String firstName = studentFirstName.getText().toString();
        String lastName = studentLastName.getText().toString();
        firstName = myUtils.cleanString(firstName);
        lastName = myUtils.cleanString(lastName);

        /*
        Code: was adapted from the following stack over flow post:
        https://stackoverflow.com/questions/4301064/how-to-get-the-first-name-and-last-name-from-android-contacts
        DATA ACCESSED: 5/10/21 @ 00:41
         */

        //need to give android studio permissions to search the contacts list
        if(ContextCompat.checkSelfPermission(Register.this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(Register.this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACT_PERMISSION);
        }

        String whereClause = ContactsContract.Data.MIMETYPE + " = ?";
        String[] whereValues = new String[] { ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE };
        Cursor c = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, whereClause, whereValues, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);

        try
        {
            c.moveToFirst();
            do
            {
                String given = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                String family = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
                contactId = c.getInt(c.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID));

                //make the search case insensintive
                given = myUtils.cleanString(given);
                family = myUtils.cleanString(family);
                if(given.equals(firstName) && family.equals(lastName))
                {
                    importFromContactsBttn.setVisibility(View.VISIBLE);
                    importFromContactsBttn.setClickable(true);
                    Log.e(TAG, "found contact ID: " + contactId);
                }

                //@SuppressLint("Range") String display = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));

                Log.i(TAG, "GIVEN NAME: " + given);
                Log.i(TAG, "FAMILY NAME: " + family);
            }while(c.moveToNext());
        }
        finally
        {
            c.close();
        }

    }

    public void importPhoneNumber()
    {
        String result = "";
        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] querryFieldsPhone = new String[] {
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        String whereClausePhone = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";
        String [] whereValuesPhone  = new String[]{
                String.valueOf(this.contactId)
        };
        Cursor c = getContentResolver().query(
                phoneUri, querryFieldsPhone, whereClausePhone, whereValuesPhone, null);

        try
        {
            c.moveToFirst();
            do
            {
                String phoneNum = c.getString(0);
                result = result + phoneNum + " ";
            }
            while (c.moveToNext());
        }
        finally
        {
            //we always need to make sure that we close our cursor no matter what
            c.close();
        }

        phNumInput.setText(result);
    }
    public void importEmailAddress()
    {
        String result = "";
        Uri emailUri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String[] querryFields = new String[] {
                ContactsContract.CommonDataKinds.Email.ADDRESS
        };

        String whereClause = ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?";
        String [] whereValues  = new String[]{
                String.valueOf(this.contactId)
        };

        Cursor c = getContentResolver().query(
                emailUri, querryFields, whereClause, whereValues, null);
        try
        {
            c.moveToFirst();
            do
            {
                String emailAddress = c.getString(0);
                Log.e(TAG, "current email address: "+emailAddress);
                result = result + emailAddress + " ";
            }
            while (c.moveToNext());
        }
        finally
        {
            //we always need to make sure that we close our cursor no matter what
            c.close();
        }

        emailInput.setText(result);
    }

    public boolean registerUser(User inUser)
    {
        boolean valid = false;

        int checks = 0;

        try
        {
            inUser.setFirstName(studentFirstName.getText().toString());
            checks++;
            //if an erorr has occurred set the error dialogue to blank after correct input
            studentFirstNameError.setText("");
        }
        catch (IllegalArgumentException err)
        {
            Log.e(TAG, err.getMessage());
            studentFirstNameError.setText("Student first name can't be blank");
        }

        try
        {
            inUser.setLastName(studentLastName.getText().toString());
            checks++;
            //if an error has occurred set the error dialogue to blank after correct input
            studentLastNameError.setText("");
        }
        catch (IllegalArgumentException err)
        {
            Log.e(TAG, err.getMessage());
            studentLastNameError.setText("Student Last name can't be blank");
        }

        try
        {
            //TODO: you will need to add code to check how many emails they have
            inUser.addPhoneNum(phNumInput.getText().toString());
            // if an error has occured set the error dialogue to balnk after correct input
            phoneNumError.setText("");
            checks++;

        }
        catch (IllegalArgumentException err)
        {
            Log.e(TAG, err.getMessage());

            //checking if they have more than one phone number available because everything will be fine
            if(inUser.checkPhoneNumbers())
            {
                checks++;
            }
            else
            {
                phoneNumError.setText("Invalid Phone Number");
            }
        }
        catch (IndexOutOfBoundsException err)
        {
            Log.e(TAG, err.getMessage());
            //do nothing as they have reached the maximum amount of numbers
        }


        try
        {
            //TODO: you will need to add code to check how many emails they have
            inUser.addEmail(emailInput.getText().toString());
            checks++;
            //if an error has occurred set the error dialogue to blank after correct input
            emailError.setText("");
        }
        catch (IllegalArgumentException err)
        {
            Log.e(TAG, err.getMessage());
            if(inUser.checkEmailAddresses())
            {
                checks++;
            }
            else
            {
                emailError.setText("Invalid email format");
            }
        }
        catch(IndexOutOfBoundsException err)
        {
            Log.e(TAG, err.getMessage());
            //do nothing as they have reached the maximum amount of emails
        }

        if(checks == 4)
        {
            valid = true;
        }

        return valid;
    }
    public void loadGraph()
    {
        Log.i(TAG, "YOU HAVE CREATED ME YOUR LORD");
        mathTestGraph = new Graph();
        mathTestGraph = mathTestGraph.load(Register.this);
    }


    public void loadUIElements()
    {
        studentPicture = findViewById(R.id.studentPictureView);
        importFromContactsBttn = findViewById(R.id.importIcon);
        studentFirstName = findViewById(R.id.studentFristName);
        studentFirstNameError = findViewById(R.id.studentFirstNameError);
        studentLastName = findViewById(R.id.studentLastName);
        studentLastNameError = findViewById(R.id.studentLastNameError);
        phNumSpinner = findViewById(R.id.phoneNumberSpinner);
        phNumInput = findViewById(R.id.phoneNumInput);
        addMorePhone = findViewById(R.id.addMoreNumber);
        emailInput = findViewById(R.id.emailInput);
        emailError = findViewById(R.id.emailError);
        emailSpinner = findViewById(R.id.emailSpinner);
        addMoreEmail = findViewById(R.id.addMoreEmailBttn);
        registerBttn = findViewById(R.id.registerStudentBttn);
        phoneNumError = findViewById(R.id.phoneNumError);

        //the import icon should only show up when we're imporing a contact into teh programme
        importFromContactsBttn.setVisibility(View.INVISIBLE);
        importFromContactsBttn.setClickable(false);
    }

    public void clearText()
    {
        studentFirstName.setText("");
        studentLastName.setText("");
        phNumInput.setText("");
        emailInput.setText("");
        importFromContactsBttn.setVisibility(View.INVISIBLE);
        studentPicture.setImageBitmap(null);
        studentPicture.setBackground(getResources().getDrawable(R.drawable.ic_launcher_background));
        studentPicture.setForeground(getResources().getDrawable(R.drawable.ic_launcher_foreground));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "YOU HAVE DESTROYED ME");
        mathTestGraph.save(Register.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "YOU HAVE STOPPED ME");
        mathTestGraph.save(Register.this);
    }
}
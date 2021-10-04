package com.example.asstwo;

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

import org.w3c.dom.Text;

import java.io.IOException;

public class Register extends AppCompatActivity {


    private static final String TAG = "Register.";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loadGraph();

        loadUIElements();
        Log.i(TAG, "Current Graph Object: " + mathTestGraph);
        currStdnt = new Student();


        //adding text watchers to the first name and the last name, so callback checks can be done to
        //see if contact is going to exist in the current data base or not
        studentFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //don't really care about this one or need it
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //don't really care about this one as well

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.i(TAG, "HEY YOU HAVE CHANGED THE FIRST NAME");
            }
        });

        studentLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // don't really care about this one as well
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //I don't really care about this one as well
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.i(TAG, "HEY YOU HAVE CHANGED THE LAST NAME");
            }
        });

        registerBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getting all the elements from the UI, and adding them into the current graph which we have created
                boolean valid = registerUser(currStdnt);

                if (valid)
                {
                    Log.e(TAG, "Register user and add another user to the network");

                    //once a new student has being created resetting everything else
                    currStdnt = new Student();
                }
            }
        });

        addMoreEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    currStdnt.addEmail(emailInput.getText().toString());
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
            inUser.addPhoneNum(phNumInput.getText().toString());
            // if an error has occured set the error dialogue to balnk after correct input
            phoneNumError.setText("");
            checks++;

        }
        catch (IllegalArgumentException err)
        {
            Log.e(TAG, err.getMessage());
            phoneNumError.setText("Invalid Phone Number");
        }


        try
        {
            inUser.addEmail(emailInput.getText().toString());
            checks++;
            //if an error has occurred set the error dialogue to blank after correct input
            emailError.setText("");
        }
        catch (IllegalArgumentException err)
        {
            Log.e(TAG, err.getMessage());
            emailError.setText("Invalid email format");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "YOU HAVE DESTROYED ME");
        //TODO: you will need to uncomment this, just doing this so I can play with without messing me up
        //mathTestGraph.save(Register.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "YOU HAVE STOPPED ME");
        //TODO: you will need to uncomment this out, I am just doing this so I can play wiht it
        //mathTestGraph.save(Register.this);
    }
}
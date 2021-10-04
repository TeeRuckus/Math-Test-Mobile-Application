package com.example.asstwo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;

public class Register extends AppCompatActivity {


    private static final String TAG = "Register.";
    private ImageView studentPicture;
    private ImageButton importFromContactsBttn;
    private EditText studentFirstName;
    private TextView studentFirstNameError;
    private EditText studentLastName;
    private TextView studentLastNameError;
    private Spinner phNumSpinner;
    private EditText phNumInput;
    private Button addMorePhone;
    private EditText emailInput;
    private TextView emailError;
    private Spinner emailSpinner;
    private Button registerBttn;
    private Graph mathTestGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loadGraph();

        loadUIElements();
        Log.i(TAG, "Current Graph Object: " + mathTestGraph);
    }

    public void loadGraph()
    {
        Log.i(TAG, "YOU HAVE CREATED ME YOUR LORD");
        mathTestGraph = new Graph();
        try
        {
            mathTestGraph = mathTestGraph.load(Register.this);
        }
        catch(NullPointerException err)
        {
            Log.e(TAG, err.getMessage());
        }
        catch (IOException e)
        {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        Log.i(TAG, "Current Graph Object: " + mathTestGraph);
    }

    public void loadUIElements()
    {
        studentPicture = findViewById(R.id.studentPictureView);
        importFromContactsBttn = findViewById(R.id.importIcon);
        studentFirstName = findViewById(R.id.studentFristName);
        studentFirstNameError = findViewById(R.id.studentFirstNameError);
        studentLastName = findViewById(R.id.studentLastName);
        studentLastNameError = findViewById(R.id.studentFirstNameError);
        phNumSpinner = findViewById(R.id.phoneNumberSpinner);
        phNumInput = findViewById(R.id.phoneNumInput);
        addMorePhone = findViewById(R.id.addMoreNumber);
        emailInput = findViewById(R.id.emailInput);
        emailError = findViewById(R.id.emailError);
        emailSpinner = findViewById(R.id.emailSpinner);
        registerBttn = findViewById(R.id.registerBttn);

        //the import icon should only show up when we're imporing a contact into teh programme
        importFromContactsBttn.setVisibility(View.INVISIBLE);
        importFromContactsBttn.setClickable(false);

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
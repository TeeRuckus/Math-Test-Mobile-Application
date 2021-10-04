package com.example.asstwo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button viewStdntBttn;
    private Button registerStdntBttn;
    private Graph mathTestGraph;
    private static final String TAG = "MainActivity.";


    @Override
    public void onBackPressed()
    {
        //Log.i(TAG, "THEY IS NOTHING TO DO MATE");
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "SAVING YOUR DATA BEFORE YOU COMPLETELY DESTROY ME....");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "YOU HAVE PAUSE ME YOUR LORD");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "YOU HAVE STOPPED ME YOUR LORD");
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Log.i(TAG, "YOU HAVE STARTED ME YOUR LORD");
        loadGraph();
    }

    public void loadGraph()
    {
        mathTestGraph = new Graph();
        try
        {
            mathTestGraph.load();
        }
        catch(NullPointerException err)
        {
            Log.i(TAG, "Creating a brand new graph...");
        }
        Log.i(TAG, "Current Graph Object: " + mathTestGraph);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadUIElements();

        viewStdntBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "yet to be implemented");
            }
        });

        registerStdntBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRequiredActivity(Register.class);
            }
        });

    }

    protected void loadUIElements()
    {
        viewStdntBttn = findViewById(R.id.studentListBttn);
        registerStdntBttn = findViewById(R.id.registerStudentView);
    }

    protected void startRequiredActivity(Class toActivity)
    {
        Intent intent= new Intent(MainActivity.this, toActivity);
        startActivity(intent);
    }

}
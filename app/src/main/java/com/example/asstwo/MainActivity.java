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
        //they is nothing beyond the back screen so we should do nothing
        //super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "YOU HAVE DESTROYED ME MATE AND I HATE YOU");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "YOU HAVE STOPPED ME YOUR LORD");
        mathTestGraph.save(MainActivity.this);
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
            mathTestGraph.load(MainActivity.this);
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
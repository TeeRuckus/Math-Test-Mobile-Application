package com.example.asstwo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button viewStdntBttn;
    private Button registerStdntBttn;
    private Graph mathTestGraph;
    private static final String TAG = "MainActivity.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadGraph();

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
        mathTestGraph.save(MainActivity.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "YOU HAVE STOPPED ME YOUR LORD");
        mathTestGraph.save(MainActivity.this);
    }

    public void loadGraph()
    {
        mathTestGraph = new Graph();
        mathTestGraph = mathTestGraph.load(MainActivity.this);

        try
        {
            mathTestGraph.addVertex(new Admin("Current", "Admin"));
            mathTestGraph.setAdmin("currentAdmin");
            Log.i(TAG, "Created new Graph");
        }
        catch (IllegalArgumentException err)
        {
            Log.i(TAG, "Admin already exist in the programme");
        }
    }

    protected void loadUIElements()
    {
        viewStdntBttn = findViewById(R.id.studentListBttn);
        registerStdntBttn = findViewById(R.id.registerStudentView);
    }

    protected void startRequiredActivity(Class toActivity)
    {
        mathTestGraph.save(MainActivity.this);
        Intent intent= new Intent(MainActivity.this, toActivity);
        //we should save the graph before we start the next activity
        startActivity(intent);
    }

}
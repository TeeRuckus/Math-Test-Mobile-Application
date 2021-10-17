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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadGraph();
        loadUIElements();

        Register.clear();

        viewStdntBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, StudentViewing.class);
                StudentViewing.HomePage();
                StudentViewing.students();
                startActivity(intent);
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
        mathTestGraph.save(MainActivity.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mathTestGraph.save(MainActivity.this);
    }

    public void loadGraph()
    {
        mathTestGraph = new Graph();
        mathTestGraph = mathTestGraph.load(MainActivity.this);

        try
        {
            mathTestGraph.addVertex(new Admin("Current", "Admin"));
            mathTestGraph.setAdmin("Current Admin");
        }
        catch (IllegalArgumentException err)
        {
            err.printStackTrace();
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
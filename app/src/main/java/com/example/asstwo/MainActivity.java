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
    private static final String TAG = "MainActivity.";

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
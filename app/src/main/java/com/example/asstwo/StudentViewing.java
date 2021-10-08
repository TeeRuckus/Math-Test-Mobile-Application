package com.example.asstwo;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StudentViewing extends AppCompatActivity {

    private Graph mathTestGraph;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(StudentViewing.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_viewing);

        //loading the graph when this is created
        mathTestGraph = new Graph();
        mathTestGraph = mathTestGraph.load(StudentViewing.this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mathTestGraph.save(StudentViewing.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mathTestGraph.save(StudentViewing.this);
    }
}
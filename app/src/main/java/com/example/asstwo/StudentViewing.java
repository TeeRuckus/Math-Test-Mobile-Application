package com.example.asstwo;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class StudentViewing extends AppCompatActivity {


    private final String TAG = "StudentViewing";
    private TextView banner;
    private Button emailBttn;
    private Graph mathTestGraph;

    private enum state {
        students,
        test,
        results
    }

    private static state currState;

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
        banner = findViewById(R.id.bannerStudentViewing);
        emailBttn = findViewById(R.id.sendEmailBttn);

        //loading the graph when this is created
        mathTestGraph = new Graph();
        mathTestGraph = mathTestGraph.load(StudentViewing.this);

        FragmentManager fm = getSupportFragmentManager();
        ItemViewRecycler frag = (ItemViewRecycler) fm.findFragmentById(R.id.viewingContainer);

        switch(currState)
        {
            case students:
                if (frag == null)
                {
                    //making sure that the users are going to be displayed on the screen and nothing else
                    ItemViewRecycler.usersViewing();
                    frag = new ItemViewRecycler();
                    fm.beginTransaction()
                            .add(R.id.viewingContainer, frag)
                            .commit();

                    emailBttn.setVisibility(View.GONE);
                }

                break;

            case test:
                banner.setText("Tests");
                if (frag == null)
                {
                    Log.e(TAG, "I have entered where the tests are going to be for the programme");
                    //making sure that the users are going to be displayed on the screen and nothing else
                    ItemViewRecycler.testViewing();
                    frag = new ItemViewRecycler();
                    fm.beginTransaction()
                            .add(R.id.viewingContainer, frag)
                            .commit();

                }
                break;

            case results:
                String testTitle = getIntent().getStringExtra("test");
                String name = getIntent().getStringExtra("name");
                banner.setText("Results: " + name.split(" ")[0]);
                Log.e(TAG, "The test result to be viewed: " + name);

                User currUser = mathTestGraph.getVertex(name).getValue();
                ArrayList<TestHistory> currUsersTest = currUser.getHistory();
                Log.e(TAG, "The current users test: " + currUsersTest);
                int index = currUser.indexOfTitle(testTitle);
                Log.e(TAG, "the found index: " + index);

                if (testTitle != null)
                {

                }
                else
                {
                    Toast.makeText(StudentViewing.this, "Test not found", Toast.LENGTH_LONG).show();
                }

                //open up the results page of the programme
                break;
        }

    }

    public static void students()
    {
        currState = state.students;
    }


    public static void test()
    {
        currState = state.test;
    }

    public static void results()
    {
        currState = state.results;
    }
}
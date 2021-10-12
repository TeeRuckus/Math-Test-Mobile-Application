package com.example.asstwo;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class StudentViewing extends AppCompatActivity {


    private final String TAG = "StudentViewing";
    private TextView banner;

    private enum state {
        students,
        test,
        results
    }

    private static state currState;

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

        //loading the graph when this is created

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
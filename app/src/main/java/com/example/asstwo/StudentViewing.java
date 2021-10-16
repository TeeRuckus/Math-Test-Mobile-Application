package com.example.asstwo;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
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
    private static String test;
    private CharSequence testToOpen;
    private static final int REQUEST_EMAIL = 1234;
    private emailListener listenerSend;

   /* @Override
    public void onListSelected(CharSequence currTitle) {
        Log.e(TAG, "AM I ACTUALLY GOING TO BE USEFUL TO YOU MY MASTER");
        testToOpen = currTitle;
        Log.e(TAG, "inside of me? " + currTitle);

        FragmentManager fm = getSupportFragmentManager();
        ItemViewRecycler frag = (ItemViewRecycler) fm.findFragmentById(R.id.viewingContainer);
        if (frag == null) {
            //making sure that the users are going to be displayed on the screen and nothing else
            ItemViewRecycler.usersViewing();
            frag = new ItemViewRecycler();
            fm.beginTransaction()
                    .add(R.id.viewingContainer, frag)
                    .commit();
        }
    }*/

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
        emailBttn = findViewById(R.id.sendEmailBttn);
        banner = findViewById(R.id.bannerStudentViewing);
        String name = null;

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
                    //making sure that the users are going to be displayed on the screen and nothing else
                    ItemViewRecycler.testViewing();
                    frag = new ItemViewRecycler();
                    fm.beginTransaction()
                            .add(R.id.viewingContainer, frag)
                            .commit();

                }

                emailBttn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = getIntent().getStringExtra("name");
                        String test = getIntent().getStringExtra("test");
                        listenerSend.emailTo(name);
                        //email(name, test);
                    }
                });

                break;

            case results:
                name = getIntent().getStringExtra("name");
                test = getIntent().getStringExtra("test");
                banner.setText("Results: " + name.split(" ")[0]);

                if (name != null )
                {
                    if (test != null)
                    {
                        //doing our magical shit here
                        if (frag == null)
                        {
                            ItemViewRecycler.results();
                            frag= new ItemViewRecycler();
                            fm.beginTransaction()
                                    .add(R.id.viewingContainer, frag)
                                    .commit();
                        }

                    }
                    else
                    {
                        Toast.makeText(StudentViewing.this, "Test not received", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(StudentViewing.this, "Name not received", Toast.LENGTH_LONG).show();
                }

                //the button is going to be only visible on results anyways


                break;
        }

    }

    public interface emailListener {
        void emailTo(String name);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof emailListener)
        {
            listenerSend = (emailListener) fragment;
        }
        else
        {
            throw new RuntimeException(fragment.toString() +
                    " must implement email listener");
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        listenerSend = null;
    }

    public void email(String name, String test)
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SENDTO);
        //we only allow to open mailing apps in this intent
        intent.setData(Uri.parse("mailto:"));
        //you can add all the emails which are going to be registered under the users account
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"tawanakwaramba@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Test results for jeffery");
        intent.putExtra(Intent.EXTRA_TEXT, "body of the text");
        startActivityForResult(intent, REQUEST_EMAIL);
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

    public static String getTest() {
        return test;
    }
}
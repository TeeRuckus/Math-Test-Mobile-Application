package com.example.asstwo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class TakeTest extends AppCompatActivity {

    private static final String TAG = "TakeTest.";
    private String name;
    private TextView banner;
    private TextView question;
    private TextView time;
    private TextView questionNumber;
    private TextView currenScore;
    private TextView answerSet;
    private Button end;
    private ImageButton next;
    private ImageButton prev;

    private FragmentManager fm;
    private QuestionButtons answerButtons;
    private AnswerInput inputAnswer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_test);
        loadUI();

        name = getIntent().getStringExtra("name");

        if (name != null)
        {
            banner.setText("Test: " + name);
            setUpButtonFragment();


        }
        else
        {
            Toast.makeText(TakeTest.this, "User Name not recieved", Toast.LENGTH_LONG).show();
        }
    }

    public void setUpButtonFragment()
    {
        fm = getSupportFragmentManager();
        answerButtons = (QuestionButtons) fm.findFragmentById(R.id.buttonsContainer);
        answerButtons = new QuestionButtons();

        //staging for the data I want to pass inside the fragmen
        Bundle argsButtons = new Bundle();

        fm.beginTransaction().add(R.id.buttonsContainer, answerButtons).commit();
    }

    public void setUpInputFragment()
    {
        fm = getSupportFragmentManager();
        inputAnswer = (AnswerInput) fm.findFragmentById(R.id.buttonsContainer);
        inputAnswer = new AnswerInput();
        fm.beginTransaction().add(R.id.buttonsContainer, inputAnswer).commit();
    }

    public void loadUI()
    {
        banner = findViewById(R.id.bannerTest);
        question = findViewById(R.id.testQuestionArea);
        time = findViewById(R.id.displayTestTime);
        questionNumber = findViewById(R.id.displayQuestionNumberTest);
        currenScore = findViewById(R.id.displayScoreTest);
        answerSet = findViewById(R.id.questionSetTest);
        end = findViewById(R.id.endTest);
        next = findViewById(R.id.nextAnswers);
        prev = findViewById(R.id.previousAnswers);
    }
}
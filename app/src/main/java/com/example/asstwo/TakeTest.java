package com.example.asstwo;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;


public class TakeTest extends AppCompatActivity implements QuestionButtons.QuestionBttnsListener, AnswerInput.AnswerInputListener {

    private String name;
    private  String currTitle;
    private TextView banner;
    private TextView question;
    private TextView time;
    private TextView questionNumber;
    private TextView currentScore;
    private TextView answerSet;
    private TextView elapsedTime;
    private Button end;
    private ImageButton next;
    private ImageButton prev;
    private Button skipQuestion;
    private Graph mathTestGraph;

    private int numSections;
    private int currSectionPos;
    private int[][] madeSections;
    private int userScore;
    private int userTime;

    private FragmentManager fm;
    private QuestionButtons answerButtons;
    private AnswerInput inputAnswer;

    private URL url;
    private HttpsURLConnection conn;
    private ArrayList<MenuItem> takenQuestions;
    private CountDownTimer cTimer;

    //references for all the elements inside of the fragments
    Button optionOne;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
        mathTestGraph.save(TakeTest.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mathTestGraph.save(TakeTest.this);
    }

    @Override
    public void onClickOptionOne(CharSequence input) {
        checkAnswer(input);
    }

    @Override
    public void onClickOptionTwo(CharSequence input) {
        checkAnswer(input);
    }

    @Override
    public void onClickOptionThree(CharSequence input) {
        checkAnswer(input);
    }

    @Override
    public void onClickOptionFour(CharSequence input) {
        checkAnswer(input);
    }

    @Override
    public void onClickAnswer(CharSequence input) {
        checkAnswer(input);
    }

    public void checkAnswer(CharSequence inAnswer)
    {
        int currSize = takenQuestions.size();
        MenuItem currQuestion = takenQuestions.get(currSize - 1);
        int currAnswer = currQuestion.getAnswer();
        String inAnswerString = inAnswer.toString();
        String currAnswerString =  Integer.toString(currAnswer);
        //setting the amount of time which has elapsed
        currQuestion.setElapsedTime(userTime);

        //getting the current question and setting the users respone
        int size = takenQuestions.size();
        MenuItem lastQuestion = takenQuestions.get(size - 1);
        lastQuestion.setResponse(inAnswerString);

        if(currAnswerString.equals(inAnswerString))
        {
            Toast.makeText(TakeTest.this, " +10 Correct Answer", Toast.LENGTH_SHORT).show();
            //cancelling the previous time and starting a new one so that the will have enough time
            //to see their result of the question and start next question
            cancelTimer();
            userScore += 10;
            currQuestion.setCurrScore(userScore);
            currentScore.setText(Integer.toString(userScore));
            currentScore.setTextColor(getResources().getColor(android.R.color.holo_green_dark, getTheme()));
            //startTimer(1);
            new  MyTask().execute();
        }
        else
        {
            Toast.makeText(TakeTest.this, " -5 Incorrect! Correct Answer: " + currAnswerString
            ,Toast.LENGTH_SHORT).show();
            //cancelling the previous timer and starting a new one so that will have enough time
            //to see their result of the question and srart the next question
            cancelTimer();
            userScore -= 5;
            currQuestion.setCurrScore(userScore);
            currentScore.setText(Integer.toString(userScore));
            currentScore.setTextColor(getResources().getColor(android.R.color.holo_red_dark, getTheme()));
            //startTimer(1);
            new  MyTask().execute();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_test);
        loadUI();


        mathTestGraph = new Graph();
        mathTestGraph = mathTestGraph.load(TakeTest.this);

        //making sure that instances of the two fragments are going to be initialised so I can
        //attach the required interfaces
        answerButtons = new QuestionButtons();
        inputAnswer = new AnswerInput();

        takenQuestions = new ArrayList<>();
        cTimer = null;

        name = getIntent().getStringExtra("name");
        numSections = 0;
        currSectionPos = 0;
        userTime = 0;

        if (name != null)
        {
            User currUser = mathTestGraph.getVertex(name).getValue();
            int numberOfTests = currUser.getHistory().size() + 1;
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            currTitle = "Test: " + numberOfTests + " Started at: " + dtf.format(now);
            banner.setText("Test: " + name);
            setUpButtonFragment();
            //setUpInputFragment();
            new  MyTask().execute();

            skipQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //making another JSON call so we can get a new question
                    currSectionPos = 0;
                    int size = takenQuestions.size();
                    MenuItem currQuestion = takenQuestions.get(size - 1);
                    currQuestion.setElapsedTime(userTime);
                    cancelTimer();
                    new  MyTask().execute();
                }
            });


            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currSectionPos = (currSectionPos + 1 ) % numSections;
                    displayOptions(madeSections);
                }
            });

            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currSectionPos = (currSectionPos - 1) % numSections;
                    currSectionPos = Math.abs(currSectionPos);
                    displayOptions(madeSections);
                }
            });

            end.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //getting the  current time and date so we can make a new entry for the test
                    TestHistory historyEntry = new TestHistory();
                    User currUser = mathTestGraph.getVertex(name).getValue();
                    //need to addd one as arrays are going to be zero index. Hence, making it more
                    //human read able by adding a one to the test title

                    //getting the last question, and setting what the elapsed time has being
                    int size = takenQuestions.size();
                    MenuItem currQuestion = takenQuestions.get(size - 1);
                    currQuestion.setElapsedTime(userTime);
                    currQuestion.setCurrScore(userScore);

                    historyEntry.setTitle(currTitle);
                    historyEntry.setQuestions(takenQuestions);
                    currUser.addHistoryEntry(historyEntry);
                    mathTestGraph.save(TakeTest.this);

                    //making sure that all the things are going to be set to null
                    //so we don't get unexpected behaviour out of the system
                    cancelTimer();
                    answerButtons = null;
                    inputAnswer = null;

                    //restaring the detials activity with the current user
                    Intent intent = new Intent(TakeTest.this, Details.class);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }
            });

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

        fm.beginTransaction().add(R.id.buttonsContainer, answerButtons).commit();
    }

    public void setUpInputFragment()
    {
        fm = getSupportFragmentManager();
        inputAnswer = (AnswerInput) fm.findFragmentById(R.id.buttonsContainer);
        inputAnswer = new AnswerInput();

        fm.beginTransaction().add(R.id.buttonsContainer, inputAnswer).commit();
    }

    public void replaceWithButtons(String opOne, String opTwo, String opThree, String opFour)
    {
        next.setVisibility(View.VISIBLE);
        prev.setVisibility(View.VISIBLE);
        next.setClickable(true);
        prev.setClickable(true);
        fm = getSupportFragmentManager();
        answerButtons = new QuestionButtons();
        Bundle bundle = new Bundle();
        bundle.putString("optionOne",opOne);
        bundle.putString("optionTwo",opTwo);
        bundle.putString("optionThree",opThree);
        bundle.putString("optionFour", opFour);
        answerButtons.setArguments(bundle);
        fm.beginTransaction().replace(R.id.buttonsContainer, answerButtons).commit();
    }


    public void replaceWithInput()
    {
        answerSet.setText("");
        next.setVisibility(View.GONE);
        prev.setVisibility(View.GONE);
        next.setClickable(false);
        prev.setClickable(false);

        fm = getSupportFragmentManager();
        inputAnswer = new AnswerInput();
        fm.beginTransaction().replace(R.id.buttonsContainer, inputAnswer).commit();
    }

    public void loadUI()
    {
        banner = findViewById(R.id.bannerTest);
        question = findViewById(R.id.testQuestionArea);
        time = findViewById(R.id.displayTestTime);
        questionNumber = findViewById(R.id.displayQuestionNumberTest);
        currentScore = findViewById(R.id.displayScoreTest);
        answerSet = findViewById(R.id.questionSetTest);
        end = findViewById(R.id.endTest);
        next = findViewById(R.id.nextAnswers);
        prev = findViewById(R.id.previousAnswers);
        skipQuestion = findViewById(R.id.skipTestQuestion);
        elapsedTime = findViewById(R.id.displayElapsedTime);
    }

    public void startTimer(int requiredTime)
    {
        //need to make it final so we can access it inside the annoymous classes which we have made

        float halfTime = requiredTime / 2;
        cTimer = new CountDownTimer(requiredTime * 1000, 1000) {
            @Override
            public void onTick(long l) {
                int currentTime = (int) (l / 1000);
                time.setText(Float.toString(currentTime));
                int countUp = (requiredTime - currentTime);
                countUp = userTime + countUp;

                //once we have reached the finished time, we should update the users current time
                if(currentTime == requiredTime)
                {
                    userTime += countUp;
                }

                elapsedTime.setText(Integer.toString(countUp));


                if (currentTime <= halfTime)
                {
                    time.setTextColor(getResources().getColor(R.color.paleNight_error));
                }
                else
                {
                    time.setTextColor(getResources().getColor(R.color.paleNight_text));
                }

            }
            @Override
            public void onFinish() {
                cancelTimer();
                time.setText("done!");
                currSectionPos = 0;
                new  MyTask().execute();
            }
        };
        cTimer.start();
    }

    public void cancelTimer() {
        if (cTimer !=null){
            float timeLeft = Float.parseFloat(time.getText().toString().trim());
            //getting the time which you had to solve
            int size = takenQuestions.size();
            MenuItem currQuestion = takenQuestions.get(size - 1);
            int actualTime = currQuestion.getTime();
            int timeUser = actualTime -  (int) timeLeft;
            userTime += timeUser;
            cTimer.cancel();
        }
    }

    private int[] convertToArray(JSONArray inArray)
    {
        int[] result = new int[inArray.length()];

        for (int ii = 0; ii < inArray.length(); ii++) {
            try
            {
                result[ii] = inArray.getInt(ii);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return result;
    }

    private void displayQuestion(MenuItem inQuestion)
    {
        question.setText(inQuestion.getQuestion());
        //time.setText(Integer.toString(inQuestion.getTime()));
        questionNumber.setText(Integer.toString(takenQuestions.size()));
        startTimer(inQuestion.getTime());

    }

    public void setUpQuestionInput(int[] options)
    {
        if(options.length != 0)
        {
            replaceWithButtons("1", "2", "6", "4");
            madeSections = makeSections(options);

            displayOptions(madeSections);
        }
        else
        {
            replaceWithInput();
            //set up the text inputs
        }
    }

    public void displayOptions(int[][] availableOptions)
    {
        int[] inAnswers = availableOptions[currSectionPos];
        int numOptions = inAnswers.length;

        //making sure that the number which is going to be abvaliable in the screen is going to be
        //human readable format
        int displayCurrPos = currSectionPos + 1;
        answerSet.setText("Answer Set: " + (displayCurrPos)  + " / " + numSections);

        switch (numOptions)
        {
            case 1:
                //you should replace this with a another fragment whcih is going to be a single button located in the middle
                replaceWithButtons(Integer.toString(inAnswers[0]), "", "", "");
                break;

            case 2:
                replaceWithButtons(Integer.toString(inAnswers[0]),
                        Integer.toString(inAnswers[1]),
                        "",
                        "");
                break;

            case 3:
                //TODO: you should replace with buttons except that they're going to be round instead
                replaceWithButtons(Integer.toString(inAnswers[0]),
                        Integer.toString(inAnswers[1]),
                        Integer.toString(inAnswers[2]),
                        "");
                break;

            case 4:
                replaceWithButtons(Integer.toString(inAnswers[0]),
                        Integer.toString(inAnswers[1]),
                        Integer.toString(inAnswers[2]),
                        Integer.toString(inAnswers[3]));
                break;
        }
    }

    public int[][] makeSections(int[] options)
    {
        int size = options.length;
        //we can only have a maximum of 4 buttons on the screen at a time
        float sections = (float) size / 4;
        // if we have a reminder we will want to round up no matter what so that we can fit in the reminders
        numSections = (int) Math.ceil(sections);
        int[] currSections = new int[numSections];
        int[][] optionsGroup = loadSections(numSections, options);

        return optionsGroup;

    }

    public int[][] loadSections(int numSections, int[] originalArray)
    {
        int[][] returnArray = new int[numSections][];
        //keeps a track on what index it's going to be on the original array
        int currIndx = 0;

        for (int ii = 0; ii < numSections; ii++)
        {
            ArrayList<Integer> currSec = new ArrayList<>();

            for(int jj = 0; jj < originalArray.length; jj++)
            {
                //a look ahead to make sure that we're only storing the elmennts which we want at
                //each poisition of our array
                if(jj >= currIndx  && jj < currIndx + 4)
                {
                    //trying to create a section of four, if we can't we should do nothing
                    try{
                        currSec.add(originalArray[jj]);
                    }
                    catch (IndexOutOfBoundsException e)
                    {
                        //do nothing as we have reched the end of the current array
                    }

                }
            }

            currIndx = currIndx + 4;

            returnArray[ii] = new int[currSec.size()];
            for(int kk=0; kk < currSec.size(); kk++)
            {
                returnArray[ii][kk] = currSec.get(kk);
            }
        }

        return returnArray;

    }



    private class MyTask extends AsyncTask<Void, Integer, String>
    {

        @Override
        protected String doInBackground(Void... voids) {
            connectToServer();
            String result = null;
            InputStream input = null;
            ByteArrayOutputStream boas = null;

            try
            {
                input = conn.getInputStream();
                boas = new ByteArrayOutputStream();
                //TODO: come back and add teh code for the progress bar

                byte[] buffer = new byte[1024];
                int bytesRead = input.read(buffer);
                int progress = 0;

                while (bytesRead > 0)
                {
                    boas.write(buffer, 0, bytesRead);
                    bytesRead = input.read(buffer);
                    progress += bytesRead;
                    //TODO: add code for progress ba
                }

                boas.close();
                result = new String(boas.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                conn.disconnect();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String retrievedJSON) {
            //super.onPostExecute(s);

            try
            {
                JSONObject jBase = new JSONObject(retrievedJSON);
                JSONArray options = jBase.getJSONArray("options");
                String questionJson = jBase.getString("question");
                int result= jBase.getInt("result");
                int time = jBase.getInt("timetosolve");

                int[] optionsArray = convertToArray(options);
                MenuItem currQuestion = new MenuItem(questionJson, time, result, optionsArray);
                takenQuestions.add(currQuestion);
                setUpQuestionInput(optionsArray);
                displayQuestion(currQuestion);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        private void connectToServer()
        {
            //connecting to the server which we have just made for our application
            String urlString =
                    Uri.parse("https://192.168.1.102:8000/random/question/")
                    .buildUpon()
                    .appendQueryParameter("method", "thedata.getit")
                    .appendQueryParameter("api_key", "01189998819991197253")
                    .appendQueryParameter("format", "json")
                    .build().toString();
            try
            {
                url = new URL(urlString);
                conn = (HttpsURLConnection) url.openConnection();
                // telling androi studio to trust the certificate which we just create for ourselves
                DownloadUtils.addCertificate(TakeTest.this, conn);

                if(conn.getResponseCode() != HttpURLConnection.HTTP_OK)
                {
                    Toast.makeText(TakeTest.this, "Can't access website", Toast.LENGTH_LONG ).show();
                }
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
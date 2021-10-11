package com.example.asstwo;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
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
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class TakeTest extends AppCompatActivity implements QuestionButtons.QuestionBttnsListener {

    private static final String TAG = "TakeTest.";
    private String name;
    private TextView banner;
    private TextView question;
    private TextView time;
    private TextView questionNumber;
    private TextView currentScore;
    private TextView answerSet;
    private Button end;
    private ImageButton next;
    private ImageButton prev;
    private Button skipQuestion;

    private int numSections;
    private int currSectionPos;
    private int[] currentOptions;
    private int[][] madeSections;

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
    }

    @Override
    public void onClickOptionOne(CharSequence input) {
        Log.e(TAG, "Option one which was clikcked " + input);
    }

    @Override
    public void onClickOptionTwo(CharSequence input) {
        Log.e(TAG, "Option one which was clikcked " + input);

    }

    @Override
    public void onClickOptionThree(CharSequence input) {
        Log.e(TAG, "Option one which was clikcked " + input);

    }

    @Override
    public void onClickOptionFour(CharSequence input) {
        Log.e(TAG, "Option one which was clikcked " + input);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_test);
        loadUI();

        //making sure that instances of the two fragments are going to be initialised so I can
        //attach the required interfaces
        answerButtons = new QuestionButtons();
        inputAnswer = new AnswerInput();

        takenQuestions = new ArrayList<>();
        cTimer = null;

        name = getIntent().getStringExtra("name");
        numSections = 0;
        currSectionPos = 0;

        if (name != null)
        {
            banner.setText("Test: " + name);
            setUpButtonFragment();
            new  MyTask().execute();

            skipQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //making another JSON call so we can get a new question
                    currSectionPos = 0;
                    cancelTimer();
                    new  MyTask().execute();
                }
            });


            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(TAG, "next has being clicked");
                    currSectionPos = (currSectionPos + 1 ) % numSections;
                    Log.e(TAG, "The next position: " + currSectionPos);
                    displayOptions(madeSections);
                }
            });

            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(TAG, "previous has bneing clicked");
                    //currSectionPos = Math.abs((currSectionPos - 1)) % numSections;
                    //currSectionPos = (currSectionPos % numSections) - 1;
                    currSectionPos = (currSectionPos - 1) % numSections;
                    currSectionPos = Math.abs(currSectionPos);
                    displayOptions(madeSections);
                }
            });

            /*optionOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(TAG, "You clicked me you MOTHER FUCKER");
                }
            });*/

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
    }

    public void startTimer(int requiredTime)
    {
        float halfTime = requiredTime / 2;
        cTimer = new CountDownTimer(requiredTime * 1000, 1000) {
            @Override
            public void onTick(long l) {
                int currentTime = (int) (l / 1000);
                time.setText(Float.toString(currentTime));

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
                time.setText("done!");
                cancelTimer();
                currSectionPos = 0;
                new  MyTask().execute();
            }
        };
        cTimer.start();
    }

    public void cancelTimer() {
        if (cTimer !=null){
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
                Log.e(TAG, "Couldn't read integer: " + e.getMessage());
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
        currentScore.setText("100");
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
                replaceWithButtons(Integer.toString(inAnswers[0]), "", "", "");
                break;

            case 2:
                replaceWithButtons(Integer.toString(inAnswers[0]),
                        Integer.toString(inAnswers[1]),
                        "",
                        "");
                break;

            case 3:
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
        Log.i(TAG, "Sections to be made: " + numSections);
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
                Log.e(TAG, e.getMessage());
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

                Log.e(TAG, "the current options: " + options);

                displayQuestion(currQuestion);


                //MenuItem currQuestion =  new MenuItem(question, time, result, options);

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

            Log.e(TAG, "found url: " + urlString);

            try
            {
                url = new URL(urlString);
                conn = (HttpsURLConnection) url.openConnection();
                // telling androi studio to trust the certificate which we just create for ourselves
                DownloadUtils.addCertificate(TakeTest.this, conn);

                if(conn.getResponseCode() != HttpURLConnection.HTTP_OK)
                {
                    Log.e(TAG, "Can't not access requrested website");
                }
            } catch (GeneralSecurityException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            } catch (MalformedURLException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        }

    }
}
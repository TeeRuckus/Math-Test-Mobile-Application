package com.example.asstwo;

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
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.AsynchronousChannelGroup;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class TakeTest extends AppCompatActivity {

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

    private FragmentManager fm;
    private QuestionButtons answerButtons;
    private AnswerInput inputAnswer;

    private URL url;
    private HttpsURLConnection conn;
    private ArrayList<MenuItem> takenQuestions;
    private CountDownTimer cTimer;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_test);
        loadUI();

        takenQuestions = new ArrayList<>();
        cTimer = null;

        name = getIntent().getStringExtra("name");

        if (name != null)
        {
            banner.setText("Test: " + name);
            setUpButtonFragment();
            new  MyTask().execute();

            skipQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //making another JSON call so we can get a new question
                    cancelTimer();
                    new  MyTask().execute();
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
            }
        };
        cTimer.start();
    }

    public void cancelTimer() {
        if (cTimer !=null){
            cTimer.cancel();
        }
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
    }
}
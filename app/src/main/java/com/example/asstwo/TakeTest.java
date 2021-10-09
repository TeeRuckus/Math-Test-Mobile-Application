package com.example.asstwo;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.AsynchronousChannelGroup;
import java.security.GeneralSecurityException;

import javax.net.ssl.HttpsURLConnection;

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

    private URL url;
    private HttpsURLConnection conn;



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
            new  MyTask().execute();

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
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);

            //getting  the json and displaying it on the screen
            question.setText(s);

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
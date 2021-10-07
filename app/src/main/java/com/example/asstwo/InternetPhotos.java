package com.example.asstwo;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class InternetPhotos extends AppCompatActivity {

    private static final String TAG = "internetPhotos.";
    private static final int RETURNED_IMAGES = 50;

    //only doing this, so we can access a lopping variable inside of a annoymous class
    private static int ii_special;
    private static int stop_images;

    private GridView gridview;
    private ImageButton search;
    private EditText searchItem;
    ProgressBar progressBar;

    private String[] names;
    private Drawable[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_photos);

        loadUIElements();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyTask().execute();
            }
        });
    }

    public void loadImagesGridView()
    {
        Log.e(TAG, "Images: " + images);
        if (images != null)
        {
            gridview.setVisibility(View.VISIBLE);
            imageAdapters adapter = new imageAdapters(names, images,this);
            gridview.setAdapter(adapter);

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String selectedName = names[i];
                    Drawable selectedImage = images[i];

                    //packaging the selected image and sending it back to the registration activity

                }
            });
        }
    }

    private class MyTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String key = searchItem.getText().toString();
            getImages(key);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            //after evrything is done we should display the images on the screen
            progressBar.setVisibility(View.INVISIBLE);
            loadImagesGridView();

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //super.onProgressUpdate(values);
            animateProgressBar(values[0]);
        }

        private void getImages(String searchItem) {
            String data = connectAPI(searchItem);
            String[] imageUrl = getImageCollectoin(data);
            images = new Drawable[stop_images];
            names = new String[stop_images];
            if (data != null) {
                if (imageUrl != null) {

                    for (int ii = 0; ii < stop_images; ii++)
                    {
                        Bitmap image = getImageFromUrl(imageUrl[ii]);
                        if (image != null)
                        {
                            Drawable currDrawable = new BitmapDrawable(getResources(), image);
                            //int currImage  = Integer.parseInt(currDrawable.toString());
                            Log.e(TAG, "End parameter: " + RETURNED_IMAGES);
                            Log.e(TAG, "Current index: " + ii);
                            images[ii] = currDrawable;
                            names[ii] = searchItem + " " + Integer.toString(ii + 1);
                        }
                    }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
}

        //this is to get one single image from a given url
        private Bitmap getImageFromUrl(String imageUrl) {
            Bitmap image = null;
            Uri.Builder url = Uri.parse(imageUrl).buildUpon();
            String urlString = url.build().toString();
            Log.e(TAG, "individual Image URL : " + urlString);


            HttpURLConnection conn = establishConnection(urlString);
            if (conn == null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(InternetPhotos.this, "Check internet", Toast.LENGTH_LONG).show();
                    }
                });
            } else if (checkConnectionOkay(conn) == false) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(InternetPhotos.this, "Problem wiht downloading", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                image = downloadToBitmap(conn);
                conn.disconnect();
            }

            return image;
        }

        private Bitmap downloadToBitmap(HttpURLConnection conn) {
            setProgressBar(conn.getContentLength());
            Bitmap image = null;
            try {
                InputStream is = conn.getInputStream();
                byte[] byteData = getByteArrayFromInputStream(is);
                Log.e(TAG, "Data: " + String.valueOf(byteData.length));
                image = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }

            return image;
        }

        private byte[] getByteArrayFromInputStream(InputStream inputStream) throws IOException {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[4096];
            int progress = 0;
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
                progress = progress + nRead;
                publishProgress(progress);
            }

            return buffer.toByteArray();
        }

        private String[] getImageCollectoin(String data) {
            //we only want to retrieve a maximum of 50 images in our search
            String imageUrl[] = null;

            try {
                JSONObject jBase = new JSONObject(data);
                JSONArray jHits = jBase.getJSONArray("hits");

                //if we actually results back from our search
                if (jHits.length() > 0) {
                    imageUrl = new String[RETURNED_IMAGES];
                    //not enough hits found by the programme
                    //loading up the returned string with the first 50 results which were returned
                    for (int ii = 0; ii < RETURNED_IMAGES; ii++) {
                        stop_images = ii;
                        Log.i(TAG, "Current index: " + ii);
                        JSONObject jHitsItem = jHits.getJSONObject(ii);
                        imageUrl[ii] = jHitsItem.getString("largeImageURL");
                    }
                } else {
                    Toast.makeText(InternetPhotos.this, "No results", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                //once this exception has happened, they're no more images to search so we should
                //return out of the function  straight away
                return imageUrl;
            }

            return imageUrl;
        }


        private String connectAPI(String searchItem) {
            String data = null;
            Uri.Builder url = Uri.parse("https://pixabay.com/api/").buildUpon();
            url.appendQueryParameter("key", "22656668-cebab27bc7924c4e89fa2f3b5");
            url.appendQueryParameter("q", searchItem);
            String urlString = url.build().toString();
            Log.i(TAG, "The built URL: " + urlString);

            HttpURLConnection conn = establishConnection(urlString);

            if (conn == null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(InternetPhotos.this, "check internet", Toast.LENGTH_LONG).show();
                    }
                });
            } else if (checkConnectionOkay(conn) == false) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(InternetPhotos.this, "Problem with downloading", Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                data = getDownloadString(conn);
                if (data != null) {
                    Log.e(TAG, data);
                } else {
                    Log.e(TAG, "Nothing returned");
                }

                conn.disconnect();
            }

            return data;
        }

        private String getDownloadString(HttpURLConnection conn) {
            String data = null;

            try {
                InputStream is = conn.getInputStream();
                byte[] byteData = IOUtils.toByteArray(is);
                data = new String(byteData, StandardCharsets.UTF_8);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }

            return data;
        }

        private boolean checkConnectionOkay(HttpURLConnection conn) {
            boolean valid = false;
            try {
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    valid = true;
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }

            return valid;
        }

        private HttpURLConnection establishConnection(String urlString) {
            HttpURLConnection conn = null;

            try {
                URL url = new URL(urlString);
                conn = (HttpURLConnection) url.openConnection();
            } catch (MalformedURLException e){
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
            catch (IOException e) {
                Log.e(TAG, "Something happend with the IO: " + e.getMessage());
                e.printStackTrace();
            }

            return conn;
        }

        private void setProgressBar(int max) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setMin(0);
                    progressBar.setMax(max);
                }
            });

        }

        private void animateProgressBar(int value) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(value);
                }
            });
        }
    }


    protected void loadUIElements()
    {
        gridview = findViewById(R.id.photosGridView);
        search = findViewById(R.id.searchInterntBttn);
        searchItem = findViewById(R.id.searchInternetInput);
        progressBar = findViewById(R.id.progressBarGridImages);

        progressBar.setVisibility(View.INVISIBLE);
        gridview.setVisibility(View.INVISIBLE);
    }

    //the adapter which this class is going to have
    public class imageAdapters extends BaseAdapter{
        private String[] imageNames;
        private Drawable[] imagePhotos;
        private Context cntx;
        private LayoutInflater li;

        public imageAdapters(String[] imageNames, Drawable[] imagePhotos, Context cntx) {
            this.imageNames = imageNames;
            this.imagePhotos = imagePhotos;
            this.cntx = cntx;
            this.li = (LayoutInflater) cntx.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return imagePhotos.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if(view == null){
                view = li.inflate(R.layout.row_item, viewGroup, false);
            }

            TextView viewName = view.findViewById(R.id.internetImageName);
            ImageView viewImage = view.findViewById(R.id.internetImageView);

            viewName.setText(imageNames[i]);
            viewImage.setImageDrawable(imagePhotos[i]);

            return view;
        }
    }
}
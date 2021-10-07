package com.example.asstwo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class InternetPhotos extends AppCompatActivity {

    private GridView gridview;
    private ImageButton search;
    private EditText searchItem;

    private String[] names = {"test name", "test name 2", "3", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4", "4"};
    private int[] images = {R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground,
    R.drawable.flag_ad, R.drawable.flag_ae, R.drawable.flag_af, R.drawable.flag_ag, R.drawable.flag_ai,
    R.drawable.flag_au, R.drawable.flag_al, R.drawable.flag_am, R.drawable.flag_ba, R.drawable.flag_az
    , R.drawable.flag_mx, R.drawable.flag_lt, R.drawable.flag_kr, R.drawable.flag_jp, R.drawable.flag_it
    , R.drawable.flag_hk, R.drawable.flag_gr, R.drawable.flag_ge, R.drawable.flag_ge, R.drawable.flag_gb};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_photos);

        loadUIElements();

        imageAdapters adapter = new imageAdapters(names, images,this);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedName = names[i];
                int selectedImage = images[i];

                //saving the selected image, and starting the regitration form and displaying the image
                //as the avatar on the form
            }
        });
    }

    protected void loadUIElements()
    {
        gridview = findViewById(R.id.photosGridView);
        search = findViewById(R.id.searchInterntBttn);
        searchItem = findViewById(R.id.searchInternetInput);
    }

    //the adapter which this class is going to have
    public class imageAdapters extends BaseAdapter{
        private String[] imageNames;
        private int[] imagePhotos;
        private Context cntx;
        private LayoutInflater li;

        public imageAdapters(String[] imageNames, int[] imagePhotos, Context cntx) {
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
            viewImage.setImageResource(imagePhotos[i]);

            return view;
        }
    }
}
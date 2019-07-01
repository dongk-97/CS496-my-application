package com.example.myapplication2;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;


public class FullImageActivity extends Activity {

    private Context mContext = null;
    private final int imgWidth = 430;
    private final int imgHeight = 730;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);

        // get intent data
        Intent i = getIntent();

        Bundle extras = i.getExtras();
        String imgPath = extras.getString("filename");


        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inSampleSize = 2;
        ImageView iv = (ImageView)findViewById(R.id.photo_view);
        Bitmap bm = BitmapFactory.decodeFile(imgPath, bfo);
        iv.setImageBitmap(bm);


    }

}
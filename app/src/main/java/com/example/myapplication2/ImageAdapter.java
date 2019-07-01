package com.example.myapplication2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageAdapter<string> extends BaseAdapter {

    private  Context mContext;
    private String imgData;
    private String geoData;
    private ArrayList<string> thumbsDataList;
    private ArrayList<string> thumbsIDList;

    ImageAdapter(Context c){
        mContext = c;
        thumbsDataList = new ArrayList<string>();
        thumbsIDList = new ArrayList<string>();
        getThumbInfo(thumbsIDList, thumbsDataList);
    }

    public boolean deleteSelected(int sIndex){
        return true;
    }

    @Override
    public int getCount() {
        return thumbsIDList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null){
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setAdjustViewBounds(false);
            imageView.setPadding(2, 2, 2, 2);
        }else{
            imageView = (ImageView) convertView;
        }
        BitmapFactory.Options bo = new BitmapFactory.Options();
        bo.inSampleSize = 2;
        Bitmap bmp = BitmapFactory.decodeFile((String) thumbsDataList.get(position), bo);
        Bitmap resized = Bitmap.createScaledBitmap(bmp, 200, 200, true);
        imageView.setImageBitmap(resized);

        return imageView;
    }

    private void getThumbInfo(ArrayList<string> thumbsIDs, ArrayList<string> thumbsDatas) {

        String[] proj = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};

        Cursor imageCursor = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                proj, null, null, null);

        if (imageCursor != null && imageCursor.moveToFirst()){
            String title;
            String thumbsID;
            String thumbsImageID;
            String thumbsData;
            String data;
            String imgSize;

            int thumbsIDCol = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
            int thumbsDataCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int thumbsImageIDCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
            int thumbsSizeCol = imageCursor.getColumnIndex(MediaStore.Images.Media.SIZE);
            int num = 0;
            do {
                thumbsID = imageCursor.getString(thumbsIDCol);
                thumbsData = imageCursor.getString(thumbsDataCol);
                thumbsImageID = imageCursor.getString(thumbsImageIDCol);
                imgSize = imageCursor.getString(thumbsSizeCol);
                num++;
                if (thumbsImageID != null){
                    thumbsIDs.add((string) thumbsID);
                    thumbsDatas.add((string) thumbsData);
                }
            }while (imageCursor.moveToNext());
        }
        imageCursor.close();
        return;

    }


    public void callImageViewer(int position) {
        Intent i = new Intent(mContext, FullImageActivity.class);
        String imgPath = getImageInfo(imgData, geoData, (String) thumbsIDList.get(position));
        i.putExtra("filename", imgPath);
        ((Activity) mContext).startActivityForResult(i, 1);


    }

    private String getImageInfo (String ImageData, String Location, String thumbID){
        String imageDataPath = null;
        String[] proj = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};
        Cursor imageCursor = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                proj, "_ID='"+ thumbID +"'", null, null);

        if (imageCursor != null && imageCursor.moveToFirst()){
            if (imageCursor.getCount() > 0){
                int imgData = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                imageDataPath = imageCursor.getString(imgData);
            }
        }
        imageCursor.close();
        return imageDataPath;
    }
}
package com.example.myapplication2;

import android.app.Activity;
import android.widget.Toast;

public class BackPressCloseHandler {
    private Activity activity;
    public BackPressCloseHandler(Activity activity) {
        this.activity = activity;
    }
    public void onBackPressed() {
            activity.finish();
    }
}


package com.example.myapplication2;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

public class Allimmaker extends Activity {


    private static Notification.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.allimmaker);

        Intent intent = getIntent();
        int data = intent.getIntExtra("id",0);

        TextView reset = (TextView) findViewById(R.id.timetostart);
        DBtodoHelper db = new DBtodoHelper(this);
        Todo todo = db.getTodo(data);
        int hour = todo.getHour();
        int minute = todo.getMinute();
        String contents = todo.getContents();
        reset.setText("시간이 벌써 " +hour+"시 "+minute+"분이에요\n" + contents + "을(를) 할 시간이에요! ");


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Todo : "+ contents);
        builder.setContentText("push 알림을 삭제하려면 터지하세요");
        builder.setColor(Color.RED);


        // 사용자가 탭을 클릭하면 이동
        Intent notiIconClickIntent = new Intent(this, MainActivity.class);
        notiIconClickIntent .putExtra("particularFragment", "notiIntent");

        notiIconClickIntent .setAction(Intent.ACTION_MAIN);
        notiIconClickIntent .addCategory(Intent.CATEGORY_LAUNCHER);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(Allimmaker.this);
        stackBuilder.addParentStack(MainActivity.class);


        stackBuilder.addNextIntent(notiIconClickIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }


        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        notificationManager.notify(1, builder.build());
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Allimmaker.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}


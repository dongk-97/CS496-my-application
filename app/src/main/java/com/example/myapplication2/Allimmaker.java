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

import android.view.View;

import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import androidx.core.app.NotificationManagerCompat;


public class Allimmaker extends Activity {


    private static Notification.Builder builder;
    final private Context mcontext = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.allimmaker);

        Intent intent = getIntent();
        int data = intent.getIntExtra("id",0);


        TextView timetostart = (TextView) findViewById(R.id.timetostart);
        final DBtodoHelper db = new DBtodoHelper(this);
        final Todo todo = db.getTodo(data);
        todo.setSwitching("false");
        db.updateTodo(todo);
        String h = String.valueOf(todo.getHour());
        String m = String.valueOf(todo.getMinute());
        if(String.valueOf(todo.getMinute()).length()==1){
            m = "0"+m;
        }
        String contents = todo.getContents();
        timetostart.setText("시간이 벌써 " +h+"시 "+m+"분이에요\n" + contents + "을(를) 할 시간이에요! ");



        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Todo : "+ contents);
        builder.setContentText("push 알림을 삭제하려면 터치하세요");
        builder.setColor(Color.RED);

        Button timeDelay = (Button) findViewById(R.id.timeDelayBtn);
        timeDelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationManagerCompat.from(mcontext).cancel(todo.getId());
                Intent notiIconClickIntent = new Intent(mcontext, MainActivity.class);
                notiIconClickIntent.putExtra("particularFragment", "notiIntent");
                startActivity(notiIconClickIntent);
                finish();
            }
        });

        Button reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



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

        notificationManager.notify(todo.getId(), builder.build());
    }



    @Override
    public void onBackPressed() {
        finish();
    }
}


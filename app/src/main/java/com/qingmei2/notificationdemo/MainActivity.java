package com.qingmei2.notificationdemo;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qingmei2.notificationdemo.notify.NotificationHelper;
import com.qingmei2.notificationdemo.notify.entity.Channel;
import com.qingmei2.notificationdemo.notify.entity.Importance;
import com.qingmei2.notificationdemo.notify.entity.Notification;

public class MainActivity extends AppCompatActivity {

    private Channel channel = new Channel("Alert", "警报", "火灾警报等重要推送信息。");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Application application = getApplication();
        NotificationHelper.Companion.register(application);

        Notification model1 = new Notification.Builder()
                .withId(1)
                .withSmallIconRes(R.mipmap.ic_launcher_round)
                .withTitle("发送一条通知哇")
                .withContent("通知内容")
                .withPriority(Importance.URGENT)
                .withFloatMode(true)
                .withChannel(channel)
                .withOnIntentInit(intent -> intent.putExtra("name", "jack"))
                .build();

        Notification model2 = new Notification.Builder()
                .withId(1)
                .withSmallIconRes(R.mipmap.ic_launcher_round)
                .withTitle("更新一条通知哇")
                .withContent("更新通知内容")
                .withPriority(Importance.URGENT)
                .withSoundRes(R.raw.alert)
                .withFloatMode(true)
                .withChannel(channel)
                .withOnIntentInit(intent -> intent.putExtra("name", "rose"))
                .build();

        findViewById(R.id.btn_send).setOnClickListener(__ ->
                NotificationHelper.Companion.getInstance().sendNotification(model1)
        );

        findViewById(R.id.btn_update).setOnClickListener(__ ->
                NotificationHelper.Companion.getInstance().updateNotification(model2)
        );

        findViewById(R.id.btn_delete).setOnClickListener(__ ->
                NotificationHelper.Companion.getInstance().deleteNotification(model2)
        );
    }
}

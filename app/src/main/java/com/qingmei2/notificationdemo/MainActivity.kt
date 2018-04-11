package com.qingmei2.notificationdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.qingmei2.NotificationHelper
import com.qingmei2.notificationdemo.notify.entity.Channel
import com.qingmei2.notificationdemo.notify.entity.Importance
import com.qingmei2.notificationdemo.notify.entity.Notification
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val channel = Channel("Alert", "警报", "火灾警报等重要推送信息。")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NotificationHelper.register(application)

        val model1 = Notification.Builder()
                .withId(1)
                .withSmallIconRes(R.mipmap.ic_launcher_round)
                .withTitle("发送一条通知哇")
                .withContent("通知内容")
                .withPriority(Importance.URGENT)
                .withFloatMode(true)
                .withChannel(channel)
                .withBackStackActivity(SecondActivity::class.java)
                .withOnIntentInitListener { it.putExtra("name", "jack") }
                .build()

        val model2 = Notification.Builder()
                .withId(1)
                .withSmallIconRes(R.mipmap.ic_launcher_round)
                .withTitle("更新一条通知哇")
                .withContent("更新通知内容")
                .withPriority(Importance.URGENT)
                .withSoundRes(R.raw.alert)
                .withFloatMode(true)
                .withChannel(channel)
                .withBackStackActivity(SecondActivity::class.java)
                .withOnIntentInitListener { it.putExtra("name", "rose") }
                .build()

        btn_send.setOnClickListener { NotificationHelper.instance?.sendNotification(model1) }
        btn_update.setOnClickListener { NotificationHelper.instance?.updateNotification(model2) }
        btn_delete.setOnClickListener { NotificationHelper.instance?.deleteNotification(model2) }
    }
}

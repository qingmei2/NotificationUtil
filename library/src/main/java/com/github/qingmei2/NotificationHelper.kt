package com.github.qingmei2

import android.annotation.TargetApi
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder

import com.qingmei2.notificationdemo.notify.entity.Notification
import java.lang.NullPointerException

class NotificationHelper private constructor(private val application: Application) {

    private val mNotificationManager: NotificationManager =
            application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun sendNotification(notification: Notification) {
        var resultIntent = Intent(application, notification.backStackActivity)
        if (notification.onIntentInit != null)
            resultIntent = notification.onIntentInit!!.onIntentInit(resultIntent)

        val stackBuilder = TaskStackBuilder.create(application)
        stackBuilder.addParentStack(notification.backStackActivity)
        stackBuilder.addNextIntent(resultIntent)

        val resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder: NotificationCompat.Builder

        builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getNotificationBuilder26(notification)
                    .setContentIntent(resultPendingIntent)
        } else {
            getNotificationBuilder14(notification)
                    .setContentIntent(resultPendingIntent)
        }

        mNotificationManager.notify(notification.id, builder.build())
    }

    @TargetApi(value = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private fun getNotificationBuilder14(notification: Notification): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(application)
                .setSmallIcon(notification.smallIconRes)
                .setPriority(notification.priority.importanceBelow25)
                .setContentTitle(notification.title)
                .setContentText(notification.content)
                .setAutoCancel(notification.autoCancel)

        if (notification.soundRes != 0) {
            val uri = Uri.parse("android.resource://com.qingmei2.notificationutil/" + notification.soundRes)
            builder.setSound(uri)
        }
        return builder
    }

    @TargetApi(value = Build.VERSION_CODES.O)
    private fun createNotificationChannel(notification: Notification) {
        val ch = notification.channel
        val channel = NotificationChannel(
                ch!!.channelId,
                ch.channelName,
                notification.priority.importanceOver26
        )
        channel.importance = notification.priority.importanceOver26
        channel.description = ch.channelDescription
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.setShowBadge(true)
        if (notification.soundRes != 0) {
            val uri = Uri.parse("android.resource://com.qingmei2.notificationdemo/" + notification.soundRes)
            channel.setSound(uri, AudioAttributes.Builder().build())
        }

        mNotificationManager.createNotificationChannel(channel)
    }

    @TargetApi(value = Build.VERSION_CODES.O)
    private fun getNotificationBuilder26(notification: Notification): NotificationCompat.Builder {
        this.createNotificationChannel(notification)
        return NotificationCompat.Builder(application, notification.channel!!.channelId)
                .setSmallIcon(notification.smallIconRes)
                .setContentTitle(notification.title)
                .setContentText(notification.content)
                .setAutoCancel(notification.autoCancel)
    }

    fun updateNotification(notification: Notification) {
        this.sendNotification(notification)
    }

    fun deleteNotification(notification: Notification) {
        this.deleteNotification(notification.id)
    }

    fun deleteNotification(notificationId: Int) {
        mNotificationManager.cancel(notificationId)
    }

    fun deleteAllNotifications() {
        mNotificationManager.cancelAll()
    }

    companion object {

        @Volatile
        var instance: NotificationHelper? = null

        fun register(application: Application?): NotificationHelper? {
            if (instance == null) {
                synchronized(NotificationHelper::class.java) {
                    if (application == null)
                        throw NullPointerException("You need call NotificationHelper.register() first.")
                    if (instance == null) {
                        instance = NotificationHelper(application)
                    }
                }
            }
            return instance
        }
    }

}

package cn.com.fenrir_inc.module_core.notify

import android.annotation.TargetApi
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import cn.com.fenrir_inc.module_core.notify.entity.Importance
import cn.com.fenrir_inc.module_core.notify.entity.Notification
import java.lang.NullPointerException

class NotificationHelper private constructor(private val application: Application) {

    private val mNotificationManager: NotificationManager =
            application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun sendNotification(notification: Notification) {
        var resultIntent = Intent(application, notification.backStackActivity)

        if (notification.onIntentInitListener != null)
            resultIntent = notification.onIntentInitListener!!.invoke(resultIntent)

        val stackBuilder = TaskStackBuilder.create(application)
                .apply {
                    addParentStack(notification.backStackActivity!!)
                    addNextIntent(resultIntent)
                }

        val resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder: NotificationCompat.Builder =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
                .setPriority(notification.priority?.importanceBelow25
                        ?: Importance.HIGH.importanceBelow25)
                .let {
                    if (notification.smallIconRes == 0)
                        it else it.setSmallIcon(notification.smallIconRes)
                }
                .let {
                    if (notification.title == null)
                        it else it.setContentTitle(notification.title)
                }
                .let {
                    if (notification.content == null)
                        it else it.setContentText(notification.content)
                }
                .let {
                    if (notification.soundUri == null)
                        it else it.setSound(notification.soundUri)
                }
                .setAutoCancel(notification.autoCancel)
        return builder
    }

    @TargetApi(value = Build.VERSION_CODES.O)
    private fun createNotificationChannel(notification: Notification) {
        val ch = notification.channel
        val channel = NotificationChannel(
                ch!!.channelId,
                ch.channelName,
                notification.priority?.importanceOver26 ?: Importance.HIGH.importanceOver26
        ).apply {
            importance = notification.priority?.importanceOver26 ?: Importance.HIGH.importanceOver26
            description = ch.channelDescription
            enableLights(true)
            lightColor = Color.RED
            setShowBadge(true)
        }
        if (notification.soundUri != null) {
            channel.setSound(notification.soundUri, AudioAttributes.Builder().build())
        }

        mNotificationManager.createNotificationChannel(channel)
    }

    @TargetApi(value = Build.VERSION_CODES.O)
    private fun getNotificationBuilder26(notification: Notification): NotificationCompat.Builder {
        this.createNotificationChannel(notification)
        return NotificationCompat.Builder(application, notification.channel!!.channelId)
                .let {
                    if (notification.smallIconRes == 0)
                        it else it.setSmallIcon(notification.smallIconRes)
                }
                .let {
                    if (notification.title == null)
                        it else it.setContentTitle(notification.title)
                }
                .let {
                    if (notification.content == null)
                        it else it.setContentText(notification.content)
                }
                .setAutoCancel(notification.autoCancel)
    }

    /**
     * 更新Notification
     */
    fun updateNotification(notification: Notification) {
        this.sendNotification(notification)
    }

    /**
     * 删除Notification
     */
    fun deleteNotification(notification: Notification) {
        this.deleteNotification(notification.id)
    }

    /**
     * 删除指定Id的Notification
     */
    fun deleteNotification(notificationId: Int) {
        mNotificationManager.cancel(notificationId)
    }

    /**
     * 清除所有的Notification
     */
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

package com.github.qingmei2

import com.qingmei2.notificationdemo.notify.entity.Notification

interface INotificationFactory {

    fun provideNotification(): Notification
}

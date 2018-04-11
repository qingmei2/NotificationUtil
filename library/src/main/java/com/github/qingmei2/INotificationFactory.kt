package com.github.qingmei2

import com.github.qingmei2.entity.Notification

interface INotificationFactory {

    fun provideNotification(): Notification
}

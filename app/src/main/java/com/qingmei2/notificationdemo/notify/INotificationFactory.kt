package com.qingmei2.notificationdemo.notify

import com.qingmei2.notificationdemo.notify.entity.Notification

interface INotificationFactory {

    fun provide(): Notification
}

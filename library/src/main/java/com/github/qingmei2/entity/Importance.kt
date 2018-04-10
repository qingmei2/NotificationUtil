package com.qingmei2.notificationdemo.notify.entity

import android.annotation.TargetApi
import android.os.Build

import android.support.v4.app.NotificationCompat.*
import android.support.v4.app.NotificationManagerCompat.*

/**
 * 通知的重要性/优先级
 */
@TargetApi(value = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
enum class Importance private constructor(val importanceBelow25: Int,
                                          @get:TargetApi(value = Build.VERSION_CODES.O) val importanceOver26: Int) {

    /**
     * No sound and does not appear in the status bar
     */
    LOW(PRIORITY_MIN, IMPORTANCE_MIN),

    /**
     * No sound
     */
    MEDIUM(PRIORITY_LOW, IMPORTANCE_LOW),

    /**
     * Makes a sound
     */
    HIGH(PRIORITY_DEFAULT, IMPORTANCE_DEFAULT),

    /**
     * Makes a sound and appears as a heads-up notification
     */
    URGENT(PRIORITY_MAX, IMPORTANCE_HIGH)
}

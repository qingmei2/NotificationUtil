package com.qingmei2.notificationdemo.notify.entity

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.support.annotation.DrawableRes
import android.support.annotation.RawRes
import android.support.v4.app.NotificationCompat

class Notification {

    var id: Int = 0

    /**
     * 通知的小图标资源id
     */
    @DrawableRes
    var smallIconRes: Int = 0

    /**
     * 通知的title
     *
     * @see NotificationCompat.Builder.setContentTitle
     */
    var title: String? = null

    /**
     * 通知的内容
     *
     * @see NotificationCompat.Builder.setContentText
     */
    var content: String? = null

    /**
     * 用户点击通知，是否自动cancel掉,默认为true
     *
     * @see NotificationCompat.Builder.setAutoCancel
     */
    var autoCancel = true

    /**
     * 要跳转的&&返回栈的Activity
     *
     *
     * 你需要在Manifest文件中对Activity的返回栈做出如下配置：
     * <pre>
     * `
     * < activity
     * android:name=".SecondActivity"
     * android:parentActivityName=".ThirdActivity">
     * < meta-data
     * android:name="android.support.PARENT_ACTIVITY"
     * android:value=".ThirdActivity" />
     * < /activity>
    ` *
    </pre> *
     *
     * @see android.content.Intent
     *
     * @see android.support.v4.app.TaskStackBuilder.addParentStack
     */
    var backStackActivity: Class<out Activity>? = null

    /**
     * 您可以根据需要设置通知的优先级。优先级充当一个提示，提醒设备 UI 应该如何显示通知。
     *
     *
     * @see Importance.URGENT
     *
     * @see Importance.HIGH
     *
     * @see Importance.MEDIUM
     *
     * @see Importance.LOW
     */
    var priority = Importance.HIGH

    /**
     * 设置通知背景音乐
     */
    @RawRes
    var soundRes: Int = 0

    /**
     * 是否属于浮动通知
     *
     *
     * 可能触发浮动通知的条件示例包括：
     *
     *
     * 用户的 Activity 处于全屏模式中（应用使用 fullScreenIntent），或者
     *
     *
     * 通知具有较高的优先级并使用铃声或振动
     */
    var floatMode = false

    /**
     * 负责传值给其他界面
     */
    var onIntentInit: OnIntentInit? = null

    /**
     * 通知渠道
     *
     * 兼容Android O以上版本的更新
     */
    var channel: Channel? = Channel.DEFAULT_INSTANCE

    private constructor()

    private constructor(target: Builder) {
        this.id = target.id
        this.smallIconRes = target.smallIconRes
        this.title = target.title
        this.content = target.content
        this.autoCancel = target.autoCancel
        this.backStackActivity = target.backStackActivity
        this.priority = target.priority
        this.soundRes = target.soundRes
        this.floatMode = target.floatMode
        if (target.onIntentInit != null)
            this.onIntentInit = target.onIntentInit
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && target.channel != null) {
            this.channel = analyseChannelConfig(target)
        }
    }

    @TargetApi(value = Build.VERSION_CODES.O)
    private fun analyseChannelConfig(target: Builder): Channel? {
        return target.channel
    }

    class Builder {

        var id: Int = 0

        var channel: Channel? = null

        @DrawableRes
        var smallIconRes: Int = 0

        var title: String? = null

        var content: String? = null

        var autoCancel = true

        var backStackActivity: Class<out Activity>? = null

        var priority = Importance.HIGH

        @RawRes
        var soundRes: Int = 0

        var floatMode = false

        var onIntentInit: OnIntentInit? = null

        /**
         * 通知的id,请保证通知id的唯一性（以用于实现更新、删除指定通知等功能）
         *
         * @see android.app.NotificationManager.notify
         */
        fun withId(id: Int): Builder {
            this.id = id
            return this
        }

        @TargetApi(value = Build.VERSION_CODES.O)
        fun withChannel(channel: Channel): Builder {
            this.channel = channel
            return this
        }

        fun withSmallIconRes(smallIconRes: Int): Builder {
            this.smallIconRes = smallIconRes
            return this
        }

        fun withTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun withContent(content: String): Builder {
            this.content = content
            return this
        }

        fun withAutoCancel(autoCancel: Boolean): Builder {
            this.autoCancel = autoCancel
            return this
        }

        fun withBackStackActivity(backStackActivity: Class<out Activity>): Builder {
            this.backStackActivity = backStackActivity
            return this
        }

        fun withPriority(priority: Importance): Builder {
            this.priority = priority
            return this
        }

        fun withSoundRes(soundRes: Int): Builder {
            this.soundRes = soundRes
            return this
        }

        fun withOnIntentInit(onIntentInit: OnIntentInit): Builder {
            this.onIntentInit = onIntentInit
            return this
        }

        /**
         * 必须设置：
         *
         *
         * 用户的 Activity 处于全屏模式中（应用使用 fullScreenIntent），或者
         *
         *
         * 通知具有较高的优先级并使用铃声或振动
         */
        fun withFloatMode(floatMode: Boolean): Builder {
            this.floatMode = floatMode
            return this
        }

        fun build(): Notification {
            return Notification(this)
        }
    }

    interface OnIntentInit {

        fun onIntentInit(intent: Intent): Intent
    }
}

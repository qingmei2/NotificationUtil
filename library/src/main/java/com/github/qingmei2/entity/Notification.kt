package cn.com.fenrir_inc.module_core.notify.entity

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.annotation.DrawableRes
import android.support.v4.app.NotificationCompat

class Notification private constructor(target: Builder) {

    var id: Int = 0

    /**
     * 通知的小图标资源id
     */
    @DrawableRes
    var smallIconRes: Int = 0

    /**
     * 通知的大图标资源id
     */
    @DrawableRes
    var bigIconRes: Int = 0

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
    var priority: Importance? = Importance.HIGH

    /**
     * 设置通知背景音乐
     */
    var soundUri: Uri? = null

    /**
     * 负责传值给其他界面
     */
    var onIntentInitListener: ((intent: Intent) -> Intent)? = null

    /**
     * 通知渠道
     *
     * 兼容Android O以上版本的更新
     */
    var channel: Channel? = Channel.DEFAULT_INSTANCE

    @TargetApi(value = Build.VERSION_CODES.O)
    private fun analyseChannelConfig(target: Builder): Channel? {
        return target.channel
    }

    class Builder {

        var id: Int = 0

        var channel: Channel? = null

        @DrawableRes
        var smallIconRes: Int = 0

        @DrawableRes
        var bigIconRes: Int = 0

        var title: String? = null

        var content: String? = null

        var autoCancel = true

        var backStackActivity: Class<out Activity>? = null

        var priority: Importance? = Importance.HIGH

        var soundUri: Uri? = null

        var onIntentInitListener: ((intent: Intent) -> Intent)? = null

        /**
         * 通知的id,请保证通知id的唯一性（以用于实现更新、删除指定通知等功能）
         *
         * @see android.app.NotificationManager.notify
         */
        fun withId(id: Int): Builder = apply {
            this.id = id
        }

        @TargetApi(value = Build.VERSION_CODES.O)
        fun withChannel(channel: Channel?): Builder = apply {
            this.channel = channel
        }

        fun withSmallIconRes(smallIconRes: Int): Builder = apply {
            this.smallIconRes = smallIconRes
        }

        fun withBigIconRes(bigIconRes: Int): Builder = apply {
            this.bigIconRes = bigIconRes
        }

        fun withTitle(title: String?): Builder = apply {
            this.title = title
        }

        fun withContent(content: String?): Builder = apply {
            this.content = content
        }

        fun withAutoCancel(autoCancel: Boolean): Builder = apply {
            this.autoCancel = autoCancel
        }

        fun withBackStackActivity(backStackActivity: Class<out Activity>?): Builder = apply {
            this.backStackActivity = backStackActivity
        }

        fun withPriority(priority: Importance?): Builder = apply {
            this.priority = priority
        }

        fun withSoundUri(soundUri: Uri?): Builder = apply {
            this.soundUri = soundUri
        }

        fun withOnIntentInitListener(func: ((intent: Intent) -> Intent)?): Builder {
            this.onIntentInitListener = func
            return this
        }

        fun build(): Notification {
            return Notification(this)
        }
    }

    init {
        this.id = target.id
        this.smallIconRes = target.smallIconRes
        this.title = target.title
        this.content = target.content
        this.autoCancel = target.autoCancel
        this.backStackActivity = target.backStackActivity
        this.priority = target.priority
        this.soundUri = target.soundUri
        this.bigIconRes = target.bigIconRes
        if (target.onIntentInitListener != null)
            this.onIntentInitListener = target.onIntentInitListener
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && target.channel != null) {
            this.channel = analyseChannelConfig(target)
        }
    }
}

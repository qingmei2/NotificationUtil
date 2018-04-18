package cn.com.fenrir_inc.module_core.notify.entity

import android.annotation.TargetApi
import android.os.Build

@TargetApi(value = Build.VERSION_CODES.O)
class Channel {

    var channelId = "default"

    var channelName = "默认通知"

    var channelDescription = ""

    constructor()

    constructor(channelId: String, channelName: String, channelDescription: String) {
        this.channelId = channelId
        this.channelName = channelName
        this.channelDescription = channelDescription
    }

    companion object {

        internal val DEFAULT_INSTANCE = Channel()
    }
}

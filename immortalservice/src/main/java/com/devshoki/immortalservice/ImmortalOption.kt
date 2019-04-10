package com.devshoki.immortalservice

import android.app.Notification
import java.util.concurrent.TimeUnit

class ImmortalOption {
    var onStart: (() -> Unit)? = null
    var timeCycle: TimeCycle = TimeCycle(1, TimeUnit.MINUTES)
    var actionBlock: (() -> Unit)? = null
    var createNotification: (() -> Notification)? = null

    data class TimeCycle(
            var value: Long,
            var timeUnit: TimeUnit
    )
}


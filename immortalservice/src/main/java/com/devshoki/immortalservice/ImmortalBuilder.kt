package com.devshoki.immortalservice

import android.app.Notification
import java.util.concurrent.TimeUnit

class ImmortalBuilder {
    private var onStart: (() -> Unit)? = null
    private var timeCycle: Immortal.TimeCycle = Immortal.TimeCycle(1, TimeUnit.MINUTES)
    private var actionBlock: (() -> Unit)? = null
    private var createNotification: (() -> Notification)? = null

    companion object {
        const val NO_ACTION = -30L
    }

    fun notWorking() {
        timeCycle = Immortal.TimeCycle(NO_ACTION, TimeUnit.MINUTES)
    }

    fun onServiceStart(block: (() -> Unit)? = null) {
        onStart = block
    }

    fun timeCycle(value: Long, timeUnit: TimeUnit) {
        timeCycle = Immortal.TimeCycle(value, timeUnit)
    }

    fun actionBlock(block: (() -> Unit)) {
        actionBlock = block
    }

    fun notification(block: () -> Notification) {
        createNotification = block
    }

    fun builder(): Immortal {
        return Immortal(
                onStart = onStart,
                actionBlock = actionBlock,
                createNotification = createNotification,
                timeCycle = timeCycle
        )
    }
}


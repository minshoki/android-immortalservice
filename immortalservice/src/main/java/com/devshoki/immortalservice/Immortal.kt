package com.devshoki.immortalservice

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.devshoki.immortalservice.core.RealImmortalService
import java.util.concurrent.TimeUnit

class Immortal(
        private var onStart: (() -> Unit)? = null,
        private var timeCycle: TimeCycle = TimeCycle(1, TimeUnit.MINUTES),
        private var actionBlock: (() -> Unit)? = null,
        private var createNotification: (() -> Notification)? = null
) {
    data class TimeCycle(
            var value: Long,
            var timeUnit: TimeUnit
    )

    fun sleepWithTimeCycle() {
        timeCycle.timeUnit.sleep(timeCycle.value)
    }

    fun action() {
        actionBlock?.let { it() }
    }

    fun onStart() {
        onStart?.let { it() }
    }

    fun notification(): Notification? {
        return createNotification?.let { it() }
    }
}

fun Context.startImmortal(
        lifecycleOwner: LifecycleOwner,
        block: ImmortalBuilder.() -> Unit
): Intent? {

    lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestory() {
            Log.e("shokitest", "destory observer")
        }
    })

    val builder = ImmortalBuilder()
    builder.block()

    RealImmortalService.IMMORTAL = builder.builder()

    return if (RealImmortalService.SERVICE_INTENT == null) {
        val intent = Intent(this, RealImmortalService::class.java)
        startService(intent)
        intent
    } else {
        RealImmortalService.SERVICE_INTENT
    }
}

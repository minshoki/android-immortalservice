package com.devshoki.immortalservice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
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

    fun isNotWorking(): Boolean {
        return timeCycle.value == ImmortalBuilder.NO_ACTION
    }

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


fun Context.makeEmptyNotificationBuilder(to: Class<*>): NotificationCompat.Builder {
    val appInfo = packageManager.getApplicationInfo(
            packageName,
            PackageManager.GET_META_DATA
    )
    val bundle = appInfo.metaData
    val channelId = if(bundle != null) {
        bundle.getString("immortal_service_default_channel_id")
    } else {
        "immortal_default_channel_id"
    }
    val channelName = if(bundle != null) {
        bundle.getString("immortal_service_default_channel_name")
    } else {
        "immortal_default_channel_name"
    }


    val builder = NotificationCompat.Builder(this, channelId)
    builder.setSmallIcon(R.drawable.ic_stat_name)
    builder.setContentTitle(null)
    builder.setContentText(null)
    val notificationIntent = Intent(this, to)
    val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
    builder.setContentIntent(pendingIntent)

    val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        manager.createNotificationChannel(NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT))
    }

    return builder
}

fun Context.startImmortal(
        lifecycleOwner: LifecycleOwner,
        block: ImmortalBuilder.() -> Unit
): Intent? {
    val builder = ImmortalBuilder()
    builder.block()

    RealImmortalService.IMMORTAL = builder.builder()

    var intent  = if (RealImmortalService.SERVICE_INTENT == null) {
        val tempIntent = Intent(this, RealImmortalService::class.java)
        startService(tempIntent)
        tempIntent
    } else {
        RealImmortalService.SERVICE_INTENT
    }

    lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestory() {
            Log.e("shokitest", "destory observer")
            if(intent != null) {
                stopService(intent)
                intent = null
            }
        }
    })
    return intent
}

fun Context.startImmortal(
        block: ImmortalBuilder.() -> Unit
): Intent? {
    val builder = ImmortalBuilder()
    builder.block()

    RealImmortalService.IMMORTAL = builder.builder()

    return if (RealImmortalService.SERVICE_INTENT == null) {
        val tempIntent = Intent(this, RealImmortalService::class.java)
        startService(tempIntent)
        tempIntent
    } else {
        RealImmortalService.SERVICE_INTENT
    }
}

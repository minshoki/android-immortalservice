package com.devshoki.immortalservice.core

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.devshoki.immortalservice.Immortal
import com.devshoki.immortalservice.ImmortalBuilder
import java.util.*

class RealImmortalService : Service() {

    private var mainThread: Thread? = null

    companion object {
        var SERVICE_INTENT: Intent? = null
        var IMMORTAL: Immortal? = null
    }


    override fun onBind(intent: Intent?): IBinder? = null
    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        SERVICE_INTENT = intent

        //block()

        IMMORTAL?.let { o -> o.onStart() }

        mainThread = Thread(Runnable {

            var isRun = true
            while (isRun) {
                try {
                    IMMORTAL?.let { o ->
                        o.sleepWithTimeCycle()
                        o.action()
                    }
                } catch (e: InterruptedException) {
                    isRun = false
                    e.printStackTrace()
                }
            }
        })

        mainThread?.start()

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        SERVICE_INTENT = null

        setAlarmTimer()

        Thread.currentThread().interrupt()

        if (mainThread != null) {
            mainThread?.interrupt()
            mainThread = null
        }
    }

    private fun setAlarmTimer() {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            add(Calendar.SECOND, 1)
        }

        val intent = Intent(this, ImmortalAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}
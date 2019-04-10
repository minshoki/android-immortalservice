package com.devshoki.immortalservice.core

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.app.NotificationManager
import android.app.NotificationChannel
import android.os.Build
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import com.devshoki.immortalservice.R



class RestartService: Service() {
    override fun onBind(intent: Intent?): IBinder? = null
    override fun onUnbind(intent: Intent?): Boolean { return super.onUnbind(intent) }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        RealImmortalService.OPTIONS?.let { o ->
            o.createNotification?.let { n ->
                startForeground(9, n())
            }
        }
        //notification //


        startService(
                Intent(this, RealImmortalService::class.java)
        )

        stopForeground(true)
        stopSelf()
        return START_NOT_STICKY
    }
}
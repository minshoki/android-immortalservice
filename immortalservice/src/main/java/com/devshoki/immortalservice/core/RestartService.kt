package com.devshoki.immortalservice.core

import android.app.Service
import android.content.Intent
import android.os.IBinder


class RestartService: Service() {
    override fun onBind(intent: Intent?): IBinder? = null
    override fun onUnbind(intent: Intent?): Boolean { return super.onUnbind(intent) }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        RealImmortalService.IMMORTAL?.let { o ->
            o.notification()?.let { n ->
                startForeground(9, n)
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
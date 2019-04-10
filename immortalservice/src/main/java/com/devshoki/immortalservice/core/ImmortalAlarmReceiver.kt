package com.devshoki.immortalservice.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class ImmortalAlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context?.startForegroundService(
                    Intent(context, RestartService::class.java)
            )
        } else {
            context?.startService(
                    Intent(context, RealImmortalService::class.java)
            )
        }
    }
}
package com.devshoki.immortalservice.core

import android.content.Intent
import androidx.core.content.ContextCompat.startForegroundService
import android.os.Build
import android.content.BroadcastReceiver
import android.content.Context


class RebootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(
                    Intent(context, RestartService::class.java)
            )
        } else {
            context.startService(
                    Intent(context, RealImmortalService::class.java)
            )
        }
    }
}
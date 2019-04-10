package com.devshoki.immortalserviceexample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.devshoki.immortalservice.startImmortal

class MainActivity : AppCompatActivity() {

    private var serviceIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        serviceIntent = startImmortal(this) {
            onServiceStart {

            }
            actionBlock {
                Log.e("shokitest", "ACTION BLOCK")
            }
            notification {
                val builder = NotificationCompat.Builder(this@MainActivity, "default")
                builder.setSmallIcon(com.devshoki.immortalservice.R.drawable.notification_icon_background)
                builder.setContentTitle(null)
                builder.setContentText(null)
                val notificationIntent = Intent(this@MainActivity, MainActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(this@MainActivity, 0, notificationIntent, 0)
                builder.setContentIntent(pendingIntent)

                val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    manager.createNotificationChannel(NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT))
                }

                builder.build()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(serviceIntent != null) {
            stopService(serviceIntent)
            serviceIntent = null
        }
    }
}

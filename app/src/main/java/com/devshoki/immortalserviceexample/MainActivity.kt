package com.devshoki.immortalserviceexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.devshoki.immortalservice.makeEmptyNotificationBuilder
import com.devshoki.immortalservice.startImmortal

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startImmortal(this) {
            notWorking()
            onServiceStart {

            }
            actionBlock {
                Log.e("shokitest", "ACTION BLOCK")
            }
            notification {
                makeEmptyNotificationBuilder(MainActivity::class.java).build()
            }
        }
    }
}

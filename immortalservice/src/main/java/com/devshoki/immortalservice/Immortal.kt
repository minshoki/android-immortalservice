package com.devshoki.immortalservice

import android.content.Context
import android.content.Intent
import com.devshoki.immortalservice.core.RealImmortalService

object Immortal {



}

fun Context.startImmortal(
        block: (ImmortalOption.() -> Unit)? = null
): Intent? {

    val option = ImmortalOption().apply {
        block?.let { it() }
    }

    RealImmortalService.OPTIONS = option

    return if(RealImmortalService.SERVICE_INTENT == null) {
        var intent = Intent(this, RealImmortalService::class.java)
        startService(intent)
        intent
    } else {
        RealImmortalService.SERVICE_INTENT
    }
}

package com.sportsmax.swrvepush_android

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.util.Log
import com.applicaster.app.CustomApplication
import com.swrve.sdk.SwrveInitMode
import com.swrve.sdk.SwrveNotificationConfig
import com.swrve.sdk.SwrveSDK
import com.swrve.sdk.config.SwrveConfig
import com.swrve.sdk.config.SwrveStack

class SwrveApplication: CustomApplication(){
    override fun onCreate() {
        super.onCreate()
        try {
            val config = SwrveConfig()
            config.initMode = SwrveInitMode.AUTO
            config.selectedStack = SwrveStack.EU

            var channel: NotificationChannel? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channel = NotificationChannel(
                    "123",
                    "SportsMax channel",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                if (getSystemService(Context.NOTIFICATION_SERVICE) != null) {
                    val notificationManager =
                        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.createNotificationChannel(channel)
                }
            }

            val notificationConfig = SwrveNotificationConfig.Builder(R.drawable.cast_icon_bg, R.drawable.cast_icon_bg, channel)
            config.notificationConfig = notificationConfig.build()


            val isDebuggable = 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE

            val apiKey = if (isDebuggable){
                "eMIb7GMt6Y60SgkeGJSp"
            }else{
                "0ZXSUkcDyeOkIRjfyPx"
            }

            val appId = if(isDebuggable){
                6883
            }else{
                6845
            }

            config.setNotificationListener { pushJson ->
                Log.wtf("Received push", "of body: " + pushJson.toString(1))
            }
            SwrveSDK.createInstance(this, appId, apiKey, config)
        } catch (ex: IllegalArgumentException) {
            Log.e("SwrveDemo", "Could not initialize the Swrve SDK", ex)
        }
    }
}
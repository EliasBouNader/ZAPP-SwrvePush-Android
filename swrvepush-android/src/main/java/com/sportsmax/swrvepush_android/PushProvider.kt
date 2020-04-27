package com.sportsmax.swrvepush_android

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.util.Log
import com.applicaster.app.CustomApplication
import com.applicaster.plugin_manager.push_plugin.PushContract
import com.applicaster.plugin_manager.push_plugin.helper.PushPluginsType
import com.applicaster.plugin_manager.push_plugin.listeners.PushTagLoadedI
import com.applicaster.plugin_manager.push_plugin.listeners.PushTagRegistrationI
import com.swrve.sdk.SwrveNotificationConfig
import com.swrve.sdk.SwrveSDK
import com.swrve.sdk.config.SwrveConfig

class PushProvider : PushContract {

    override fun registerPushProvider(
        context: Context,
        registerID: String
    ) {
        initPushProvider(context)
    }

    override fun initPushProvider(context: Context) {
        val config = SwrveConfig()
        var channel: NotificationChannel? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = NotificationChannel(
                "123",
                "Devapp swrve default channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            if (context.getSystemService(Context.NOTIFICATION_SERVICE) != null) {
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
        val notificationConfig = SwrveNotificationConfig.Builder(
            R.drawable.ic_player_logo,
            R.drawable.ic_player_logo,
            channel
        )

        config.notificationConfig = notificationConfig.build()


        val appId = PluginConfigurationHelper.getConfigurationValue(SWRVE_ACCOUNT_ID)?.toInt() ?: 0
        val isDebuggable = 0 != context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE

        val apiKey = if (isDebuggable){
            PluginConfigurationHelper.getConfigurationValue(SWRVE_SANDBOX_KEY)
        }else{
            PluginConfigurationHelper.getConfigurationValue(SWRVE_PRODUCTION_KEY)
        }

        config.setNotificationListener { pushJson ->
            Log.wtf("Received push", "of body: " + pushJson.toString(1))
        }
        SwrveSDK.createInstance(context.applicationContext as Application?, appId, apiKey, config)
    }

    override fun setPluginParams(params: Map<*, *>?) {

    }
    override fun addTagToProvider(
        context: Context,
        tag: List<String>,
        pushTagRegistrationListener: PushTagRegistrationI
    ) {
    }

    override fun removeTagToProvider(
        context: Context,
        tag: List<String>,
        pushTagRegistrationListener: PushTagRegistrationI
    ) {
    }

    override fun getPluginType(): PushPluginsType {
        return PushPluginsType.applicaster
    }

    override fun getTagList(
        context: Context,
        listener: PushTagLoadedI
    ) {
    }
}
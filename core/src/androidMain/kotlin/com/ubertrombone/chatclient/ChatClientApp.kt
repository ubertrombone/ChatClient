package com.ubertrombone.chatclient

import android.app.Application
import android.content.Context
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class ChatClientApp : Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        // TODO: Napier setup functions could be expect/actual so that Napier can be just a Core dependency
        Napier.base(DebugAntilog())
    }
}
package com.joshrose

import MainView
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.preference.PreferenceManager
import com.arkivanov.decompose.defaultComponentContext
import com.russhwolf.settings.SharedPreferencesSettings
import component.root.DefaultRootComponent
import settings.SettingsRepository

class MainActivity : ComponentActivity() {

    private val settingsRepository by lazy {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val settings = SharedPreferencesSettings(sharedPrefs)
        SettingsRepository(settings)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = DefaultRootComponent(
            componentContext = defaultComponentContext(),
            settingsRepository = settingsRepository
        )

        setContent {
            MainView(root)
        }
    }
}
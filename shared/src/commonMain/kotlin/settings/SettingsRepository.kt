package settings

import com.russhwolf.settings.Settings

class SettingsRepository(private val settings: Settings) {
    val token = SettingConfig(settings, "TOKEN", "")
    val username = SettingConfig(settings, "USERNAME", "")
    val password = SettingConfig(settings, "PASSWORD", "")
    val rememberMe = SettingConfig(settings, "REMEMBER_ME", false)

    fun clear() = settings.clear()
}

class SettingConfig<T>(
    private val settings: Settings,
    val key: String,
    private val defaultValue: T
) {
    private fun getValue(settings: Settings, key: String, defaultValue: T): String = when (defaultValue) {
        is String -> settings.getString(key, defaultValue)
        is Int -> settings.getInt(key, defaultValue).toString()
        else -> throw ClassCastException("Type could not be inferred. Value must be String or Int.")
    }

    private fun setValue(settings: Settings, key: String, value: T) {
        when (value) {
            is String -> settings.putString(key, value)
            is Int -> settings.putInt(key, value)
            else -> throw ClassCastException("Type could not be inferred. Value must be String or Int.")
        }
    }

    fun remove() = settings.remove(key)
    fun exists(): Boolean = settings.hasKey(key)

    fun get(): String = getValue(settings, key, defaultValue)
    fun set(value: T) = try {
        setValue(settings, key, value)
        true
    } catch (e: Exception) { false }

    override fun toString() = key
}
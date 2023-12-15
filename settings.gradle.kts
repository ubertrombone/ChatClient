@file:Suppress("UnstableApiUsage")

rootProject.name = "ChatClient"

include(":androidApp")
include(":shared")
include(":desktopApp")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://maven.pkg.jetbrains.space/public/p/amper/amper")
    }

    plugins {
        id("app.cash.sqldelight") apply false
    }
}

plugins {
    id("org.jetbrains.amper.settings.plugin").version("0.1.4")
}

plugins.apply("org.jetbrains.amper.settings.plugin")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://maven.pkg.jetbrains.space/public/p/amper/amper")
    }
}

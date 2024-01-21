@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.library)
    alias(libs.plugins.sqldelight)
}

sqldelight {
    databases {
        create("ChatDatabase") {
            packageName.set("com.ubertrombone.chat")
            schemaOutputDirectory.set(file("build/sqldelight"))
        }
    }
    linkSqlite.set(true)
}

kotlin {
    androidTarget()

    jvm("desktop") {
        jvmToolchain(17)
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    compilerOptions {
        languageVersion.set(KotlinVersion.KOTLIN_1_9)
    }

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            api(libs.aakira.napier)
            implementation(libs.bundles.kotlinx.common)
            implementation(libs.sqldelight.coroutinesExtensions)
        }

        androidMain.dependencies {
            implementation(libs.kotlinx.kotlinxCoroutinesAndroid)
            implementation(libs.android.driver)
        }

        desktopMain.dependencies {
            implementation(libs.kotlinx.kotlinxCoroutinesSwing)
            implementation(libs.sqlite.driver)
        }

        iosMain.dependencies {
            implementation(libs.native.driver)
        }
    }
}

android {
    namespace = "com.ubertrombone.coreData"
    compileSdk = libs.versions.compileSdk.get().toInt()
}
@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.library)
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
            api(project(":coreData"))
            api(libs.aakira.napier)
            api(libs.decompose.decompose)
            api(libs.decompose.extensionsCompose)
            api(libs.multiplatformSettings)
            implementation(libs.bundles.ktor.common)
            implementation(libs.bundles.kotlinx.common)
        }

        androidMain.dependencies {
            implementation(libs.ktor.ktorClientOkhttp)
            implementation(libs.kotlinx.kotlinxCoroutinesAndroid)
        }

        desktopMain.dependencies {
            implementation(libs.ktor.ktorClientOkhttp)
            implementation(libs.kotlinx.kotlinxCoroutinesSwing)
        }

        iosMain.dependencies {
            implementation(libs.ktor.ktorClientDarwin)
        }
    }
}

android {
    namespace = "com.ubertrombone.core"
    compileSdk = libs.versions.compileSdk.get().toInt()
}
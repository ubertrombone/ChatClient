plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    //alias(libs.plugins.kotlin.native.cocoapods) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}
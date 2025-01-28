buildscript {
    val agp_version by extra("8.7.3")
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
//    id("com.android.application") version "8.7.3" apply false
//    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
//    id("com.android.library") version "8.1.4" apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false

    // Add the dependency for the Google services Gradle plugin
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.google.devtools.ksp") version "2.1.0-1.0.29" apply false
//    id("org.jetbrains.kotlin.kapt") version "1.9.24" apply false

    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.androidLibrary) apply false
}
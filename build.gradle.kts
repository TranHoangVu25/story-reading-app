// File: build.gradle.kts (Project-level)

buildscript {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven.google.com") } // THÊM DÒNG NÀY

    }
    dependencies {
        // Android Gradle Plugin
        classpath("com.android.tools.build:gradle:8.1.0")
        // Google Services plugin
        classpath("com.google.gms:google-services:4.3.15")

    }
}




plugins {
    id("com.android.application") version "8.9.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
}

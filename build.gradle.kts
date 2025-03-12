// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.google.dagger.hilt.android") version "2.52" apply false
    id ("androidx.navigation.safeargs") version "2.8.4" apply false
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
   //id("com.google.devtools.ksp") version "2.0.20-1.0.25" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false

}
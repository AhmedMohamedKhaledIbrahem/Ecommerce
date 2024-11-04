plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id ("kotlin-kapt") // Add this
    // id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.ecommerce"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.ecommerce"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }



}

dependencies {

    // ViewModel (Lifecycle)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation (libs.androidx.lifecycle.runtime.ktx)
    // Retrofit and Gson Converter
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    //dagger hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.junit.ktx)
    testImplementation(libs.hilt.android.testing)
    kapt(libs.hilt.android.compiler)
    implementation (libs.androidx.room.ktx)
    implementation (libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation (libs.kotlinx.coroutines.core)
    testImplementation (libs.kotlinx.coroutines.test)
    testImplementation (libs.mockito.core)
    testImplementation (libs.mockito.inline)
    testImplementation (libs.mockito.kotlin)
    testImplementation (libs.kotlin.test)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation (libs.androidx.core.testing)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation (libs.androidx.runner)
    testImplementation (libs.json)



}
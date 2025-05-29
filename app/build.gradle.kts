plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt") // Add this
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
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
    buildFeatures {
        viewBinding = true

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
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    // ViewModel (Lifecycle)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx.v241)

    // Retrofit and Gson Converter
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    //dagger hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.work.runtime.ktx)
    testImplementation(libs.hilt.android.testing)
    kapt(libs.androidx.hilt.compiler)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    annotationProcessor(libs.androidx.hilt.compiler.v100)
    //firebase
    implementation(libs.firebase.messaging)

    implementation(libs.recyclerview.swipedecorator)
    implementation(libs.coil)
    implementation(libs.facebook.shimmer)
    implementation(libs.play.services.location)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.room.paging)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.androidx.junit)

    androidTestImplementation(libs.androidx.runner)
    testImplementation(libs.json)


}
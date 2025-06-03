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
        debug {
            buildConfigField("String", "BASE_URL", "\"http://10.0.0.106/wordpress/\"")
            buildConfigField(
                "String",
                "CUSTOMER_ADDRESS_END_POINT",
                "\"wp-json/custom/v1/customer-address\""
            )
            buildConfigField(
                "String",
                "CUSTOMER_INFO_END_POINT",
                "\"wp-json/custom/v1/customer-info\""
            )
            buildConfigField("String", "TOKEN_END_POINT", "\"wp-json/jwt-auth/v1/token\"")
            buildConfigField("String", "SIGN_UP_END_POINT", "\"wp-json/custom/v1/register\"")
            buildConfigField(
                "String",
                "RESET_PASSWORD_END_POINT",
                "\"wp-json/custom/v1/forget-password\""
            )
            buildConfigField(
                "String",
                "SEND_VERIFICATION_CODE_END_POINT",
                "\"wp-json/custom/v1/verify-email\""
            )
            buildConfigField(
                "String",
                "CHECK_VERIFICATION_STATUS_END_POINT",
                "\"wp-json/custom/v1/check-verification-status\""
            )
            buildConfigField(
                "String",
                "CHANGE_PASSWORD_END_POINT",
                "\"wp-json/custom/v1/change-password\""
            )
            buildConfigField(
                "String",
                "CONFIRM_PASSWORD_RESET_END_POINT",
                "\"wp-json/custom/v1/confirm-change-password\""
            )
            buildConfigField("String", "GET_CART_END_POINT", "\"wp-json/cocart/v2/cart\"")
            buildConfigField(
                "String",
                "ADD_ITEM_TO_CART_END_POINT",
                "\"wp-json/cocart/v2/cart/add-item\""
            )
            buildConfigField(
                "String",
                "REMOVE_ITEM_END_POINT",
                "\"wp-json/cocart/v2/cart/item/{cart_item_key}\""
            )
            buildConfigField("String", "CLEAR_CART_END_POINT", "\"wp-json/cocart/v2/cart/clear\"")
            buildConfigField(
                "String",
                "GET_PRODUCT_CATEGORIES_END_POINT",
                "\"wp-json/custom/v1/product-categories\""
            )
            buildConfigField(
                "String",
                "REMOVE_FCM_TOKEN_END_POINT",
                "\"wp-json/custom/v1/remove-fcm-token\""
            )
            buildConfigField("String", "SAVE_TOKEN_END_POINT", "\"wp-json/fcm/v1/save-token\"")
            buildConfigField("String", "CREATE_ORDER_END_POINT", "\"wp-json/custom/v1/orders\"")
            buildConfigField("String", "GET_ORDERS_END_POINT", "\"wp-json/custom/v1/user-orders\"")
            buildConfigField("String", "GET_PRODUCTS_END_POINT", "\"wp-json/cocart/v2/products\"")
            buildConfigField(
                "String",
                "UPLOAD_IMAGE_PROFILE_END_POINT",
                "\"wp-json/custom/v1/upload\""
            )
            buildConfigField(
                "String",
                "GET_IMAGE_PROFILE_END_POINT",
                "\"wp-json/custom/v1/profile-image\""
            )
            buildConfigField(
                "String",
                "CHECK_USER_NAME_DETAILS_UPDATE_END_POINT",
                "\"wp-json/custom/v1/profile-updated\""
            )
            buildConfigField(
                "String",
                "GET_USER_NAME_DETAILS_END_POINT",
                "\"wp-json/custom/v1/user\""
            )
            buildConfigField(
                "String",
                "UPDATE_USER_NAME_DETAILS_END_POINT",
                "\"wp-json/custom/v1/user-update\""
            )

        }
        release {
            buildConfigField("String", "BASE_URL", "\"http://10.0.0.106/wordpress/\"")
            buildConfigField(
                "String",
                "CUSTOMER_ADDRESS_END_POINT",
                "\"wp-json/custom/v1/customer-address\""
            )
            buildConfigField(
                "String",
                "CUSTOMER_INFO_END_POINT",
                "\"wp-json/custom/v1/customer-info\""
            )
            buildConfigField("String", "TOKEN_END_POINT", "\"wp-json/jwt-auth/v1/token\"")
            buildConfigField("String", "SIGN_UP_END_POINT", "\"wp-json/custom/v1/register\"")
            buildConfigField(
                "String",
                "RESET_PASSWORD_END_POINT",
                "\"wp-json/custom/v1/forget-password\""
            )
            buildConfigField(
                "String",
                "SEND_VERIFICATION_CODE_END_POINT",
                "\"wp-json/custom/v1/verify-email\""
            )
            buildConfigField(
                "String",
                "CHECK_VERIFICATION_STATUS_END_POINT",
                "\"wp-json/custom/v1/check-verification-status\""
            )
            buildConfigField(
                "String",
                "CHANGE_PASSWORD_END_POINT",
                "\"wp-json/custom/v1/change-password\""
            )
            buildConfigField(
                "String",
                "CONFIRM_PASSWORD_RESET_END_POINT",
                "\"wp-json/custom/v1/confirm-change-password\""
            )
            buildConfigField("String", "GET_CART_END_POINT", "\"wp-json/cocart/v2/cart\"")
            buildConfigField(
                "String",
                "ADD_ITEM_TO_CART_END_POINT",
                "\"wp-json/cocart/v2/cart/add-item\""
            )
            buildConfigField(
                "String",
                "REMOVE_ITEM_END_POINT",
                "\"wp-json/cocart/v2/cart/item/{cart_item_key}\""
            )
            buildConfigField("String", "CLEAR_CART_END_POINT", "\"wp-json/cocart/v2/cart/clear\"")
            buildConfigField(
                "String",
                "GET_PRODUCT_CATEGORIES_END_POINT",
                "\"wp-json/custom/v1/product-categories\""
            )
            buildConfigField(
                "String",
                "REMOVE_FCM_TOKEN_END_POINT",
                "\"wp-json/custom/v1/remove-fcm-token\""
            )
            buildConfigField("String", "SAVE_TOKEN_END_POINT", "\"wp-json/fcm/v1/save-token\"")
            buildConfigField("String", "CREATE_ORDER_END_POINT", "\"wp-json/custom/v1/orders\"")
            buildConfigField("String", "GET_ORDERS_END_POINT", "\"wp-json/custom/v1/user-orders\"")
            buildConfigField("String", "GET_PRODUCTS_END_POINT", "\"wp-json/cocart/v2/products\"")
            buildConfigField(
                "String",
                "UPLOAD_IMAGE_PROFILE_END_POINT",
                "\"wp-json/custom/v1/upload\""
            )
            buildConfigField(
                "String",
                "GET_IMAGE_PROFILE_END_POINT",
                "\"wp-json/custom/v1/profile-image\""
            )
            buildConfigField(
                "String",
                "CHECK_USER_NAME_DETAILS_UPDATE_END_POINT",
                "\"wp-json/custom/v1/profile-updated\""
            )
            buildConfigField(
                "String",
                "GET_USER_NAME_DETAILS_END_POINT",
                "\"wp-json/custom/v1/user\""
            )
            buildConfigField(
                "String",
                "UPDATE_USER_NAME_DETAILS_END_POINT",
                "\"wp-json/custom/v1/user-update\""
            )
            buildConfigField(
                "String", "customer-address-end-point",
                "\"wp-json/custom/v1/customer-address\""
            )
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
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
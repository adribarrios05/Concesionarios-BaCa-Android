plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id ("androidx.navigation.safeargs.kotlin")
    kotlin("plugin.serialization") version "2.0.21"
    id("com.google.gms.google-services")
    id("org.jetbrains.dokka") version "1.9.10"
}

android {
    namespace = "com.example.concesionariosbaca"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.concesionariosbaca"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    // Core AndroidX Libraries
    implementation (libs.androidx.core.ktx.v1130)
    implementation (libs.androidx.appcompat)
    implementation (libs.androidx.activity.activity.ktx3)
    implementation (libs.androidx.constraintlayout)
    implementation(libs.androidx.fragment.ktx)

    // Lifecycle (ViewModel & LiveData)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.lifecycle.viewmodel.compose3)
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.lifecycle.runtime.ktx3)

    // Room Database
    implementation (libs.androidx.room.runtime)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.legacy.support.v4)
    kapt (libs.androidx.room.room.compiler3)
    implementation (libs.androidx.room.ktx)

    // Networking - Retrofit and GSON
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    // Hilt Dependency Injection
    implementation (libs.hilt.android)
    kapt (libs.hilt.compiler)

    // Coil (Image Loading Library)
    implementation (libs.coil)
    implementation (libs.coil.base)

    // Testing
    testImplementation (libs.junit)
    androidTestImplementation (libs.androidx.junit)
    androidTestImplementation (libs.androidx.espresso.core)

    // Kotlin Script Runtime
    implementation (libs.kotlin.script.runtime)

    // Jetpack Compose integration
    implementation(libs.androidx.navigation.compose)

    // Views/Fragments integration
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    // Feature module support for Fragments
    implementation(libs.androidx.navigation.dynamic.features.fragment)

    // Testing Navigation
    androidTestImplementation(libs.androidx.navigation.testing)

    // JSON serialization library, works with the Kotlin serialization plugin
    implementation(libs.kotlinx.serialization.json)

    //Material
    implementation (libs.material)

    //DataStore
    implementation (libs.androidx.datastore.preferences)

    //Camera
    implementation (libs.androidx.camera.core)
    implementation (libs.androidx.camera.camera2)
    implementation (libs.androidx.camera.lifecycle)
    implementation (libs.androidx.camera.view)
    implementation (libs.androidx.camera.extensions)

    //Google maps
        implementation(libs.play.services.maps)
        implementation(libs.play.services.location)

    //Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation (libs.firebase.auth.ktx)
    implementation (libs.firebase.firestore.ktx)

    //Worker manager
    implementation(libs.androidx.work.runtime.ktx)
}

kapt{
    correctErrorTypes = true
}

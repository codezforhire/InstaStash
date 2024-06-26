plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.instastash"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.instastash"
        minSdk = 24
        targetSdk = 34
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)


    implementation (libs.firebase.auth.v2100)
    //implementation ("com.google.firebase:firebase-storage:21.0.0")
    implementation("com.google.firebase:firebase-firestore:21.0.0")



    implementation("com.google.android.gms:play-services-vision:20.1.3")
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.17")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation(libs.camera.core)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


    implementation ("androidx.room:room-runtime:2.5.1") // Replace with the latest version
    annotationProcessor ("androidx.room:room-compiler:2.5.1") // Replace with the latest version




    implementation ("com.google.mlkit:barcode-scanning:17.2.0")
    implementation ("androidx.camera:camera-core:1.1.0")
    implementation ("androidx.camera:camera-camera2:1.3.3")
    implementation ("androidx.camera:camera-lifecycle:1.3.3")
    implementation ("androidx.camera:camera-view:1.3.3")
    implementation ("com.google.mlkit:barcode-scanning:17.2.0")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation ("com.google.zxing:core:3.3.0")





}


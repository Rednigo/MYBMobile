plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
//    id("kotlin-kapt")
//    id("com.android.application")
//    id("org.getbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.myb"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myb"
        minSdk = 26
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.preference)
    implementation(libs.firebase.crashlytics.buildtools)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Room components
    val roomVersion = "2.6.0"
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")

    // Extend Icons
    implementation("androidx.compose.material:material-icons-extended:1.5.4")

    implementation ("androidx.preference:preference-ktx:1.1.1")

//    implementation("androidx.room:room-runtime:$roomVersion")


//    // Coroutines
//    val coroutinesVersion = "1.5.2"
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
//
//    // Lifecycle components
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
//    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.1")
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
}
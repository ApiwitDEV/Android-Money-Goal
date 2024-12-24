plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.overshoot.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    implementation(libs.kotlin.stdlib.jdk7)
    implementation(libs.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.androidx.room.ktx)

    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)

//    kapt("androidx.room:room-compiler:$room_version")
    ksp(libs.androidx.room.compiler)
    
    implementation(libs.firebase.auth.ktx)

    implementation(libs.retrofit)
    implementation(libs.converter.scalars)
    implementation(libs.converter.gson)
    implementation(libs.converter.moshi)
    implementation(libs.moshi)
    implementation(libs.logging.interceptor)
    implementation(libs.ok2curl)

}

// Preferences DataStore (SharedPreferences like APIs)
dependencies {
    implementation(libs.androidx.datastore.preferences)

    // optional - RxJava2 support
    implementation(libs.androidx.datastore.preferences.rxjava2)

    // optional - RxJava3 support
    implementation(libs.androidx.datastore.preferences.rxjava3)
}

// Alternatively - use the following artifact without an Android dependency.
dependencies {
    implementation(libs.androidx.datastore.preferences.core)
}
// Typed DataStore (Typed API surface, such as Proto)
dependencies {
    implementation(libs.androidx.datastore)

    // optional - RxJava2 support
    implementation(libs.androidx.datastore.rxjava2)

    // optional - RxJava3 support
    implementation(libs.androidx.datastore.rxjava3)
}

// Alternatively - use the following artifact without an Android dependency.
dependencies {
    implementation(libs.androidx.datastore.core)
}

dependencies {
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(project.dependencies.platform(libs.koin.bom))
}
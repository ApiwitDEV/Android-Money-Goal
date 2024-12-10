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

    val kotlin_version = "1.5.31"

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version")
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    val room_version = "2.6.1"

    implementation("androidx.room:room-ktx:$room_version")

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

//    kapt("androidx.room:room-compiler:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    
    implementation("com.google.firebase:firebase-auth-ktx:23.1.0")

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
    implementation("com.squareup.moshi:moshi:1.15.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.github.mrmike:ok2curl:0.8.0")

}

// Preferences DataStore (SharedPreferences like APIs)
dependencies {
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // optional - RxJava2 support
    implementation("androidx.datastore:datastore-preferences-rxjava2:1.1.1")

    // optional - RxJava3 support
    implementation("androidx.datastore:datastore-preferences-rxjava3:1.1.1")
}

// Alternatively - use the following artifact without an Android dependency.
dependencies {
    implementation("androidx.datastore:datastore-preferences-core:1.1.1")
}
// Typed DataStore (Typed API surface, such as Proto)
dependencies {
    implementation("androidx.datastore:datastore:1.1.1")

    // optional - RxJava2 support
    implementation("androidx.datastore:datastore-rxjava2:1.1.1")

    // optional - RxJava3 support
    implementation("androidx.datastore:datastore-rxjava3:1.1.1")
}

// Alternatively - use the following artifact without an Android dependency.
dependencies {
    implementation("androidx.datastore:datastore-core:1.1.1")
}

dependencies {
    val koinVersion = "4.0.0"
    implementation("io.insert-koin:koin-core")
    implementation("io.insert-koin:koin-android")
    implementation(project.dependencies.platform("io.insert-koin:koin-bom:$koinVersion"))
}
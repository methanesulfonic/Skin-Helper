plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile = file("C:\\android_keystore\\cancerchecker\\cancerchecker.jks")
            storePassword = "aaaaaaaa"
            keyAlias = "cancerchecker"
            keyPassword = "aaaaaaaa"
        }
    }
    namespace = "com.uca.tflitetest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.uca.tflitetest"
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
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        mlModelBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.firebase:firebase-firestore:25.0.0")
    implementation ("com.google.firebase:firebase-firestore-ktx:25.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("androidx.viewpager2:viewpager2:1.0.0")

    implementation ("com.github.yalantis:ucrop:2.2.8")
    implementation("io.coil-kt:coil:2.6.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation ("org.tensorflow:tensorflow-lite:2.6.0")
    implementation ("org.tensorflow:tensorflow-lite-gpu:2.6.0")
    implementation ("org.tensorflow:tensorflow-lite-support:0.1.0")

    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-firestore:25.0.0")
    implementation ("com.google.firebase:firebase-storage:20.0.0")

    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    implementation ("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.11.0")

    implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation ("androidx.navigation:navigation-fragment-ktx:2.6.0") // Check for the latest version
    implementation ("androidx.navigation:navigation-ui-ktx:2.6.0")

    implementation ("com.github.yalantis:ucrop:2.2.6")
}
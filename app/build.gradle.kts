plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
    alias(libs.plugins.googleAndroidLibrariesMapsplatformSecretsGradlePlugin)
    alias(libs.plugins.jetbrainsKotlinAndroid)

}
android {
    namespace = "com.example.seyahatkolaymobileapp"
    compileSdk = 34
    viewBinding {
        var enabled = true
    }

    defaultConfig {
        applicationId = "com.example.seyahatkolaymobileapp"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildToolsVersion = "34.0.0"
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }

}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.core.ktx)
    implementation(libs.firebase.database.ktx)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.ui.test.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-analytics-ktx:22.0.1")
    implementation("com.google.firebase:firebase-firestore-ktx:25.0.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.7")

}
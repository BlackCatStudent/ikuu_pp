plugins {
    id("com.android.library")
}

android {
    namespace = "com.ikuuvpn.common"
    compileSdk = 31

    defaultConfig {
        minSdk = 24
    consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
}
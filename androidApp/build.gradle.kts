plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "xyz.malefic.mobile.android"
    compileSdk = 36

    defaultConfig {
        applicationId = "xyz.malefic.mobile"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    
    buildFeatures {
        compose = true
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.androidx.activity.compose)
    
    // Required for Compose runtime features
    implementation(libs.compose.foundation)
    
    // Compose UI Tooling for debug builds
    debugImplementation(libs.androidx.compose.ui.tooling)
}

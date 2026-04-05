import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.dokka)
}

kotlin {
    // Android target is automatically created by com.android.kotlin.multiplatform.library plugin
    // Configure JVM target in android {} block below
    
    android {
        namespace = "xyz.malefic.mobile.shared"
        compileSdk = 36
        minSdk = 24
        
        // Set JVM target for Android
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
        
        // Enable resources for Compose Multiplatform
        androidResources {
            enable = true
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.compose.ui.tooling.preview)
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonMain.dependencies {
            implementation(libs.bundles.compose)

            implementation(libs.navigation.compose)
            implementation(libs.lifecycle.runtime.compose)
            implementation(libs.material.icons.core)

            implementation(libs.bundles.ktor)

            implementation(libs.bundles.coil)

            implementation(libs.bundles.malefic)
        }
    }
}

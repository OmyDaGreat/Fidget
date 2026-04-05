import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val user: String by project
val dev: String by project
val mail: String by project
val devURL: String by project
val repo: String by project
val g: String by project
val artifact: String by project
val v: String by project
val desc: String by project
val inception: String by project

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.dokka)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = g
version = v

kotlin {
    // Android target is automatically created by com.android.kotlin.multiplatform.library plugin
    // Configure JVM target in android {} block below
    
    android {
        namespace = "xyz.malefic.fidget"
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

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(g, artifact, v)

    pom {
        name = repo
        description = desc
        inceptionYear = inception
        url = "https://github.com/$user/$repo"
        licenses {
            license {
                name = "MIT License"
                url = "https://mit.malefic.xyz"
            }
        }
        developers {
            developer {
                name = dev
                email = mail
                url = devURL
            }
        }
        scm {
            url = "https://github.com/$user/$repo"
            connection = "scm:git:git://github.com/$user/$repo.git"
            developerConnection = "scm:git:ssh://github.com/$user/$repo.git"
        }
    }
}

dokka {
    pluginsConfiguration.html {
        footerMessage.set("&copy; 2025 $dev <$mail>")
    }
}

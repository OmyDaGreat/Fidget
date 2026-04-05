import org.jetbrains.dokka.gradle.tasks.DokkaGenerateModuleTask

plugins {
    alias(libs.plugins.androidKotlinMultiplatformLibrary) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinxSerialization) apply false
    alias(libs.plugins.vanniktech.mavenPublish) apply false
}

tasks.withType<DokkaGenerateModuleTask>().configureEach {
    outputDirectory.set(layout.buildDirectory.dir("dokka/html"))
}

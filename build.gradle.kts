// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // Using version ref from libs.versions.toml (which uses the same version as the kotlin plugin)
    id("org.jetbrains.kotlin.plugin.serialization") version "${libs.versions.kotlin.get()}" apply false
    // For KSP, you need to find the specific compiler plugin version matching your Kotlin version.
    // The previous version was correct for Kotlin 2.0.21, but it's cleaner to keep it here:
    id("com.google.devtools.ksp") version "2.2.20-2.0.4" apply false
}
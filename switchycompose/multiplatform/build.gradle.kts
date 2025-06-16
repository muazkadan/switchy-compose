@file:OptIn(ExperimentalWasmDsl::class, ExperimentalComposeLibrary::class)

import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "dev.muazkadan"
version = "0.6.1"

kotlin {
    jvm()
    androidTarget {
        publishLibraryVariants("release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    wasmJs { browser() }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "SwitchyCompose"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
        }

        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.uiToolingPreview)
                implementation(compose.materialIconsExtended)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(compose.uiTest)
            }
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

android {
    namespace = "dev.muazkadan.switchycompose"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    // Only sign if signing properties are available (e.g., for Maven Central)
    // This prevents signing issues when building on JitPack
    if (project.hasProperty("signing.keyId") &&
        project.hasProperty("signing.password") &&
        project.hasProperty("signing.secretKeyRingFile")
    ) {
        signAllPublications()
    }

    coordinates(group.toString(), "switchy-compose", version.toString())

    pom {
        name = "Switchy Compose"
        description =
            "A modern, customizable switch component library for Kotlin Multiplatform that provides beautiful animated switches with various styles and configurations."
        inceptionYear = "2025"
        url = "https://github.com/muazkadan/switchy-compose"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "muazkadan"
                name = "Muaz KADAN"
                url = "https://muazkadan.dev/"
            }
        }
        scm {
            url = "https://github.com/muazkadan/switchy-compose"
            connection = "scm:git:git://github.com/muazkadan/switchy-compose.git"
            developerConnection = "scm:git:ssh://github.com/muazkadan/switchy-compose.git"
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
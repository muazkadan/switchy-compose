@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.testing.KotlinJsTest

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "dev.muazkadan"
version = "0.7.2"

kotlin {
    jvm()
    android {
        namespace = "dev.muazkadan.switchycompose"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    // Compose UI tests on web need an executable binary so the Skiko runtime
    // is bundled with the tests (CMP-4906). The published artifact is still a klib.
    js {
        browser()
        binaries.executable()
    }

    wasmJs {
        browser()
        binaries.executable()
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "SwitchyCompose"
            isStatic = true
        }
    }

    // macOS targets
    listOf(
        macosArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "SwitchyCompose"
            isStatic = true
        }
    }

    // NativeSwitch is Material3's Switch on these targets and a native Swing/UIKit view on
    // jvm and iOS. Group them so tests of the Compose implementation (materialSwitchTest)
    // only run where it is used; interop views can't be hosted in headless UI tests.
    applyDefaultHierarchyTemplate {
        common {
            group("materialSwitch") {
                withJs()
                withWasmJs()
                withMacos()
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.compose.material.iconsExtended)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.compose.ui.test)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }

        webMain.dependencies {
            implementation(libs.kotlinx.browser)
        }
    }
}

// Compose UI tests can't initialise Skiko under Kotlin/JS in Compose 1.12 (CMP-4906).
// The fix (compose-multiplatform-core#3316) ships after 1.12.x; run every test again once we
// upgrade. Until then run only the DOM-based tests on JS; all tests still run on wasmJs.
tasks.named<KotlinJsTest>("jsBrowserTest") {
    filter.includeTestsMatching("dev.muazkadan.switchycompose.HtmlSwitchControllerTest")
}

mavenPublishing {
    publishToMavenCentral()

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
    // Compose preview tooling for the IDE, kept off the published API
    "androidRuntimeClasspath"(libs.compose.ui.tooling)
}

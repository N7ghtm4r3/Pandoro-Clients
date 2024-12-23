import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Exe
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Pkg
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.UUID

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization") version "2.0.20"
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    jvm("desktop") {
        compilations.all {
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            this@jvm.compilerOptions {
                jvmTarget.set(JvmTarget.JVM_21)
            }
        }
    }
    // TODO: PLANNED TO BE IMPLEMENTED IN THE NEXT VERSION
    /*listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Pandoro"
            isStatic = true
        }
    }*/
    // TODO: PLANNED TO BE IMPLEMENTED IN THE NEXT VERSION
    @OptIn(ExperimentalWasmDsl::class)
    /*wasmJs {
        moduleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "pandoro.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }*/
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.coil.network.okhttp)
            implementation(libs.androidx.startup.runtime)
            implementation(libs.app.update)
            implementation(libs.app.update.ktx)
            implementation(libs.review)
            implementation(libs.review.ktx)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.material3AdaptiveNavigationSuite)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.precompose)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.equinox.core)
            implementation(libs.equinox.backend)
            implementation(libs.equinox.compose)
            implementation(libs.material3.window.size)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor3)
            implementation(libs.pandorocore)
            implementation(libs.lazyPaginationCompose)
            implementation(libs.kotlinx.datetime)
            implementation(libs.filekit.core)
            implementation(libs.filekit.compose)
            implementation(libs.kmprefs)
            implementation(libs.jetlime)
            implementation (libs.compose.charts)
            implementation("com.squareup.okhttp3:okhttp:4.12.0")
            implementation("com.github.N7ghtm4r3:APIManager:2.2.4")
            implementation("org.json:json:20240303")
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.java)
            implementation(libs.octocatkdu)
        }
    }
}

android {
    namespace = "com.tecknobit.pandoro"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.tecknobit.pandoro"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 15
        versionName = "1.1.0"
    }
    packaging {
        resources {
            excludes += "*/**"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.tecknobit.pandoro.MainKt"
        nativeDistributions {
            targetFormats(Deb, Pkg, Exe)
            modules(
                "java.compiler", "java.instrument", "java.management", "java.net.http", "java.prefs", "java.rmi",
                "java.scripting", "java.security.jgss", "java.sql.rowset", "jdk.jfr", "jdk.unsupported", "jdk.security.auth"
            )
            packageName = "Pandoro"
            packageVersion = "1.1.0"
            version = "1.1.0"
            description = "Pandoro, open source management software"
            copyright = "© 2024 Tecknobit"
            vendor = "Tecknobit"
            licenseFile.set(project.file("src/desktopMain/resources/LICENSE"))
            macOS {
                bundleID = "com.tecknobit.pandoro"
                iconFile.set(project.file("src/desktopMain/resources/logo.icns"))
            }
            windows {
                iconFile.set(project.file("src/desktopMain/resources/logo.ico"))
                upgradeUuid = UUID.randomUUID().toString()
            }
            linux {
                iconFile.set(project.file("src/desktopMain/resources/logo.png"))
                packageName = "com-tecknobit-pandoro"
                debMaintainer = "infotecknobitcompany@gmail.com"
                appRelease = "1.1.0"
                appCategory = "PERSONALIZATION"
                rpmLicenseType = "MIT"
            }
        }
        buildTypes.release.proguard {
            configurationFiles.from(project.file("src/desktopMain/resources/compose-desktop.pro"))
            obfuscate.set(true)
        }
    }
}

// TODO: TO REMOVE IN THE NEXT VERSION (DEPRECATED TRIGGER SEARCH)
configurations.all {
    exclude("commons-logging", "commons-logging")
    // TODO: TO REMOVE IN THE NEXT VERSION (DEPRECATED TRIGGER SEARCH)
    resolutionStrategy {
        force("com.github.N7ghtm4r3:GitHubManager:1.0.1")
    }
}

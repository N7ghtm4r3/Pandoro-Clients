import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Exe
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Pkg
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import org.jetbrains.kotlin.gradle.targets.wasm.nodejs.WasmNodeJsRootExtension
import java.util.UUID

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.dokka)
    kotlin("plugin.serialization") version "2.0.20"
    id("com.github.gmazzo.buildconfig") version "5.7.1"
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_22)
        }
    }

    jvm("desktop") {
        compilations.all {
            this@jvm.compilerOptions {
                jvmTarget.set(JvmTarget.JVM_22)
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Pandoro"
            isStatic = true
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "Pandoro.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.fragment.compose)
            implementation(libs.ktor.client.okhttp)
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
            implementation(libs.navigation.compose)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.equinox.core)
            implementation(libs.equinox.compose)
            implementation(libs.equinox.navigation)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor3)
            implementation(libs.lazyPaginationCompose)
            implementation(libs.kotlinx.datetime)
            implementation(libs.filekit.core)
            implementation(libs.filekit.compose)
            implementation(libs.jetlime)
            implementation (libs.compose.charts)
            implementation(libs.pandorocore)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.octocatkdu)
        }

        wasmJsMain.dependencies {
            implementation(npm("brace-expansion", "2.0.2"))
            implementation(npm("http-proxy-middleware", "2.0.9"))
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
        versionCode = 21
        versionName = "1.2.1"
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
        sourceCompatibility = JavaVersion.VERSION_22
        targetCompatibility = JavaVersion.VERSION_22
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
            packageVersion = "1.2.1"
            version = "1.2.1"
            description = "Pandoro, open source management software"
            copyright = "© 2025 Tecknobit"
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
                appRelease = "1.2.1"
                appCategory = "PERSONALIZATION"
                rpmLicenseType = "APACHE2"
            }
        }
        buildTypes.release.proguard {
            configurationFiles.from(project.file("src/desktopMain/resources/compose-desktop.pro"))
            obfuscate.set(true)
        }
    }
}

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
        moduleName.set("Pandoro")
        outputDirectory.set(layout.projectDirectory.dir("../docs"))
    }

    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
        customAssets = listOf(file("../docs/logo-icon.svg"))
        footerMessage = "(c) 2025 Tecknobit"
    }
}

buildConfig {
    className("AmetistaConfig")
    packageName("com.tecknobit.pandoro")
    buildConfigField<String>(
        name = "HOST",
        value = project.findProperty("host").toString()
    )
    buildConfigField<String?>(
        name = "SERVER_SECRET",
        value = project.findProperty("server_secret").toString()
    )
    buildConfigField<String?>(
        name = "APPLICATION_IDENTIFIER",
        value = project.findProperty("application_id").toString()
    )
    buildConfigField<Boolean>(
        name = "BYPASS_SSL_VALIDATION",
        value = project.findProperty("bypass_ssl_validation").toString().toBoolean()
    )
}

rootProject.the<WasmNodeJsRootExtension>().versions.webpackDevServer.version = "5.2.2"
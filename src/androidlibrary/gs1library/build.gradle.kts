import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    `maven-publish`
}

val libNamespace = "org.gs1.gs1androidlibrary"

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication> (project.name) {
                from(components["release"])
                groupId = libNamespace
                artifactId = project.name
                version = "1.1.2"
            }
        }
    }
}

android {
    namespace = libNamespace
    compileSdk = 36

    defaultConfig {
        minSdk = 21

        ndk {
            abiFilters += listOf("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        @Suppress("UnstableApiUsage")
        externalNativeBuild {
            @Suppress("UnstableApiUsage")
            cmake {
                arguments += listOf(
                    "-DCMAKE_BUILD_TYPE=Release",
                    "-DCMAKE_SHARED_LINKER_FLAGS=-Wl,-z,max-page-size=16384,-z,common-page-size=16384"
                )
                cppFlags += listOf("-O2", "-DNDEBUG")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.31.6"
        }
    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
}
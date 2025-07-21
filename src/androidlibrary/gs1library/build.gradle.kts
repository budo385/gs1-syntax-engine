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
                version = "1.1.0"
            }
        }
    }
}

android {
    namespace = libNamespace
    compileSdk = 36

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        @Suppress("UnstableApiUsage")
        externalNativeBuild {
            @Suppress("UnstableApiUsage")
            cmake {
                cppFlags += "-Wl,-z,max-page-size=16384"
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
            version = "4.0.2"
        }
    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
}
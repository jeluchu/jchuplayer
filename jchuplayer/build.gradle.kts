plugins {
    id("maven-publish")
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.jeluchu.jchuplayer"
    compileSdk = 34

    defaultConfig {
        minSdk = 22

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

group = "com.jeluchu.jchuplayer"
version = "1.0.0-alpha04"

dependencies {
    implementation(libs.bundles.androidx)
    implementation(libs.bundles.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                from(components["release"])
                artifactId = "jchuplayer"
                version = "1.0.0-alpha04"
            }
        }
    }
}

tasks.withType<AbstractArchiveTask> {
    if(this.name == "releaseSourcesJar" || this.name == "debugSourcesJar") {
        enabled = false
    }
}
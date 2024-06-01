plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    `maven-publish`
    `ivy-publish`
}

android {
    namespace = "com.github.joaoeudes7.grpc.android.helper"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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

afterEvaluate {
    publishing {
        publications {
            val organizationGroup = "com.github.joaoeudes7"
            val artifactIdPkg = "grpc-android-helper"
            val versionPkg = "1.0.4"

            create<MavenPublication>("maven") {
                groupId = organizationGroup
                artifactId = artifactIdPkg
                version = versionPkg

                from(components.findByName("release"))
            }

            create<IvyPublication>("ivy") {
                organisation = organizationGroup
                module = artifactIdPkg
                revision = versionPkg

                from(components.findByName("release"))
            }
        }

        repositories {
            mavenLocal()
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(libs.protobuf.javalite)
    implementation(libs.grpc.protobuf.lite)
    implementation(libs.grpc.kotlin.stub)
    implementation(libs.grpc.stub)
    implementation(libs.timberkt)
    implementation(libs.grpc.android)
    implementation(libs.grpc.okhttp)
}
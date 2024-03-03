import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)

    `maven-publish`
    alias(libs.plugins.ktlint)
}

android {
    namespace = "com.github.tbsten.cameraxcompose"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    lint {
        sarifOutput = File(project.buildDir, "reports/android-lint/lintResults.sarif")
        textOutput = File(project.buildDir, "reports/android-lint/lintResults.txt")
        htmlOutput = File(project.buildDir, "reports/android-lint/lintResults.html")
        xmlReport = false
    }

    publishing {
        singleVariant("release") {
            withJavadocJar()
            withSourcesJar()
        }
    }
}

ktlint {
    android = true
    outputToConsole = true
    verbose = true
    debug = true
    ignoreFailures = true
    reporters {
        reporter(ReporterType.HTML)
        reporter(ReporterType.CHECKSTYLE)
    }
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.tbsten"
            artifactId = "camerax-compose"
            version = "0.0.1"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

dependencies {
    lintChecks(libs.compose.lint.checks)

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.camerax.core)
    implementation(libs.camerax.camera2)
    implementation(libs.camerax.lifecycle)
    implementation(libs.camerax.video)
    implementation(libs.camerax.view)
}

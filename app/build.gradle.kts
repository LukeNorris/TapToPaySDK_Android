import java.util.Properties
val localProps = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}

fun localProp(name: String): String =
    (localProps[name] as? String) ?: ""

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.0"
}

android {
    namespace = "com.example.taptopaysdk"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.taptopaysdk"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        vectorDrawables.useSupportLibrary = true

        buildConfigField(
            "String",
            "ADYEN_API_KEY",
            "\"${localProp("ADYEN_API_KEY")}\""
        )
        buildConfigField(
            "String",
            "ADYEN_MERCHANT_ACCOUNT",
            "\"${localProp("ADYEN_MERCHANT_ACCOUNT")}\""
        )
        buildConfigField(
            "String",
            "STORE",
            "\"${localProp("STORE")}\""
        )
        buildConfigField(
            "String",
            "KEY_IDENTIFIER",
            "\"${localProp("KEY_IDENTIFIER")}\""
        )
        buildConfigField(
            "String",
            "PASSPHRASE",
            "\"${localProp("PASSPHRASE")}\""
        )
        buildConfigField(
            "String",
            "KEY_VERSION",
            "\"${localProp("KEY_VERSION")}\""
        )
        buildConfigField(
            "String",
            "ENV",
            "\"${localProp("ENV")}\""
        )
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

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}


kotlin {
    jvmToolchain(17)
}
val version = "2.9.0"
dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.10.01"))
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.8.3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.datastore:datastore-core:1.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    debugImplementation("com.adyen.ipp:pos-mobile-debug:$version")
    debugImplementation("com.adyen.ipp:payment-tap-to-pay-debug:$version")
    debugImplementation("com.adyen.ipp:payment-card-reader-debug:$version")
    debugImplementation("androidx.compose.ui:ui-tooling")

}

plugins {
    alias(libs.plugins.speedmeter.android.library)
    alias(libs.plugins.speedmeter.android.hilt)
}

android {
    namespace = "com.matin.speedmeter.core.network"
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.kotlinx.serialization.json)
}
plugins {
    alias(libs.plugins.speedmeter.android.library)
    alias(libs.plugins.speedmeter.android.hilt)
}

android {
    namespace = "com.matin.speedmeter.core.common"
}

dependencies {
    implementation(libs.androidx.core.ktx)
}
plugins {
    alias(libs.plugins.speedmeter.android.library)
}

android {
    namespace = "com.matin.speedmeter.core.common"
}

dependencies {
    implementation(libs.androidx.core.ktx)
}
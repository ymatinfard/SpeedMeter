plugins {
    alias(libs.plugins.speedmeter.android.library)
    alias(libs.plugins.speedmeter.android.hilt)
}

android {
    namespace = "com.matin.speedmeter.core.data"
}

dependencies {
    implementation(projects.model)
    implementation(projects.core.network)
}
plugins {
    alias(libs.plugins.speedmeter.android.feature)
    alias(libs.plugins.speedmeter.android.library.compose)
    alias(libs.plugins.speedmeter.android.hilt)
}

android {
    namespace = "com.matin.speedmeter.feature.stopwatch"
}

dependencies {
    implementation(projects.model)
    implementation(projects.core.data)
    implementation(projects.core.testing)
    implementation(projects.core.designsystem)
    implementation(projects.core.common)
    implementation(projects.worker)
    implementation(libs.coil.network.okhttp)
    implementation (libs.accompanist.permissions)
    implementation(libs.compose.charts)
}

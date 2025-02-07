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
    implementation(libs.androidx.core.ktx)
}

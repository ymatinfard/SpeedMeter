plugins {
    alias(libs.plugins.speedmeter.android.library)
    alias(libs.plugins.speedmeter.android.library.compose)
}
android {
    namespace = "com.matin.speedmeter.core.designsystem"
}
dependencies {
    implementation(projects.core.common)
    implementation(libs.coil3.coil.compose)
}
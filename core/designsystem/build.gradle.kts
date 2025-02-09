plugins {
    alias(libs.plugins.speedmeter.android.library)
    alias(libs.plugins.speedmeter.android.library.compose)
}
android {
    namespace = "com.matin.speedmeter.core.designsystem"
}
dependencies {
    implementation(project(":core:common"))
}
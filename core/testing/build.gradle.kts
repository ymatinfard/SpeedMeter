plugins {
    alias(libs.plugins.speedmeter.android.library)
}

android {
    namespace = "com.matin.speedmeter.core.testing"
}

dependencies {
    implementation(projects.model)
    implementation(projects.core.common)
    implementation(libs.androidx.test.rules)
    implementation(libs.hilt.android.testing)
    api(libs.kotlinx.coroutines.test)
    api(libs.androidx.junit)
    api(libs.mockk)
    api(libs.turbine)
}
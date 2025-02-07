plugins {
    alias(libs.plugins.speedmeter.android.library)
}

android {
    namespace = "com.matin.speedmeter.core.testing"
}

dependencies {
    api(libs.kotlinx.coroutines.test)
    api(libs.androidx.junit)
    api(libs.mockk)
    api(libs.turbine)
}
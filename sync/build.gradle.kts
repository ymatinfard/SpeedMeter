plugins {
    alias(libs.plugins.speedmeter.android.library)
    alias(libs.plugins.speedmeter.android.hilt)
}

android {
    namespace = "com.matin.speedmeter.sync"
}

dependencies {
    implementation(libs.hilt.ext.work)
    implementation(libs.androidx.work.ktx)
    ksp(libs.hilt.ext.compiler)
    implementation(projects.core.testing)
}
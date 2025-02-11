plugins {
    alias(libs.plugins.speedmeter.android.library)
    alias(libs.plugins.speedmeter.android.hilt)
}

android {
    namespace = "com.matin.speedmeter.worker"
}

dependencies {
    implementation(libs.hilt.ext.work)
    implementation(libs.androidx.work.ktx)
    implementation(projects.core.common)
    ksp(libs.hilt.ext.compiler)
    implementation(projects.core.testing)
    implementation(projects.core.data)
}
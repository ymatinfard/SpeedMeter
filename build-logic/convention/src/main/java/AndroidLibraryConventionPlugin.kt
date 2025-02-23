import com.android.build.gradle.LibraryExtension
import com.matin.speedmeter.configureFlavors
import com.matin.speedmeter.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                configureFlavors(this)
                defaultConfig.targetSdk = 34
                testOptions.animationsDisabled = true
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                // The resource prefix is derived from the module name,
                // so resources inside ":core:module1" must be prefixed with "core_module1_"
                resourcePrefix =
                    path.split("""\W""".toRegex()).drop(1).distinct().joinToString(separator = "_")
                        .lowercase() + "_"
            }

            dependencies {
                add("testImplementation", kotlin("test"))
            }
        }
    }
}

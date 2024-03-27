import java.util.UUID
import org.apache.tools.ant.taskdefs.condition.Os

plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "sn.zhang.routines"
    compileSdk = 34

    defaultConfig {
        applicationId = "sn.zhang.routines"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
//    implementation("androidx.activity:activity:1.8.2:sources")
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
task("downloadSources") {
    afterEvaluate {
        doLast {
            var configuration: Configuration? = null
            var repository: RepositoryHandler? = null
            repositories.toList().find {
                logger.lifecycle("Attempt to download sources from ${it.name}")
                repositories.clear()
                repositories.add(it)
                configuration = configurations.create("downloadSourcesFrom_${UUID.randomUUID()}")
                configuration?.isTransitive = false
                dependencies.add(configuration!!.name, "androidx.activity:activity:1.8.2:sources")
                var files: Set<File>? = null
                try {
                    files = configuration!!.resolvedConfiguration.lenientConfiguration.files
                } catch (ignore: Throwable) {
                }
                files != null && files.isNotEmpty()
            }?.let {
                //repository = it
            }
            if (repository == null) {
                configuration = configurations.create("downloadSources_${UUID.randomUUID()}")
                configuration?.isTransitive = false
                dependencies.add(configuration!!.name, "androidx.activity:activity:1.8.2:sources")
                configuration?.resolve()
            }
            val sourcesPath = configuration?.singleFile?.path
            if (sourcesPath != null) {
                logger.lifecycle("Sources were downloaded to $sourcesPath")
                File("C:\\Users\\ZhangJun\\AppData\\Local\\Temp\\sources7loc\\path.tmp").writeText(
                    sourcesPath
                )
            } else throw RuntimeException("Sources download failed")
        }
    }
}


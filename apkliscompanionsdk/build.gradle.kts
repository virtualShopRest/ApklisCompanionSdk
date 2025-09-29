plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.maven.publish)
}

android {
    namespace = "cu.apklis.companion.sdk"
    compileSdk = 36

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        // Información de la librería
        buildConfigField("String", "LIBRARY_VERSION", "\"1.0.0\"")
        buildConfigField("String", "LIBRARY_NAME", "\"ApklisCompanionSdk\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
    }
    // Configuración para generar fuentes y documentación
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

// Configuración de publicación
publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.tuusuario"
            artifactId = "apklis-library"
            version = "1.0.0"

            afterEvaluate {
                from(components["release"])
            }

            pom {
                name.set("Apklis Library")
                description.set("Android library for Apklis license and purchase verification")
                url.set("https://github.com/tuusuario/ApklisLibrary")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("tuusuario")
                        name.set("Tu Nombre")
                        email.set("tu@email.com")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/tuusuario/ApklisLibrary.git")
                    developerConnection.set("scm:git:ssh://github.com:tuusuario/ApklisLibrary.git")
                    url.set("https://github.com/tuusuario/ApklisLibrary/tree/main")
                }
            }
        }
    }
}
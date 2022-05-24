import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.apache.tools.ant.taskdefs.condition.Os

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
}

version = "1.0"

kotlin {
    fun KotlinNativeTarget.configBridge(platform: String) {
        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            println("building Swift is not supported on windows")
        } else {
            compilations.getByName("main") {
                cinterops.create("StaticLibrary") {
                    extraOpts("-libraryPath", "$rootDir/iosApp/StaticLibrary/build/Release-$platform")
                    val interopTask = tasks[interopProcessingTaskName]
                    //necessary to run this task first
                    interopTask.dependsOn(":iosApp:StaticLibrary:build${platform.capitalize()}")
                    //check if swift header has changed
                    headers(file("$rootDir/iosApp/StaticLibrary/build/Release-$platform/include/StaticLibrary/StaticLibrary-Swift.h"))
                    //this includes the files so the .def file can find the header etc
                    includeDirs.headerFilterOnly("$rootDir/iosApp/StaticLibrary/build/Release-$platform/include")

                    binaries {
                        all {
                            //adds libswiftCompatibilityConcurrency
                            linkerOpts.add("-L/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/lib/swift/$platform")
                        }
                    }
                }
            }
        }
    }

    android()
    iosX64 {
        this.configBridge("iphonesimulator")
    }

    iosArm64 {
        this.configBridge("iphoneos")
    }

    iosSimulatorArm64 {
        this.configBridge("iphonesimulator")
    }

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }
    }
    
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
                implementation("org.robolectric:robolectric:4.8.1")
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 32
    }

    testOptions.unitTests.isIncludeAndroidResources = true
    testOptions.unitTests.isReturnDefaultValues = true
}
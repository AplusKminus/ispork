plugins {
  id("com.android.application")
  kotlin("android")
  id("kotlin-android-extensions")
  kotlin("kapt")
  id("de.mannodermaus.android-junit5")
}

android {
  compileSdkVersion(28)
  defaultConfig {
    applicationId = "app.pmsoft.ispork"
    minSdkVersion(21)
    targetSdkVersion(28)
    versionCode = 1
    versionName = "0.0.1"
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    javaCompileOptions {
      annotationProcessorOptions {
        arguments = mapOf("room.schemaLocation" to "$projectDir/schemas")
      }
    }
  }
  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android.txt"),
        "proguard-rules.pro"
      )
    }
  }
  configurations.all {
    resolutionStrategy.force("com.google.code.findbugs:jsr305:1.3.9")
  }
  sourceSets {
    getByName("main") {
      java.srcDir("src/main/kotlin")
      res.srcDirs(
        setOf(
          "src/main/res/layout_scheduled_transaction",
          "src/main/res"
        )
      )
    }
    getByName("test").java.srcDir("src/test/kotlin")
  }
}

dependencies {
  implementation(Libs.appcompat)
  implementation(Libs.recyclerview)
  implementation(Libs.constraintlayout)
  implementation(Libs.material)
  implementation(Libs.dagger)
  testImplementation(Libs.junit_jupiter_api)
  testRuntimeOnly(Libs.junit_jupiter_engine)

  androidTestImplementation(Libs.espresso_core) {
    exclude(
      group = "com.android.support",
      module = "support-annotations"
    )
  }
  implementation(Libs.org_jetbrains_kotlin_kotlin_stdlib_jdk8)

  // START Room Dependencies
  implementation(Libs.room_runtime)
  kapt("androidx.room:room-compiler:" + Versions.androidx_room)

  // Test helpers
  testImplementation(Libs.room_testing)
  // END Room Dependencies

  implementation(Libs.lifecycle_extensions)
}

kapt {
  correctErrorTypes = true
}

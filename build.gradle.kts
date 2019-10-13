// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  `kotlin-dsl`
  id("de.fayard.buildSrcVersions") version "0.6.1"
}

buildscript {
  repositories {
    google()
    jcenter()
  }
  dependencies {
    classpath(Libs.com_android_tools_build_gradle)
    classpath(Libs.android_junit5)

    // NOTE: Do not place your application dependencies here; they belong
    // in the individual module build.gradle files
  }
}

allprojects {
  repositories {
    google()
    jcenter()
  }
}

tasks {
}

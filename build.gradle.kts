import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.4.10"
}

allprojects {
    tasks.withType<KotlinCompile>().all {
        kotlinOptions.jvmTarget = "11"
    }

    repositories {
        mavenCentral()
    }
}

dependencies {
    implementation(project(":app"))
}

application {
    mainClassName = "com.eighthours.sample.app.ApplicationKt"
}

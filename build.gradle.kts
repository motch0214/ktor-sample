@file:Suppress("PropertyName")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.4.10"
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions.jvmTarget = "11"
}

repositories {
    mavenCentral()
}

val ktor_version: String by project

dependencies {
    // Ktor
    implementation("io.ktor:ktor-server-netty:$ktor_version")

    // Logging
    runtimeOnly("ch.qos.logback:logback-classic:1.2.3")
}

application {
    mainClassName = "com.eighthours.sample.app.ApplicationKt"
}

@file:Suppress("PropertyName")

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

val ktor_version: String by project

dependencies {
    implementation(project(":support"))

    // Ktor
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")

    // Logging
    runtimeOnly("ch.qos.logback:logback-classic:1.2.3")
}

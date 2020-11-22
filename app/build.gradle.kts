@file:Suppress("PropertyName")

plugins {
    kotlin("jvm")
}

val ktor_version: String by project

dependencies {
    // Ktor
    implementation("io.ktor:ktor-server-netty:$ktor_version")

    // Logging
    runtimeOnly("ch.qos.logback:logback-classic:1.2.3")
}

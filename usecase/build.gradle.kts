@file:Suppress("PropertyName")

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

val ktor_version: String by project
val koin_version: String by project

dependencies {
    implementation(project(":domain"))

    // Ktor
    implementation("io.ktor:ktor-server-host-common:$ktor_version")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")

    // Dependency Injection
    implementation("org.koin:koin-core:$koin_version")
}

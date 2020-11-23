@file:Suppress("PropertyName")

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

val koin_version: String by project
val serialization_version: String by project

dependencies {
    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization_version")

    // Dependency Injection
    implementation("org.koin:koin-core:$koin_version")
}

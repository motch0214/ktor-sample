@file:Suppress("PropertyName")

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

val ktor_version: String by project
val koin_version: String by project
val firebase_version: String by project
val serialization_version: String by project

dependencies {
    implementation(project(":usecase"))
    implementation(project(":domain"))

    // Ktor
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization_version")

    // Dependency Injection
    implementation("org.koin:koin-ktor:$koin_version")
    implementation("org.koin:koin-logger-slf4j:$koin_version")

    // Logging
    runtimeOnly("ch.qos.logback:logback-classic:1.2.3")

    // Firebase
    implementation("com.google.firebase:firebase-admin:$firebase_version")
}

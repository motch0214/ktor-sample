@file:Suppress("PropertyName")

plugins {
    kotlin("jvm")
}

val ktor_version: String by project

dependencies {
    // Ktor
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
}

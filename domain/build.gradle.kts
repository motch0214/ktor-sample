@file:Suppress("PropertyName")

plugins {
    kotlin("jvm")
}

val koin_version: String by project

dependencies {
    implementation("org.koin:koin-core:$koin_version")
}

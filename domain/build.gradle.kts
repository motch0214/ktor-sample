@file:Suppress("PropertyName")

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    kotlin("kapt")
}

val koin_version: String by project
val serialization_version: String by project
val doma_version: String by project

dependencies {
    api(project(":domain-serialization"))

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization_version")

    // Dependency Injection
    implementation("org.koin:koin-core:$koin_version")

    // Database Access
    implementation("org.seasar.doma:doma:$doma_version")
    kapt("org.seasar.doma:doma:$doma_version")
}

kapt {
    includeCompileClasspath = false
    arguments {
        arg("doma.domain.converters", "com.eighthours.sample.domain.common.doma.DomainConvertersProvider")
    }
}

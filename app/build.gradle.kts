@file:Suppress("PropertyName")

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

val ktor_version: String by project
val koin_version: String by project
val firebase_version: String by project
val doma_version: String by project

dependencies {
    implementation(project(":usecase"))
    implementation(project(":domain"))

    // Ktor
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")

    // Dependency Injection
    implementation("org.koin:koin-ktor:$koin_version")
    implementation("org.koin:koin-logger-slf4j:$koin_version")
    testImplementation("org.koin:koin-test:$koin_version")

    // Logging
    runtimeOnly("ch.qos.logback:logback-classic:1.2.3")

    // Firebase
    implementation("com.google.firebase:firebase-admin:$firebase_version")

    // Database Access
    implementation("org.seasar.doma:doma:$doma_version")
    implementation("com.zaxxer:HikariCP:3.4.5")

    // Database Migration
    implementation("org.flywaydb:flyway-core:7.2.1")

    // Database
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client:2.7.0")
    testRuntimeOnly("com.h2database:h2:1.4.200")
}

tasks.test {
    doFirst {
        environment(
            mapOf(
                "DATABASE_SECRET_JSON" to """{ "username": "sa", "password": "pass" }""",
                "DATABASE_URL" to "jdbc:h2:$rootDir/.env/work/h2/test;MODE=MySQL;AUTO_SERVER=TRUE",
                "DATABASE_DIALECT" to "org.seasar.doma.jdbc.dialect.H2Dialect"
            )
        )
    }
    maxParallelForks = 1
}

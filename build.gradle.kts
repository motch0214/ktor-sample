import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.file.File
import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    application
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
    kotlin("kapt") version "1.4.10"
    id("com.google.cloud.tools.jib") version "2.6.0"
}

allprojects {
    tasks.withType<KotlinCompile>().all {
        kotlinOptions.jvmTarget = "11"
    }

    repositories {
        mavenCentral()
        jcenter()
    }
}

dependencies {
    implementation(project(":app"))
}

val mainClass = "com.eighthours.sample.app.ApplicationKt"

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = mainClass
    }
}

jib {
    to.image = "${project.name}:latest"
}
tasks.jib {
    doFirst {
        val image = findProperty("jib.image") as String?
        if (image != null) {
            jib {
                to.image = "$image:$version"
                to.tags = emptySet()
            }
        }
    }
}

application {
    mainClassName = mainClass
}
tasks.named<JavaExec>("run") {
    classpath += project(":app").sourceSets["test"].runtimeClasspath
    doFirst {
        val file = File("$projectDir/.env/application.properties")
        if (file.exists) {
            @Suppress("UNCHECKED_CAST")
            environment(file.loadProperties() as Map<String, *>)
        }
    }
}

tasks.register("h2Console", JavaExec::class) {
    group = "application"
    classpath += project(":app").sourceSets["test"].runtimeClasspath
    main = "org.h2.tools.Console"
    args = listOf(
        "-url", "jdbc:h2:./.env/work/h2/test;MODE=MySQL;AUTO_SERVER=TRUE",
        "-user", "sa",
        "-password", "pass"
    )
}

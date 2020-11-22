import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
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

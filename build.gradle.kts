import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.file.File
import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    application
    kotlin("jvm") version "1.4.20"
    kotlin("plugin.serialization") version "1.4.20"
    kotlin("kapt") version "1.4.20"
    id("com.google.cloud.tools.jib") version "2.6.0"
    id("name.remal.check-updates") version "1.1.4"
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

subprojects {
    apply(plugin = "java")

    dependencies {
        // Testing
        testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
        testImplementation("org.assertj:assertj-core:3.18.1")
        testImplementation("org.mockito:mockito-inline:3.6.0")
    }

    tasks.test {
        useJUnitPlatform()
    }
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "com.eighthours.sample.app.ApplicationKt"
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
    mainClass.set("com.eighthours.sample.app.LocalApplicationKt")
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
    doFirst {
        val name = findProperty("db.name") as String?
        args = listOf(
            "-url", "jdbc:h2:./.env/work/h2/${name ?: "application"};MODE=MySQL;AUTO_SERVER=TRUE",
            "-user", "sa",
            "-password", "pass"
        )
    }
}

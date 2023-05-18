import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    `java-library`
    `maven-publish`
    kotlin("jvm") version "1.8.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

val _name = "MultiBans"
val _version = "0.1.0"
val _group = "me.kvalbrus.multibans"

subprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    this.group = _group
    this.version = _version
}

val modules = listOf("api", "common", "bukkit")

publishing {
    repositories {
        maven {
            url = uri("https://jitpack.io")
        }
    }

    publications {
        modules.forEach { moduleName ->
            val module = project(":$moduleName")

            create<MavenPublication>(moduleName) {
                this.groupId = _group
                this.artifactId = moduleName
                this.version = _version

                from(module.components["java"])
            }
        }
    }
}

tasks.withType(ShadowJar::class.java) {
    archiveFileName.set("${_name}-${_version}.jar")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.21")
}

kotlin {
    jvmToolchain(19)
}
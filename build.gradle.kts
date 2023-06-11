import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.support.uppercaseFirstChar

plugins {
    java
    `java-library`
    `maven-publish`
    kotlin("jvm") version "1.8.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

val _name = "MultiBans"
val _version = "0.1.1"
val _group = "me.kvalbrus.multibans"

val api = "api"
val modules = listOf("api", "common", "bukkit")
val platforms = listOf("bukkit")

subprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "maven-publish")
    apply(plugin = "com.github.johnrengelman.shadow")

    this.group = _group
    this.version = _version

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks.withType<ShadowJar> {
       // minimize()

        relocations()

        if (platforms.contains(project.name) || api == project.name) {
            version = _version
            this.archiveFileName.set("${_name}${project.name.uppercaseFirstChar()}-${version}.jar")
            this.destinationDirectory.set(file("${project.rootProject.buildDir}/libs"))
        }
    }

    tasks.named("build") {
        dependsOn(project.tasks.withType<ShadowJar>())
    }
}

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

tasks.withType<ShadowJar> {
    relocations()
    dependsOn(subprojects.map { it.tasks.withType<ShadowJar>() })
    archiveFileName.set("${_name}-${_version}.jar")
}

tasks.named("build") {
    dependsOn(project.tasks.withType<ShadowJar>())
    dependsOn(subprojects.map { it.tasks.named("clean") })
}

dependencies {
//    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.21")
    implementation(project(":common"))
    implementation(project(":api"))
    implementation(project(":bukkit"))
}

fun ShadowJar.relocations() {
    relocate("com.zaxxer.hikari", "${_group}.libs.com.zaxxer.hikari")
    relocate("com.mysql", "${_group}.libs.com.mysql")
    relocate("com.moandjiezana.toml", "${_group}.libs.com.moandjiezana.toml")
    relocate("com.google.protobuf", "${_group}.libs.com.google.protobuf")
    relocate("com.google.gson", "${_group}.libs.com.google.gson")
    relocate("kotlin", "${_group}.libs.kotlin")
    relocate("net.kyori", "${_group}.libs.net.kyori")
    relocate("org.intellij.lang.annotations", "${_group}.libs.org.intellij.lang.annotations")
    relocate("org.jetbrains.annotations", "${_group}.libs.org.jetbrains.annotations")
    relocate("org.slf4j", "${_group}.libs.org.slf4j")
    relocate("google.protobuf", "${_group}.libs.google.protobuf")
}

fun ShadowJar.deleteJar() {
    delete(("${project.buildDir}"))
}
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.8.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

val _version = "0.1.0"
val _group = "me.kvalbrus"

subprojects {
    apply(plugin = "maven-publish")

    this.group = _group
    this.version = _version

    configure<PublishingExtension> {
        publications {
            create<MavenPublication>("maven") {
                this.groupId = _group
                this.artifactId = "multibans"
                this.version = _version
            }
        }
    }
}

tasks.withType(ShadowJar::class.java) {
    archiveFileName.set("MultiBans-${_version}.jar")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.21")
}

kotlin {
    jvmToolchain(11)
}
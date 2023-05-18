import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    `java-library`
    `maven-publish`
    kotlin("jvm") version "1.8.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

val _version = "0.1.0"
val _group = "me.kvalbrus"
//
//subprojects {
//    apply(plugin = "java")
//    apply(plugin = "maven-publish")
//
//    this.version = _version
//
//    configure<PublishingExtension> {
//        publications {
//            create<MavenPublication>("maven") {
//                this.groupId = "me.kvalbrus.multibans"
//                this.artifactId = project.name
//                this.version = _version
//
//                from(components["java"])
//            }
//        }
//    }
//}

configure<PublishingExtension> {
    repositories {
        maven {
            url = uri("https://jitpack.io")
        }
    }

    publications {
        create<MavenPublication>("api") {
            from(components["java"])

            groupId = "me.kvalbrus.multibans"
            artifactId = "api"
            version = _version
        }

        create<MavenPublication>("common") {
            from(components["java"])

            groupId = "me.kvalbrus.multibans"
            artifactId = "common"
            version = _version
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
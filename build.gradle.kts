/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    `java-library`
    `maven-publish`
    idea
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    maven {
        url = uri("https://nexus.scarsz.me/content/groups/public/")
    }

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }

    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }

    maven{
        url = uri("https://oss.sonatype.org/content/repositories/smaven")
    }

}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    compileOnly("org.spigotmc:spigot-api:1.21.1-R0.1-SNAPSHOT") // The Spigot API with no shadowing. Requires the OSS repo
    compileOnly("com.discordsrv:discordsrv:1.28.0")
}

group = "net.yukkuricraft.remisux"
version = "0.0.3"
description = "Remi Doesn't Know What They're Doing"
java.sourceCompatibility = JavaVersion.VERSION_21

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

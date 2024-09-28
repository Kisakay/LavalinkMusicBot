plugins {
    kotlin("jvm") version "2.0.10"
    kotlin("plugin.serialization") version "1.9.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("dev.kord:kord-core:0.14.0")
    implementation("io.ktor:ktor-client-core:2.0.0")
    implementation("io.ktor:ktor-client-cio:2.3.5")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("org.reflections:reflections:0.10.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.0") // Add Kotlin Reflection library

    implementation("dev.schlaubi.lavakord:kord-jvm:7.1.0")
    implementation("dev.schlaubi.lavakord:lavasrc-jvm:7.1.0") {
        exclude(group = "com.github.topi314.lavasrc", module = "protocol")
    }
    implementation("org.yaml:snakeyaml:2.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
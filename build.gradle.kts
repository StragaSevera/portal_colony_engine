plugins {
    kotlin("jvm") version "1.5.10"
}

group = "ru.ought"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    val kotest = "4.6.0"
    val atrium = "0.16.0"
    val mockk = "1.11.0"

    implementation(kotlin("stdlib"))
    testImplementation("io.kotest:kotest-runner-junit5:$kotest")
    testImplementation("ch.tutteli.atrium:atrium-fluent-en_GB:$atrium")
    testImplementation("io.mockk:mockk:$mockk")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
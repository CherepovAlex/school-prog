plugins {
    java
    application
}

group = "inno.code"
version = "1.0-SNAPSHOT"

application { mainClass.set("webinar8.Zadacha1") }

tasks.withType<JavaExec> {
    standardInput = System.`in`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.datafaker:datafaker:2.0.2")
}
plugins {
    java
    application
}

group = "inno.code"
version = "1.0-SNAPSHOT"

application { mainClass.set("homeworks.homework07.Task3.Task3") }

tasks.withType<JavaExec> {
    standardInput = System.`in`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.datafaker:datafaker:2.0.2")
}
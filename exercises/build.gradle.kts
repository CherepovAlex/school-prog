plugins {
    application
    java
}

group = "inno.code"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("webinar6.exercise1")
}

tasks.withType<JavaExec> {
    standardInput = System.`in`
}

repositories {
    mavenCentral()
}
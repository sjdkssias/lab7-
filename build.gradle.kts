plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.0.13"
}

group = "org.example"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.openjfx:javafx-controls:24.0.1")
    implementation("org.openjfx:javafx-fxml:24.0.1")
    implementation("org.postgresql:postgresql:42.7.5")
    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.18.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("io.vertx:vertx-core:4.5.3")
}

javafx {
    version = "23.0.1"
    modules = listOf("javafx.controls", "javafx.fxml")
}


application {
    mainClass.set("se.ifmo.client.ClientMain")
}
tasks.compileJava {
    options.release.set(21)
}

tasks.register<Jar>("serverFatJar") {

    archiveClassifier.set("server")
    manifest {
        attributes["Main-Class"] = "se.ifmo.server.ServerMain"
    }
    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
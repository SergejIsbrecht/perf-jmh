plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

application {
    mainClass.set("org.openjdk.jmh.Main")
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

tasks.withType<Zip> {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks.withType<Tar> {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.openjdk.jmh:jmh-core:1.36")
    annotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.36")

    implementation("org.assertj:assertj-core:3.23.1")
    implementation("io.vavr:vavr:0.10.4")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
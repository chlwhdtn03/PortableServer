import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
}

group = "com.chlwhdtn"
version = "1.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    implementation("io.vertx:vertx-core:4.3.1")
    implementation("io.vertx:vertx-lang-kotlin:4.3.1")
    implementation("io.vertx:vertx-web:4.3.1")

    implementation("org.pushing-pixels:radiance-common:5.0.0")
    implementation("org.pushing-pixels:radiance-swing-ktx:5.0.0")

    implementation("org.pushing-pixels:radiance-theming:5.0.0")
    implementation("org.pushing-pixels:radiance-theming-ktx:5.0.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = org.gradle.api.file.DuplicatesStrategy.INCLUDE
}
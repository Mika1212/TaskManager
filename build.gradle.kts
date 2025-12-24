import java.util.Properties

fun loadEnv(path: String): Map<String, String> {
    val props = Properties()
    file(path).inputStream().use { props.load(it) }
    return props.entries.associate { it.key.toString() to it.value.toString() }
}

val envDev = loadEnv(".env.dev")

plugins {
    id("org.springframework.boot") version "3.5.6" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    java
}

group = "org.mika1212"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    group = "org.mika1212"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(17)
        }
    }

    dependencies {
        compileOnly("org.projectlombok:lombok:1.18.30")
        annotationProcessor("org.projectlombok:lombok:1.18.30")
        testCompileOnly("org.projectlombok:lombok:1.18.30")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun>().configureEach {
        environment(envDev)
        systemProperty("spring.profiles.active", envDev["SPRING_PROFILES_ACTIVE"] ?: "dev")
    }
}

tasks.register("runAll") {
    dependsOn(
        ":user-service:bootRun",
        ":task-service:bootRun",
        ":project-service:bootRun"
    )
}

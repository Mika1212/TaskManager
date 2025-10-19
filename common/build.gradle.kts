plugins {
    java
}

group = "org.mika1212"
version = "0.0.1-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
    }
}

dependencies {
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.boot:spring-boot-starter")
}


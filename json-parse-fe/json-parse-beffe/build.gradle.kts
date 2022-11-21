plugins {
    java
    `maven-publish`
    id("my-project.java-conventions2")
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter:2.7.3")
    implementation("com.fasterxml.jackson.core:jackson-core:2.12.3")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.3")
    implementation("org.javassist:javassist:3.28.0-GA")
    implementation("org.projectlombok:lombok:1.18.24")
    implementation("org.jd:jd-core:1.1.4")
    implementation("com.hayden.dynamicparse:spring-boot-starter-dynamic-parse:1.1.0")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive:2.5.0")
    implementation("org.mongodb:bson:4.2.3")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client:2.5.0")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:2.5.0")
    implementation("org.springframework.boot:spring-boot-starter-security:2.5.0")
    implementation("org.springframework.boot:spring-boot-starter-webflux:2.5.0")
    implementation("org.projectlombok:lombok:1.18.20")
    implementation("org.springframework.boot:spring-boot-starter-mail:2.5.0")
    implementation("com.github.eirslett:frontend-maven-plugin:1.6")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.5.0")
    testImplementation("io.projectreactor:reactor-test:3.4.6")
    testImplementation("org.testcontainers:testcontainers:1.15.3")
    testImplementation("org.testcontainers:junit-jupiter:1.15.3")
    testImplementation("org.springframework.security:spring-security-test:5.5.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.3")
}

group = "com.hayden"
version = "0.0.1-SNAPSHOT"
description = "merge-social-beffe"

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.test  {
    useJUnitPlatform()
}

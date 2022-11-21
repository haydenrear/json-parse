import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    java
    `maven-publish`
    id("org.springframework.boot") version "2.4.2"
//    `organizational-conventions`
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    implementation("org.springframework.boot:spring-boot-starter:2.7.3")
    implementation("com.fasterxml.jackson.core:jackson-core:2.12.3")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.3")
    implementation("org.javassist:javassist:3.28.0-GA")
    implementation("org.projectlombok:lombok:1.18.24")
    implementation("org.jd:jd-core:1.1.4")
    implementation(project(":json-parse-webapp"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

group = "com.hayden"
version = "0.0.1-SNAPSHOT"
description = "json-parse-angular-fe"

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

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
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.3")
}

group = "com.hayden"
version = "0.0.1-SNAPSHOT"
description = "json-parse-beffe"

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

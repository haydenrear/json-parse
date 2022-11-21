plugins {
    java
    `maven-publish`
    id("my-project.java-conventions")
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation(project(":merge-social-library"))
    implementation("org.springframework.boot:spring-boot-starter:2.7.3")
    implementation("org.springframework.boot:spring-boot-test:2.7.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.3")
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

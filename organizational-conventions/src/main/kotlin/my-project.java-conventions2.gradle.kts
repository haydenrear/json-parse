plugins {
    java
}

group = "com.hayden"

java.sourceCompatibility = JavaVersion.VERSION_15
java.targetCompatibility = JavaVersion.VERSION_15

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    testCompileOnly("org.projectlombok:lombok:1.18.24")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.24")
    implementation("org.springframework.boot:spring-boot-starter:2.7.3")
    implementation("com.fasterxml.jackson.core:jackson-core:2.12.3")
    implementation("org.javassist:javassist:3.28.0-GA")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.3")
    implementation("org.springframework.boot:spring-boot-starter:2.7.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.3")
}

repositories {
    mavenCentral()
}
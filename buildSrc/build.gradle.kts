plugins {
    id("java")
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    mavenLocal()
}

tasks.register("run") {
    dependsOn(gradle.includedBuild("json-parse-fe:json-parse-angular-fe").task(":build"))
}
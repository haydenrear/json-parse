import com.github.gradle.node.npm.task.NpxTask

group = "com.hayden"
version = "0.0.1-SNAPSHOT"
description = "json-parse-webapp"

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
    gradlePluginPortal()
}

plugins {
    java
    // You have to specify the plugin version, for instance
    // id("com.github.node-gradle.node") version "3.0.0"
    // This works as is here because we use the plugin source
    id("com.github.node-gradle.node") version "3.4.0"
}

val buildTask = tasks.register<NpxTask>("buildWebapp") {
    command.set("ng")
    args.set(listOf("build", "--prod"))
    dependsOn(tasks.npmInstall)
    inputs.dir(project.fileTree("src").exclude("**/*.spec.ts"))
    inputs.dir("node_modules")
    inputs.files("angular.json", ".browserslistrc", "tsconfig.json", "tsconfig.app.json")
    outputs.dir("${project.buildDir}/webapp")
}

val testTask = tasks.register<NpxTask>("testWebapp") {
    command.set("ng")
    args.set(listOf("test"))
    dependsOn(tasks.npmInstall)
    inputs.dir("src")
    inputs.dir("node_modules")
    inputs.files("angular.json", ".browserslistrc", "tsconfig.json", "tsconfig.spec.json", "karma.conf.js")
    outputs.upToDateWhen { true }
}

sourceSets {
    java {
        main {
            resources {
                // This makes the processResources task automatically depend on the buildWebapp one
                srcDir(buildTask)
            }
        }
    }
}

tasks.test {
    dependsOn()
}

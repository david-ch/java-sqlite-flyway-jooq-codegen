plugins {
    id("java")
    id("java-library")
    id("org.flywaydb.flyway") version "10.11.0"
    id("org.jooq.jooq-codegen-gradle") version "3.19.3"
}

group = "davidch.dbschema"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jooq:jooq:3.19.4")
    implementation("org.xerial:sqlite-jdbc:3.45.0.0")
    implementation("org.flywaydb:flyway-core:10.11.0")

    jooqCodegen("org.xerial:sqlite-jdbc:3.45.0.0")
}

flyway {
    mkdir("${project.layout.buildDirectory.get().asFile}")

    url = "jdbc:sqlite:${project.layout.buildDirectory.get().asFile}/codegen.db"
    locations = arrayOf("filesystem:${projectDir}/src/main/sql")
    schemas = arrayOf("main")
}

jooq {
    configuration {
        jdbc {
            url = "jdbc:sqlite:${project.layout.buildDirectory.get().asFile}/codegen.db"
        }

        generator {
            target {
                packageName = "davidch.dbschema"
                directory = "${projectDir}/generated/src/main/java"
            }
        }
    }

    tasks.jooqCodegen {
        dependsOn(tasks.flywayMigrate)
    }

    tasks.compileJava {
        dependsOn(tasks.jooqCodegen)
    }

    tasks.clean {
        delete("${projectDir}/generated")
    }

    sourceSets["main"].java.srcDir(file("${projectDir}/generated"))
}

tasks.test {
    useJUnitPlatform()
}
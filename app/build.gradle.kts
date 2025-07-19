plugins {
    id("java-library")
    id("io.freefair.lombok") version "8.14"
    id("io.github.goooler.shadow") version "8.1.8"
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}

val coreName = "Packer-App"

dependencies {
    implementation(project(":packer-core"))
}

tasks {
    jar {
        enabled = false
        manifest {
            attributes["Main-Class"] = "dev.nilkoush.packer.app.PackerApp"
        }
    }
    shadowJar {
        archiveBaseName.set(coreName)
        archiveClassifier.set("")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(sourceSets.main.get().output)
        destinationDirectory.set(file("${rootProject.projectDir}/target"))
    }
    build {
        dependsOn("shadowJar")
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
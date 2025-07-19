plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.8"
    id("io.freefair.lombok") version "8.14"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}

val coreName = "Packer"

dependencies {
    compileOnly("org.purpurmc.purpur:purpur-api:1.21.5-R0.1-SNAPSHOT")
    implementation("dev.nilkoush:thelibrary-paper:2.0.0-SNAPSHOT")
    implementation(project(":packer-core"))
}

paper {
    name = coreName
    main = "$group.packer.plugin.PackerPlugin"
    version = rootProject.version.toString()
    description = rootProject.description
    apiVersion = "1.21"
}

tasks {
    jar {
        enabled = false
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
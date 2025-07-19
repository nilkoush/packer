plugins {
    id("java")
    id("io.freefair.lombok") version "8.14"
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}

allprojects {
    group = "dev.nilkoush"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven("https://repo.purpurmc.org/snapshots")
        maven("https://repo.nilkoush.dev/public")
        maven("https://repo.nexomc.com/releases")
    }
}
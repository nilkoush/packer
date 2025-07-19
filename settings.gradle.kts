rootProject.name = "packer"

sequenceOf(
    "core",
    "plugin",
    "app",
).forEach {
    include("packer-$it")
    project(":packer-$it").projectDir = file(it)
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}
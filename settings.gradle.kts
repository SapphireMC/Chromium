pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        gradlePluginPortal()
    }
}

if (JavaVersion.current().ordinal + 1 < 21) {
    throw IllegalStateException("Please run gradle with Java 21+! (Your version: ${JavaVersion.current().ordinal + 1})")
}

rootProject.name = "chromium"

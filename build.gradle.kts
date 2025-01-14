plugins {
    java
    `maven-publish`
    alias(libs.plugins.loom)
    alias(libs.plugins.licenser)
}

val sodiumCompatibility = project.properties["sodiumCompatibility"].toString().toBoolean()

base {
    archivesName = "Chromium-mc${libs.versions.minecraft.get()}"
}

repositories {
    mavenCentral()
    maven("https://maven.shedaniel.me/") { name = "Shedaniel Maven" }
    maven("https://maven.terraformersmc.com/releases/") { name = "TerraformersMC" }
    maven("https://maven.isxander.dev/releases") { name = "Xander Maven" }
    maven("https://api.modrinth.com/maven/") {
        name = "Modrinth"
        content {
            includeGroup("maven.modrinth")
        }
    }
}

dependencies {
    minecraft(libs.minecraft)
    mappings(variantOf(libs.fabric.yarn) { classifier("v2") })
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)

    modImplementation(libs.mod.modmenu)
    modImplementation(libs.mod.yacl) {
        // was including old fapi version that broke things at runtime
        exclude(group = "net.fabricmc.fabric-api", module = "fabric-api")
    }

    if (sodiumCompatibility) {
        modImplementation(libs.mod.sodium)
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

license {
    include("**/me/denarydev/chromium/**")

    header(file("HEADER"))
    newLine(false)
}

loom {
    accessWidenerPath = file("src/main/resources/chromium.accesswidener")
}

sourceSets {
    if (sodiumCompatibility) {
        create("sodiumCompatibility") {
            java {
                compileClasspath += main.get().compileClasspath
                compileClasspath += main.get().output
            }
        }
    }

    main {
        java {
            if (sodiumCompatibility) {
                runtimeClasspath += getByName("sodiumCompatibility").output
            }
        }
    }
}

tasks {
    withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }

    withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }

    withType<ProcessResources> {
        filteringCharset = Charsets.UTF_8.name()
    }

    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand("version" to project.version)

            if (!sodiumCompatibility) {
                filter {
                    it.replace("mixins.chromium.compat.sodium.json", "mixins.empty.sodium.json")
                }
            }
        }

        if (sodiumCompatibility) {
            exclude("mixins.empty.sodium.json")
        }
    }

    jar {
        from("LICENSE")

        if (sodiumCompatibility) {
            from(sourceSets["sodiumCompatibility"].output) {
                filesMatching("*refmap.json") {
                    name = "chromium-sodium-compat-refmap.json"
                }
            }
        }
    }

    runClient {
        jvmArgs("-Dmixin.debug.export=true")
    }
}

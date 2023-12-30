plugins {
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.serialization") version "1.9.21"

    id("io.papermc.paperweight.userdev") version "1.5.9"
    id("xyz.jpenilla.run-paper") version "2.2.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"

    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "de.maxbossing"
version = 1

repositories {
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/groups/public/")
    maven("https://repo.codemc.org/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")

    bukkitLibrary(kotlin("stdlib"))
    bukkitLibrary("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

    implementation("io.github.rysefoxx.inventory", "RyseInventory-Plugin", "1.6.5")

    implementation("de.maxbossing", "mxpaper", "2.0.0")

    bukkitLibrary("dev.jorel", "commandapi-bukkit-kotlin", "9.3.0")
    bukkitLibrary("dev.jorel", "commandapi-bukkit-shade", "9.3.0")

}


tasks {
    shadowJar {
        dependencies {
            exclude("dev.jorel::")
        }
    }
}

kotlin {
    jvmToolchain(17)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

bukkit {
    name = "KillHeads"
    author = "Max Bossing <info@maxbossing.de>"
    website = "maxbossing.de"
    description = "Kill Players - Get Heads"

    apiVersion = "1.20"

    libraries = listOf(
        "dev.jorel:commandapi-bukkit-shade:9.3.0",
        "dev.jorel:commandapi-bukkit-kotlin:9.3.0"
    )


    main = "de.maxbossing.killheads.KillHeads"
}
plugins {
    alias(libs.plugins.paperweight)
    alias(libs.plugins.shadowJar)
    alias(libs.plugins.runPaper)

    `paper-plugin`
}

rootProject.version = "1.0.0"

repositories {
    maven("https://repo.codemc.io/repository/maven-releases")

    maven("https://repo.fancyplugins.de/releases")
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper)

    //implementation(libs.triumph.cmds)

    implementation(libs.vital.paper)

    //compileOnly(libs.fancy.holograms)

    compileOnly(libs.packet.events)
}

tasks {
    runServer {
        jvmArgs("-Dnet.kyori.ansi.colorLevel=truecolor")

        defaultCharacterEncoding = Charsets.UTF_8.name()

        minecraftVersion(libs.versions.minecraft.get())
    }

    shadowJar {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")
    }

    processResources {
        inputs.properties("name" to rootProject.name)
        inputs.properties("version" to project.version)
        inputs.properties("group" to project.group)
        inputs.properties("apiVersion" to libs.versions.minecraft.get())
        inputs.properties("description" to project.properties["description"])
        inputs.properties("website" to project.properties["website"])

        filesMatching("paper-plugin.yml") {
            expand(inputs.properties)
        }
    }
}
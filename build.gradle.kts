import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.gradleup.shadow") version "8.3.2"
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "org.alexdev"
version = "1.6.8"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.extendedclip.com/releases")
    maven("https://repo.minebench.de/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://libraries.minecraft.net/")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-releases/")
    maven("https://repo.viaversion.com/")
    maven("https://repo.opencollab.dev/main/")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.oraxen.com/releases")
    maven("https://repo.oraxen.com/snapshots")
    maven("https://jitpack.io")
    maven("https://repo.viaversion.com")
    maven("https://maven.evokegames.gg/snapshots")
    maven("https://repo.alessiodp.com/releases")
    maven("https://maven.typewritermc.com/beta")
    maven("https://repo.nexomc.com/snapshots/")
    maven("https://repo.nexomc.com/releases")
    maven("https://repo.hibiscusmc.com/releases")
    maven("https://repo.md-5.net/content/groups/public/")
}

dependencies {

    compileOnly(libs.paperApi)
    compileOnly(libs.adventureApi)
    implementation(libs.entityLib)
    compileOnly(libs.typeWriter) {
        exclude(group = "io.papermc.paper") // Exclude Paper API
        exclude(group = "com.github.Tofaa2.EntityLib") // Exclude EntityLib
        exclude(group = "me.tofaa.entitylib") // Exclude EntityLib
    }
    compileOnly(libs.placeholderapi)
    compileOnly(libs.miniplaceholdersApi)
    compileOnly(libs.floodgateApi)
    compileOnly(libs.geyserApi)
    compileOnly(libs.commonsLang)
    implementation(libs.configlib)
    implementation(libs.configlibPaper)
    compileOnly(libs.packeteventsSpigot)
    compileOnly(libs.viaVersionApi)
    compileOnly(libs.bstatsBukkit)
    compileOnly(libs.expiringMap)
    compileOnly(libs.commonsJexl3)
    compileOnly(libs.nexo)
    compileOnly(libs.oraxen)
    compileOnly(libs.itemsAdder)
    compileOnly(libs.hmccosmetics)
    compileOnly(libs.creative.rp)
    compileOnly(libs.creative.serializer)
    compileOnly(libs.libs.disguises)

    implementation(libs.minedownAdventure)
    implementation(libs.drink)
    implementation(libs.universalScheduler)
    implementation(libs.libbyBukkit)

    compileOnly(libs.gson)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}

tasks.named<ShadowJar>("shadowJar") {
    val relocation = "org.alexdev.unlimitednametags.libraries."
    relocate("net.byteflux.libby", relocation + "libby.bukkit")
    relocate("org.jetbrains", relocation + "jetbrains")
    relocate("org.intellij", relocation + "intellij")
    relocate("de.themoep", relocation + "themoep")
    relocate("me.tofaa.entitylib", relocation + "entitylib")
    relocate("com.jonahseguin.drink", relocation + "drink")
    relocate("javax.annotation", relocation + "annotation")
    relocate("com.github.Anon8281.universalScheduler", relocation + "universalScheduler")
    relocate("net.kyori.adventure.text.serializer", "io.github.retrooper.packetevents.adventure.serializer")
    relocate("net.byteflux.libby", relocation + "libby.bukkit")

    //Nexo
//    val nexoRelocation = "com.nexomc.libs"
//    relocate("team.unnamed", nexoRelocation)

    dependencies {
        exclude(dependency(":kotlin-stdlib"))
        exclude(dependency(":slf4j-api"))
        exclude(dependency("com.google.code.gson:gson"))
        exclude(dependency("net.kyori:adventure-api"))
        exclude(dependency("net.kyori:adventure-key"))
        exclude(dependency("net.kyori:adventure-nbt"))
        exclude(dependency("net.kyori:option"))
        exclude(dependency("net.kyori:examination-api"))
        exclude(dependency("net.kyori:examination-string"))
        exclude(dependency("net.kyori:text"))
        exclude(dependency("net.kyori:adventure-text-serializer-gson"))
        exclude(dependency("net.kyori:adventure-text-serializer-json"))
    }

    //mappings
    exclude("assets/mappings/block/**")
    exclude("assets/mappings/stats/**")
    exclude("assets/mappings/particle/**")
    exclude("assets/mappings/enchantment/**")

    destinationDirectory.set(file("$rootDir/target"))
    archiveFileName.set("${project.name}.jar")

    minimize()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(17)

}
tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))

    disableAutoTargetJvm()
}

tasks.named<Jar>("jar").configure {
    dependsOn("shadowJar")
}
tasks.named<Delete>("clean").configure {
    delete(tasks.named<ShadowJar>("shadowJar").get().archiveFile)
}

tasks.jar {
    manifest {
        attributes["paperweight-mappings-namespace"] = "mojang"
    }
}

tasks {
    runServer {
        minecraftVersion("1.21.4")

        downloadPlugins {
            hangar("PlaceholderAPI", "2.11.6")
            modrinth("luckperms", "v5.4.145-bukkit")
            modrinth("multiverse-core", "4.3.14")
            url("https://github.com/retrooper/packetevents/releases/download/v2.8.0/packetevents-spigot-2.8.0.jar")
            github("ViaVersion", "ViaVersion", "5.1.1", "ViaVersion-5.1.1.jar")
            github("ViaVersion", "ViaBackwards", "5.1.1", "ViaBackwards-5.1.1.jar")
            github("MilkBowl", "Vault", "1.7.3", "Vault.jar")
//            github("gecolay", "GSit", "1.11.2", "GSit-1.11.2.jar")
//            url("https://github.com/EssentialsX/Essentials/releases/download/2.20.1/EssentialsX-2.20.1.jar")
        }
    }
    runPaper.folia.registerTask {
        minecraftVersion("1.21.1")

        downloadPlugins {
            github("Anon8281", "PlaceholderAPI", "2.11.7", "PlaceholderAPI-2.11.7-DEV-Folia.jar")

            url("https://cdn.modrinth.com/data/HQyibRsN/versions/J2guR3GH/MiniPlaceholders-Paper-2.2.4.jar")
            url("https://ci.lucko.me/job/LuckPerms-Folia/lastSuccessfulBuild/artifact/bukkit/loader/build/libs/LuckPerms-Bukkit-5.4.141.jar")
            url("https://github.com/retrooper/packetevents/releases/download/v2.4.0/packetevents-spigot-2.4.0.jar")
            github("ViaVersion", "ViaVersion", "5.0.1", "ViaVersion-5.0.1.jar")
        }
    }
}

tasks.processResources {
    var compiled = true
    if (file("license.txt").exists()) {
        compiled = false
    }

    from("src/main/resources") {
        include("plugin.yml")
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        expand(
            "configlibVersion" to libs.versions.configlibVersion.get(),
            "expiringMapVersion" to libs.versions.expiringMapVersion.get(),
            "commonsJexl3Version" to libs.versions.commonsJexl3Version.get(),

            "version" to version,
            "compiled" to compiled
        )
    }

}

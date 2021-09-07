import net.minecraftforge.gradle.userdev.DependencyManagementExtension
import net.minecraftforge.gradle.userdev.UserDevExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.30"
}

buildscript {
    repositories {
        maven { url = uri("https://maven.siro256.dev/repository/maven-public/") }
    }

    dependencies {
        classpath("net.minecraftforge.gradle:ForgeGradle:5.1.+") {
            isChanging = true
        }
    }
}

apply(plugin = "net.minecraftforge.gradle")

//Change this
group = "dev.siro256.rtmpack.template"
version = "1.0.0-SNAPSHOT"

repositories {
    maven { url = uri("https://maven.siro256.dev/repository/maven-public/") }
    maven { url = uri("https://cursemaven.com") }
}

dependencies {
    val fgDepManager = project.extensions[DependencyManagementExtension.EXTENSION_NAME] as DependencyManagementExtension

    //kotlin-stdlib
    api(kotlin("stdlib"))

    //Forge
    add("minecraft", "net.minecraftforge:forge:1.12.2-14.23.5.2855")

    //NGTLib and RTM
    //If you want to update, see https://www.cursemaven.com/
    implementation(fgDepManager.deobf("curse.maven:ngtlib-288989:3387256"))
    implementation(fgDepManager.deobf("curse.maven:realtrainmod-288988:3387261"))
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
}

configure<UserDevExtension> {
    mappings("snapshot", "20171003-1.12")
}

tasks {
    withType<KotlinCompile>().configureEach {
        // Common options
        //If you are leniency to yourself, set this to false.
        kotlinOptions.allWarningsAsErrors = true

        // Kotlin/JVM compiler options
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<Jar> {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        archiveExtension.set("zip")

        from(configurations.api.get().apply { isCanBeResolved = true }.map { if (it.isDirectory) it else zipTree(it) })
    }
}

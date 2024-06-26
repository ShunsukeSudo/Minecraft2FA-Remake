plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    kotlin("jvm") version "1.9.23"
}

repositories {
    mavenCentral()
}

val pluginVersion = "0.0.1"

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "java")
    java.sourceCompatibility = org.gradle.api.JavaVersion.VERSION_17

    ext {
        project.ext["groupID"] = "com.github.shunsukesudo"
        project.ext["authorName"] = "s.s"
        project.ext["pluginVersion"] = pluginVersion
        project.ext["baseServerAPIVersion"] = "1.20"
        project.ext["baseProxyAPIVersion"] = "1.20"
    }

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.f2a.dev/")
    }

    dependencies {
        implementation("net.dv8tion:JDA:5.0.0-beta.21")
        implementation("dev.creativition:simplejdautil:0.0.2")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("net.kyori:adventure-platform-bungeecord:4.3.0")
        implementation("net.kyori:adventure-platform-bukkit:4.3.0")
        implementation(kotlin("stdlib-jdk8"))
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    }

    tasks.getByName<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveBaseName.set("Minecraft2FA-${project.name}")
        archiveClassifier.set("splitted")
        archiveVersion.set(pluginVersion)
    }

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
    }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation(project(path = ":waterfall", configuration = "shadow"))
    implementation(project(path = ":paper", configuration = "shadow"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.getByName<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveBaseName.set("Minecraft2FA")
    archiveClassifier.set("universal")
    archiveVersion.set(pluginVersion)
}
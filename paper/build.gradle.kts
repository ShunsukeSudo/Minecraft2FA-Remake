plugins {
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
}

dependencies {
    implementation(project(":shared"))
    compileOnly("io.papermc.paper:paper-api:${project.ext["baseServerAPIVersion"]}-R0.1-SNAPSHOT")
}

bukkit {
    name = rootProject.name
    main =  "${project.ext["groupID"]}.minecraft2fa.paper.Minecraft2FA"

    apiVersion = "1.13"

    load = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder.STARTUP
    author = project.ext["authorName"].toString()
    prefix = "MC2FA"
    version = project.ext["pluginVersion"].toString()


    commands {
        register("mc2fa") {
            description = "Used for integrating discord and minecraft account"
            permission = "mc2fa.connect"
            usage = ""
            permissionMessage = "You don't have permission to perform this command."
        }
    }


    permissions {
        register("mc2fa.*") {
            children = listOf("mc2fa.connect")

        }

        register("mc2fa.connect") {
            description = "Discord integration command permission"
            default = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.OP
        }
    }
}
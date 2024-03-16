plugins {
    id("net.minecrell.plugin-yml.bungee") version "0.5.2"
}

dependencies {
    implementation(project(":shared"))
    compileOnly("io.github.waterfallmc:waterfall-api:${project.ext["baseProxyAPIVersion"]}-R0.1-SNAPSHOT")
}

bungee {
    main = "${project.ext["groupID"]}.minecraft2fa.waterfall.Minecraft2FA"
    author = project.ext["authorName"].toString()
    version = project.ext["pluginVersion"].toString()
}
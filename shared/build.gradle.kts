repositories {
    maven("https://dl.bintray.com/kotlin/exposed/")
}

val exposedVersion= "0.48.0"

dependencies{
    compileOnly("io.github.waterfallmc:waterfall-api:${project.ext["baseProxyAPIVersion"]}-R0.1-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:${project.ext["baseServerAPIVersion"]}-R0.1-SNAPSHOT")
    implementation("mysql:mysql-connector-java:8.0.15")
    implementation("org.xerial:sqlite-jdbc:3.45.2.0")
    implementation("com.warrenstrange:googleauth:1.5.0")
    implementation("com.google.zxing:core:3.5.0")
    implementation("com.google.zxing:javase:3.5.0")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
}
dependencies {
    implementation(project(":api"))
    implementation(project(":common"))

    implementation("net.kyori:adventure-text-minimessage:4.13.1")
    implementation("net.kyori:adventure-text-serializer-legacy:4.13.1")
    implementation("net.kyori:adventure-platform-bukkit:4.3.0")

    annotationProcessor("org.projectlombok:lombok:1.18.22")

    compileOnly("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.22")
    compileOnly("org.jetbrains:annotations:16.0.2")
}
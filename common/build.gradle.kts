dependencies {
    implementation(project(":api"))

    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("mysql:mysql-connector-java:8.0.32")
    implementation("com.moandjiezana.toml:toml4j:0.7.2")
    implementation("net.kyori:adventure-text-minimessage:4.13.1")
    implementation("net.kyori:adventure-text-serializer-legacy:4.13.1")
    implementation("net.kyori:adventure-platform-bukkit:4.3.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.21")



    annotationProcessor("org.projectlombok:lombok:1.18.22")
    compileOnly("org.projectlombok:lombok:1.18.22")
    compileOnly("org.jetbrains:annotations:16.0.2")
}
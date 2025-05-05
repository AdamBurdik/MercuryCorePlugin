plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.14"
    id("com.gradleup.shadow") version "8.3.0"
}

group = "me.adamix.mercury.core"
version = "0.3.3"

var lampVersion = "4.0.0-rc.9"
var mercuryApiCommit = "ae14b92eba"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/") }
    maven { url = uri("https://repo.dmulloy2.net/repository/public/") }
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
    implementation("com.marcusslover:plus:4.3.3-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.3.0")
    implementation("org.luaj:luaj-jse:3.0.1")

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    implementation("io.github.revxrsal:lamp.common:${lampVersion}")
    implementation("io.github.revxrsal:lamp.bukkit:${lampVersion}")
    implementation("io.github.revxrsal:lamp.brigadier:${lampVersion}")

    implementation("it.unimi.dsi:fastutil:8.5.15")

    implementation(platform("org.dizitart:nitrite-bom:4.3.0"))
    implementation("org.dizitart:nitrite")
    implementation("org.dizitart:nitrite-mvstore-adapter")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("com.github.AdamBurdik:MercuryAPI:${mercuryApiCommit}")
    implementation("com.github.AdamBurdik:MercuryCommon:df6607fc5f")
}

tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    archiveClassifier = ""
    archiveVersion.set(project.version.toString())
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}
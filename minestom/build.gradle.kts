plugins {
    kotlin("jvm")
    `java-library`
}

group = "org.readutf.arena"
version = "1.1.0"

repositories {
    mavenCentral()
    maven {
        name = "utfRepoReleases"
        url = uri("https://mvn.utf.lol/releases")
    }
}

dependencies {
    testImplementation(kotlin("test"))

    compileOnly("net.minestom:minestom-snapshots:96cedb1bab")
    compileOnly("org.readutf.arena:core:1.1.0")

    api(project(":core"))

    api("dev.hollowcube:schem:2.0.1")
    api("dev.hollowcube:polar:1.12.2")

    // slf4j
    api("org.slf4j:slf4j-api:2.0.16")

    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.2")

    api("io.github.revxrsal:lamp.common:4.0.0-rc.8")
    api("io.github.revxrsal:lamp.minestom:4.0.0-rc.8")

    api("net.kyori:adventure-text-minimessage:4.18.0")
    api("net.kyori:adventure-text-serializer-legacy:4.18.0")

    api("net.bladehunt:kotstom:0.4.0-beta.0")
    implementation("io.github.togar2:minestompvp:1.0.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}

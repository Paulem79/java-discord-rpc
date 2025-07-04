plugins {
    id("idea")
    id("java")
    `maven-publish`
    id("com.gradleup.shadow") version "8.+"
}

group = "club.minnced"
version = "2.0.3"

sourceSets {
    create("examples") {
        compileClasspath += sourceSets["main"].output
        runtimeClasspath += sourceSets["main"].output
    }
}

repositories {
    mavenCentral()
    mavenLocal()

    maven {
        url = uri("https://jitpack.io")
        name = "JitPack"
    }
    maven {
        url = uri("https://litarvan.github.io/maven")
        name = "LitarvanMaven"
    }
    maven {
        url = uri("https://maven.scijava.org/content/repositories/public/")
        name = "ClubMinnced"
    }
    maven {
        name = "paulemReleases"
        url = uri("https://maven.paulem.ovh/releases")
    }
}

dependencies {
    implementation("net.java.dev.jna:jna:4.4.0")
    implementation("club.minnced:discord-rpc-release:v3.3.0")
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
    withJavadocJar()
}

tasks.shadowJar {
    archiveClassifier.set("")
}

tasks.withType<JavaCompile> {
    options.isIncremental = true
    options.encoding = "UTF-8"
}

// Publication
tasks.jar {
    manifest {
        attributes(
            "Implementation-Version" to version,
            "Target-Platforms" to "win32-x86-64, win32-x86, linux-x86-64, darwin"
        )
    }
}

publishing {
    repositories {
        maven {
            name = "paulem"
            url = uri("https://maven.paulem.ovh/releases")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])
        }
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
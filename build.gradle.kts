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

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
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

tasks.withType<JavaCompile> {
    options.isIncremental = true
    options.encoding = "UTF-8"
}

tasks.build {
    dependsOn(tasks.jar)
    dependsOn(tasks.shadowJar)
    tasks.jar { mustRunAfter(tasks.clean) }
}

// Publication

val sources by tasks.registering(Copy::class) {
    from("src/main/java")
    into("${layout.buildDirectory}/sources")
}

tasks.classes {
    dependsOn(sources)
}

tasks.jar {
    archiveBaseName.set(project.name)
    manifest {
        attributes(
            "Implementation-Version" to version,
            "Target-Platforms" to "win32-x86-64, win32-x86, linux-x86-64, darwin"
        )
    }
    dependsOn(sources)
}

tasks.javadoc {
    options.encoding = "UTF-8"
    dependsOn(sources)
    source = fileTree(sources.get().destinationDir)
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from("$buildDir/sources")
    dependsOn(tasks.classes)
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.javadoc.get().destinationDir)
    dependsOn(tasks.javadoc)
}

tasks.build {
    dependsOn(tasks.jar)
    dependsOn(javadocJar)
    dependsOn(sourcesJar)
    dependsOn(tasks.shadowJar)

    tasks.jar {
        mustRunAfter(tasks.clean)
    }
    javadocJar {
        mustRunAfter(tasks.jar)
    }
    sourcesJar {
        mustRunAfter(javadocJar)
    }
    tasks.shadowJar {
        mustRunAfter(sourcesJar)
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
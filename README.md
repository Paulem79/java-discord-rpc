[bintray-version]: https://api.bintray.com/packages/minndevelopment/maven/java-discord-rpc/images/download.svg
[bintray-download]: https://bintray.com/minndevelopment/maven/java-discord-rpc/_latestVersion
[jitpack-version]: https://jitpack.io/v/MinnDevelopment/java-discord-rpc.svg
[jitpack-setup]: https://jitpack.io/#MinnDevelopment/java-discord-rpc

# NOTICE

Message from the original author:<br>
This library is discontinued since the original project [discord-rpc](https://github.com/discord/discord-rpc) is no longer being maintained. Discord now expects you to use the [Game SDK](https://discord.com/developers/docs/game-sdk/sdk-starter-guide) which is not open source and will not be wrapped by this project. More information on the deprecation notice can be found here: [Deprecation and Migration to Discord GameSDK](https://github.com/discord/discord-rpc/issues/290)

Message from Paulem :<br>
*Actually, this library still works and I wanted to keep it usable and accessible to anyone who needs it*

# java-discord-rpc

This library contains Java bindings for [Discord's official RPC SDK](https://github.com/discord/discord-rpc) using JNA.

This project provides binaries for `linux-x86-64`, `win32-x86-64`, `win32-x86`, and `darwin`.
The binaries can be found at [MinnDevelopment/discord-rpc-release](https://github.com/MinnDevelopment/discord-rpc-release)

## Documentation

You can see the official discord documentation in the [API Documentation](https://discordapp.com/developers/docs/rich-presence/how-to).
<br>Alternatively you may visist the javadoc at [the maven repo](https://maven.paulem.ovh/javadoc/releases/club/minnced/java-discord-rpc/2.0.3).

## Setup

In the follwing please replace `%VERSION%` with the version listed above.

### Gradle

**Repository**

```gradle
repositories {
    jcenter()
}
```

**Artifact**

```gradle
dependencies {
    compile 'club.minnced:java-discord-rpc:%VERSION%'
}
```

### Maven

**Repository**

```xml
<repository>
    <id>paulem-releases</id>
    <name>Paulem's Repo</name>
    <url>https://maven.paulem.ovh/releases</url>
</repository>
```

**Artifact**

```xml
<dependency>
    <groupId>club.minnced</groupId>
    <artifactId>java-discord-rpc</artifactId>
    <version>%VERSION%</version>
</dependency>
```

### Compile Yourself

1. Install git and JDK 8+
2. `git clone https://github.com/minndevelopment/java-discord-rpc`
3. `cd java-discord-rpc`
4. `./gradlew build` or on windows `gradlew build`
5. Get the jar from `build/libs` with the name `java-discord-rpc-%VERSION%.jar`

## Examples

### Basics

The library can be used just like the SDK. This means you can almost copy the exact code used in the official documentation.

```java
import club.minnced.discord.rpc.*;

public class Main {
    public static void main(String[] args) {
        DiscordRPC lib = DiscordRPC.INSTANCE;
        String applicationId = "";
        String steamId = "";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = (user) -> System.out.println("Ready!");
        lib.Discord_Initialize(applicationId, handlers, true, steamId);
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
        presence.details = "Testing RPC";
        lib.Discord_UpdatePresence(presence);
        // in a worker thread
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }, "RPC-Callback-Handler").start();
    }
}
```

## License

java-discord-rpc is licensed under the Apache 2.0 License. The base DiscordRPC is licensed under the MIT license.

## Contributing

Find something that is lacking? Fork the project and pull request!

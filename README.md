# jobs-reborn-patch-place-break
![Target](https://img.shields.io/badge/plugin-Minecraft-blueviolet)
![Minecraft version](https://img.shields.io/badge/version-1.8%20--%201.19-blue)
[![CircleCI](https://dl.circleci.com/status-badge/img/gh/Djaytan/mc-jobs-reborn-patch-place-break/tree/main.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/Djaytan/mc-jobs-reborn-patch-place-break/tree/main)

A place-and-break patch extension of [JobsReborn plugin](https://www.spigotmc.org/resources/jobs-reborn.4216/)
for Bukkit servers.

The ressource is available in [Spigot](https://www.spigotmc.org/resources/jobsreborn-patchplacebreak.102779/) and [PaperMC](https://forums.papermc.io/threads/jobsreborn-patchplacebreak.367/).

## Place-and-break issue

With JobsReborn, it appears that placing a block and then breaking it is counted as a valid job
action which lead to a payment for the player. Given this fact, it's very easy to imagine a
diamond ore to be gathered with a Silk Touch pickaxe and immediately after be replaced to
repeat the process again and again...

A solution with JobsReborn is to remove money and xp when a diamond ore is placed, preventing
the previously described scenario. But it isn't perfect: if you expect to use money and xp boost
for whatever reason, the amount of money to give when the block is broken will be higher than
the amount to be retrieved when placing the same one. And... well... losing money and xp when you
place a block for decoration isn't very appreciated by players too.

The place-and-break patch provided by JobsReborn seems to be insufficient: you must specify **for each
block** a fix amount of time during which breaking the block again will not permit to earn money
and xp. It's a first step forward, but it's insufficient. Specifying an unlimited time isn't
sufficient too, because after 14 days maximum the placed blocks will not prevent the payment
anymore... And finally, piston exploit isn't taking into account at all.

So, this is why this project exists: doing an obvious and easy patch that would have been done
a long time ago.

## How the patch works

The patch is very simple: when breaking or placing blocks, each one is tagged. This information
is persisted across server restarts.

At payment time, if a BREAK, TNTBREAK or PLACE action involve an active "player" tag, the payment
will be cancelled. It doesn't matter whose player is the author, so if one player place a block
and another one break it, the payment will be cancelled anyway.

There are two main behaviors which have subtle differences:
* When a block is placed, a tag is attached to it: This is useful to patch BREAK and TNTBREAK
  actions (e.g. for breaking diamond ores) ;
* When a block is broken, a tag is attached to the location where it was: This is useful to
  patch PLACE actions (e.g. for placing saplings).

*Note: the second behavior lead to "ephemeral" tags, that's to say a tag which will be
applicable during a short-time only. The value is fixed to three seconds.*

As a comparison point, this behavior can have similarities with the one implemented by
[mcMMO](https://www.spigotmc.org/resources/official-mcmmo-original-author-returns.64348/) plugin.

Easy and efficient, this does the trick.

## Setup

We expect here that you already have a Bukkit server already set up with the JobsReborn plugin
installed on it.

Simply download the latest `.jar` file from the
[release section](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/releases/) of this
repository and put it into the `plugins/` folder, and you'll be done! After restarting the server,
the plugin should now appear green in the list displayed by the `/plugins` command.

At this point, you should simply turn off all options of the "PlaceAndBreak" config part of JobsReborn.
This would lead to a similar result as the following one:

```yaml
PlaceAndBreak:
  # Enable blocks protection, like ore, from exploiting by placing and destroying same block again and again.
  # Modify restrictedBlocks.yml for blocks you want to protect
  Enabled: false
  # Enabling this we will ignore blocks generated in ore generators, liko stone, coublestone and obsidian. You can still use timer on player placed obsidian block
  IgnoreOreGenerators: true
  # For how long in days to keep block protection data in data base
  # This will clean block data which ones have -1 as cooldown value
  # Data base cleanup will be performed on each server startup
  # This cant be more then 14 days
  KeepDataFor: 14
  # All blocks will be protected X sec after player places it on ground.
  GlobalBlockTimer:
    Use: false
    Timer: 3
  # Enable silk touch protection.
  # With this enabled players wont get paid for broken blocks from restrictedblocks list with silk touch tool.
  SilkTouchProtection: false
```

## How to compile the project

Since a [Maven Wrapper](https://github.com/apache/maven-wrapper) has been set up for this project, it's recommended
to use it instead of your own local instance by executing the script `mvnw`
(or `mvnw.cmd` if you are under Windows terminal of Powershell).

To compile the project, run unit tests and package the plugin into a Jar file, simply run this command
without any arguments: `mvnw`
(see [Maven lifecycle](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html) for more details).
This is equivalent to `mvnw clean install`.

The environment variable `JAVA_1_17_HOME` must be defined by targeting a JDK 17 home directory. This is required since
the cross-compilation feature of `javac` is needed to target version 8 of Java whereas we are using libraries compiled
with a later Java version (for example PaperMC 1.18.2 which as been compiled with JDK 17).
Typically, this plugin need to be executed in 1.8 Minecraft servers under a Java 8 VM.

***Take care:** the Maven compiler plugin for Java doesn't provide clear error message if the environment variable
isn't defined or target a wrong JDK version. So, in case you have any unidentifiable issue when compiling the project,
take a look at this section first by ensuring the set-up is right.*

## Licence

This project is under the licence [GNU GPLv3](https://www.gnu.org/licenses/gpl-3.0.en.html).

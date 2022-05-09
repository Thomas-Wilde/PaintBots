<div id="top"></div>

<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->

![Build][gradle-build-badge]
<!-- [![Doxygen-Docu][doxygen-badge]][doxygen-url] -->

[gradle-build-badge]: https://github.com/Thomas-Wilde/PaintBots/actions/workflows/gradle.yml/badge.svg
<!--[doxygen-badge]: https://github.com/Thomas-Wilde/PaintBots/actions/workflows/doxygen.yml/badge.svg-->
[doxygen-url]: https://thomas-wilde.github.io/PaintBots/
[paintbots-release]: https://thomas-wilde.github.io/PaintBots/release
[aud]: https://aud.vc.cs.ovgu.de

# Paint Bots

This repository contains the source code of the **Paint Bots** game. The game
is part of a programming contest. The contest is organized for the lecture
[*Algorithmen & Datenstrukturen*][aud] at the Otto-von-Guericke University in
Magdeburg in the summer term of 2022.

## The Contest

For some more information about the contest you can read the following documents
(currently only available in German):

- [The intro](docs/intro.md)
- [Information about creating levels](docs/levels.md)
- [Information about creating bots](docs/bot.md)

## Release & Documentation
You can find the current release [here][paintbots-release]. The corresponding source
code documentation is [here][doxygen-url].

## Compile the Project
PaintBots uses [libGDX] a Java based game engine. To compile
the project you have to install a Java SDK. [libGDX] is
compatible with Java 13 or less -- there is a problem with
newer version. The git-repository contains a automatic build
pipeline using Java 11, so if everything else does not work,
Java 11 should definitely do. PaintBots uses the [gradle] build
tool. Everything you should do is clone the repository,
build the code and run the game. The following lines should
do the job:

```
git clone git@github.com:Thomas-Wilde/PaintBots.git
cd PaintBots
./gradlew desktop:run
```

[gradle]: https://gradle.org
[libGDX]: https://libgdx.com/
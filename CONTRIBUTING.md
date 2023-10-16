# Contributing

When contributing to this repository, please first discuss the change you wish to make via issues,
discussions, or any other method with the owners of this repository before making a change.

Please note we have a [code of conduct](CODE_OF_CONDUCT.md), please follow it in all your
interactions with the project.

## 🌱 How to contribute

A contribution can be as simple as opening a discussion or reporting us an issue, an idea of
enhancement or a code improvement.

No matter of your capabilities or how important is your wish to contribute to this project, your
help will be welcome and very appreciated!

### 💭 Discussions

Discussions are where we have conversations.

If you'd like to help troubleshooting a PR you're working on, have a great new idea,
or want to share something amazing you've experimented with our product,
join us in [discussions](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/discussions).

### 🐛 Issues

[Issues](https://docs.github.com/en/github/managing-your-work-on-github/about-issues) are used to
track tasks that contributors can help with.
If an issue doesn't have any label, this means we haven't reviewed it yet,
and you shouldn't begin to work on it.

If you've found a bug, a weird behavior or an exploit,
search open issues to see if someone else has reported the same thing.
If it's something new, open an issue using
a [template](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/new/choose).
We'll use the issue to have a conversation about the problem you want to be fixed.

### 🛠️ Pull requests

A [pull request](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/about-pull-requests)
is a way to suggest changes in our repository.
When we merge those changes, they would be deployed in the next release by the maintainer. 🌍

If this is your first pull request (PR), we recommend you to get familiar with the process through
this [GitHub repository](https://github.com/firstcontributions/first-contributions).

When considering contributing to the project through PRs, please follow these guidelines:

1. Try to open several smaller PRs instead of only a big one, it will make the job of the reviewers
   easier.
2. Give a summary of the changes provided by your PR.
   Link any related issue, discussion or documentation that could help the reviewer understand your
   work, the impacts and the plus-value.
3. You will need at least one approval of a reviewer before being able to merge the PR.
4. All automatic jobs must pass (GitHub Actions, SonarQube analysis, security scans, formatting
   verification, ...) before merging.

If you are working on Bukkit related parts of the project, then you may find useful to consult these
documentations if required:

* [The Spigot API JavaDoc](https://hub.spigotmc.org/javadocs/spigot/)
* [The Bukkit Dev Wiki](https://bukkit.fandom.com/wiki/Main_Page)

### ❓ Support

Asking questions is a way for you to be unblocked, and by doing so,
this could help other people too if they are interested in the answer!
Thus, the best place for asking support is
under [discussions](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/discussions).

Furthermore, rest assured that the community will try to find the best way to help you! ✨

## 🔰 Getting started

These instructions will get you a copy of the project up and running on your local machine for
development and testing purposes.

### 📝 Prerequisites

Working on this project requires the following dependencies installed in your local environment:

* JDK 11 ([Download Link](https://adoptium.net/en-GB/temurin/releases/?version=11))
* Maven
  3.6+ ([Download Link](https://maven.apache.org/download.cgi) | [Install Guide](https://maven.apache.org/install.html))

### 🔌 Installing

We suppose you know how to install a Minecraft server.

To build the project and run unit and integration tests, you can simply build the project by typing
this command at the root project:

    $ mvn

This is the command you will run most of the time.
You will find the packaged JAR file of the Bukkit plugin at `bukkit/bukkit-plugin/target`.
If you want to test it, copy it into your local Spigot server by taking care that the server
version is compatible with the plugin.

If you want to quickly package the Jar file without running tests and other time-consuming steps:

    $ mvn -Pfast

However, it's recommended to use it as little as possible.
Never use it for validating changes before committing.

### ✏️ Code Formatting

The only thing we ask when contributing to the code is to apply
[Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).

To help respect this, an automatic formatter has been configured through Maven to be dispatched
automatically when building the project and running tests.

#### IntelliJ IDEA plugin

If you are using IntelliJ IDEA, you can install the
[google-java-format plugin](https://plugins.jetbrains.com/plugin/8527-google-java-format) which will
replace the default IDE code formatting behavior.

## 🔧 Troubleshooting

### ⚠️ Windows Long Paths

> "Git has a limit of 4096 characters for a filename, except on Windows when Git is compiled with
> msys.
> It uses an older version of the Windows API, and there's a limit of 260 characters for a
> filename." - _From StackOverflow answer: https://stackoverflow.com/a/22575737_

Since some files in project exceed 265 characters long, it is required to overcome this limitation.

To solve the issue, run with administrator rights the following command:

```
$ git config --system core.longpaths true
```

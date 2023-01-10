# Contributing

When contributing to this repository, please first discuss the change you wish to make via issues,
discussions, or any other method with the owners of this repository before making a change.

Please note we have a [code of conduct](CODE_OF_CONDUCT.md), please follow it in all your
interactions with the project.

## How To Contribute

A contribution can be as simple as opening a discussion or reporting us an issue, an idea of
enhancement or a code improvement. More details can be found about the
[types of contributions](docs/types-of-contributions.md) for this repo.

No matter of your capabilities or how important is your wish to contribute on this project, your
help will be the welcome and very appreciated!

## First Contribution

If this is your first contribution, we recommend you to get familiar with the process through
this [GitHub repository](https://github.com/firstcontributions/first-contributions).

## Getting Started

Actually, there isn't documentation which will permit you to navigate in our codebase with
confidence. Fortunately, one will be added soon:
https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/46

If you want to work on Bukkit related aspects, you will find the API JavaDoc
[here](https://hub.spigotmc.org/javadocs/spigot/).

A [wiki](https://bukkit.fandom.com/wiki/Main_Page) exists as well to get familiar with Bukkit
core concepts.

Anyway, if you have any question, don't hesitate to
[ask us](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/discussions)! We are here
to help you for getting started.

## Pull Requests

1. Try to open several smaller PRs instead of only a big one, it will make the job of the reviewers
easier.
2. Give a summary of the changes provided by your PR. Link any related issue, discussion or documentation
that could help the reviewer understand your work, the impacts and the plus-value.
3. You will need at least one approval of a reviewer before being able to merge the PR.
4. All automatic jobs must pass (CircleCI, SonarQube analysis, formatting verification, ...) before
merging.

## Code Formatting

The only thing we ask when contributing to the code is to apply
[google-java-format](https://google.github.io/styleguide/javaguide.html) style guidelines.

To help respecting this, an automatic-formatter as been configured through Maven to be dispatched
automatically when building the project and running tests. If you forgot to run Maven before
committing and pushing your changes, your PR will be prevented from being merged by our CI until
you solve the issue.
Unfortunately, the current automatic-formatter doesn't respect very well google-java-format style
guidelines and this is why
[an improvement is planed for this](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/40).

If you are using IntelliJ IDEA, you can as well install the
[google-java-format plugin](https://plugins.jetbrains.com/plugin/8527-google-java-format) which will
replace the default IDE code formatting behavior. However, because imports order aren't managed
by the plugin, we recommend you to follow
[these steps](https://github.com/google/google-java-format#intellij-android-studio-and-other-jetbrains-ides).

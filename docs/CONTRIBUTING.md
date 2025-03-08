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

* [The PaperMC API JavaDoc](https://papermc.io/javadocs)
* [The PaperMC Development Guide](https://docs.papermc.io/paper/dev)

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

* JDK 17 ([Download Link](https://adoptium.net/en-GB/temurin/releases/?version=17))
* Maven
  3.8+ ([Download Link](https://maven.apache.org/download.cgi) | [Install Guide](https://maven.apache.org/install.html))
* Docker v24.0+ (on Windows it's possible to use Docker Desktop, Podman Desktop or Rancher Desktop:
  choose whatever you want or any other alternative if you like)
  * _Required for running integration tests_
* Testcontainers reusable containers feature
  enabled ([how to](https://java.testcontainers.org/features/reuse/))
  * On Linux you can run the following command:
  ```bash
  echo 'testcontainers.reuse.enable=true' >> ~/.testcontainers.properties
  ```

### 🔌 Installing

We suppose you know how to install a Minecraft server.

To build the project and run unit and integration tests, you can simply build the project by typing
this command at the root project:

    $ mvn

This is the command you will run most of the time.
You will find the packaged JAR file of the PaperMC plugin at `src/paper-plugin/target`.
If you want to test it, copy it into your local PaperMC server by taking care that the server
version is compatible with the plugin.

### 📋 Test-Driven Development (TDD)

The [TDD](https://en.wikipedia.org/wiki/Test-driven_development) is a great way to improve software
quality and maintainability by writing tests first.

A good way to adopt this approach is by relying on
the ["red, green, refactor"](https://www.codecademy.com/article/tdd-red-green-refactor) framework.

Each source code change must add, update or remove tests while ensuring a minimal code coverage of
80% is respected.
Meeting these requirements is required for each PR before being merged.

### ✏️ Code Formatting

The only thing we ask when contributing to the code is to apply
[Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).

To help respect this, an automatic formatter has been configured through Maven to be dispatched
automatically when building the project and running tests.

#### IntelliJ IDEA plugin

If you are using IntelliJ IDEA, you can install the
[google-java-format plugin](https://plugins.jetbrains.com/plugin/8527-google-java-format) which will
replace the default IDE code formatting behavior.

### ⭕ Conventional Commits

Following a standard for commit message provides several benefits:

* Readable for both humans **and** machines
* Automatic version bump
* Automatic release note creation/update

This is why we strive to follow
the [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) one.

We took inspiration
from [Angular project](https://github.com/angular/angular/blob/main/CONTRIBUTING.md#type).

#### Commit Message Header

```
<type>(<scope>): <short summary>
  │       │             │
  │       │             └─⫸ Summary in present tense. Not capitalized. No period at the end.
  │       │
  │       └─⫸ Commit Scope: core|paper-adapter|paper-plugin|readme|contributing|packaging|deps|
  │                          other|github|renovate|release
  │
  └─⫸ Commit Type: feat|fix|perf|refactor|docs|test|build|ci|chore
```

The `<type>` and `<summary>` fields are mandatory, the `(<scope>)` field is optional.

##### Type

It must be one of the following:

* **feat**: A new feature
* **fix**: A bug fix
* **perf**: A code change that improves performance
* **refactor**: A code change that neither fixes a bug nor adds a feature
* **docs**: Documentation only changes
* **test**: Adding missing tests or correcting existing tests
* **build**: Changes that affect the build system or external dependencies (e.g. PaperMC API, Guice,
  Flyway, ...)
* **ci**: Changes to our CI configuration files and scripts (e.g. GitHub Actions, Renovate, ...)
* **chore**: Changes that don't in any other category (e.g. dependencies' update, update
  of `.gitignore` `.gitattributes` & `.editorconfig` files, ...)

##### Scope

Most of the time, you should try to stick with the following scopes:

* **core**: used for changes related to the `patch-place-break-api`, `patch-place-break-core`
  and `patch-place-break-cts` modules
* **paper-adapter**: used for changes related to the `paper-patch-adapter` module
* **paper-plugin**: used for changes related to the `paper-plugin` module

But there are exceptions that shall be considered as well depending mostly on the change type.

###### Specific to the `docs` type

* **readme**: used for changes updating the `README.md` file
* **contributing**: used for changes updating the contribution-related
  files: `CONTRIBUTING.md`, `CODE_OF_CONDUCT.md` & `SECURITY.md`

###### Specific to the `build` type

* **packaging**: used for changes updating the Maven layout in all of our modules (e.g. groupId
  change, inherited plugins changes, ...)

###### Specific to the `ci` type

* **github**: used for updating GitHub Actions workflows, issues/PRs templates and `CODEOWNERS`
  file(s)
* **renovate**: used for updating Renovate configuration
* **release**: used for updating release-related configuration

##### Specific to the `chore` & `fix` types

* **deps**: used for changes updating the project dependencies

###### Special scope

* **_none/empty string_**: useful for test and refactor changes that are done across all packages
  (e.g. test: add missing unit tests) and for docs changes that are not related
  to a specific package (e.g. docs: fix typo in tutorial)

##### Summary

The summary must provide a succinct description of the change:

* Use the imperative, present tense: "change" not "changed" nor "changes"
* Don't capitalize on the first letter
* No dot (.) at the end

#### Commit Message Body

Just as in the summary, use the imperative, present tense: "fix" not "fixed" nor "fixes".

Explain the motivation for the change in the commit message body. This commit message should explain
_why_ you are making the change.
You can include a comparison of the previous behavior with the new behavior in order to illustrate
the impact of the change.

#### Commit Message Footer

The footer can contain information about breaking changes and is also the place to reference GitHub
issues, Jira tickets, and other PRs that this commit closes or is related to.

For example:

```
BREAKING CHANGE: <breaking change summary>
<BLANK LINE>
<breaking change description + migration instructions>
<BLANK LINE>
<BLANK LINE>
Fixes #<issue number>
```

Breaking Change section should start with the phrase `BREAKING CHANGE: ` followed by a summary of
the breaking change, a blank line, and a detailed description of the breaking change that also
includes migration instructions.

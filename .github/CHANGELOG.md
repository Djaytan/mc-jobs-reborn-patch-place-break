# Changelog
All notable changes to this project will be documented in this file. See [conventional commits](https://www.conventionalcommits.org/) for commit guidelines.

- - -
## [v3.0.2](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/compare/v3.0.1..v3.0.2) - 2024-01-02
#### 🏗️ Build System
- stop auto-generating uselessly sources and Javadoc JAR files (#450) - ([15a1f35](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/15a1f35a33196d0722e3fb328ba560bfa66ff0f9)) - [@Djaytan](https://github.com/Djaytan)
- Maven "fast" profile no longer skip source and Javadoc auto-generation (#433) - ([f32a11b](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/f32a11b905fa4d3a77dfd96521cdd35159e3293a)) - [@Djaytan](https://github.com/Djaytan)
#### 🐛 Bug Fixes
- **(deps)** update dependency com.google.guava:guava to v33 (#446) - ([d86e7d3](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/d86e7d3afeccbccd7291dc94ba972dad5af17c03)) - renovate[bot]
- **(deps)** update version.flyway to v10.4.1 (#442) - ([2cf5e25](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/2cf5e251cbc7cf06205d9ec421b3a270c4fb2d76)) - renovate[bot]
- **(deps)** update dependency org.mockito:mockito-bom to v5.8.0 (#441) - ([b354437](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/b354437b2f6ef34af6ae6a24b4b37d29fff77c58)) - renovate[bot]
- **(deps)** update dependency com.google.errorprone:error_prone_annotations to v2.24.0 (#439) - ([fbab03b](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/fbab03b7917d2fed12c34e4a3c574e94738d0f3f)) - renovate[bot]
- **(deps)** update dependency org.assertj:assertj-core to v3.25.0 (#440) - ([43e8354](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/43e83541ccfb55afb2dc00d34151e5e27cda06b2)) - renovate[bot]
- **(deps)** update dependency org.slf4j:slf4j-bom to v2.0.10 (#438) - ([b410d08](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/b410d0820c6465d3eddf8e0184f33a21780d2a0c)) - renovate[bot]
#### 📦 Continuous Integration
- **(actions)** replace 'softprops/action-gh-release' action by GitHub CLI (#459) - ([3e54680](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/3e5468053e20f652318885773a2880b8487825e6)) - [@Djaytan](https://github.com/Djaytan)
- **(cocogitto)** simplify branch switch step (#458) - ([4ab80b8](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/4ab80b8de12497ccace0a20e384eba92681b2ad8)) - [@Djaytan](https://github.com/Djaytan)
- **(github)** activate release PR auto-merge once prerequisites are met - ([2734698](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/2734698327f050f999ced4cf64e3bf0b2d2d776d)) - [@Djaytan](https://github.com/Djaytan)
- **(github)** rewrite cocogitto commit by updating the message header (#466) - ([b46978d](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/b46978d0f02bdfb007ffcdb4759ac7e6780451df)) - [@Djaytan](https://github.com/Djaytan)
- **(github)** extract the release changelog before publishing the release (#463) - ([837d359](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/837d359b13f5f826976badd76586a94e8c31c2ad)) - [@Djaytan](https://github.com/Djaytan)
- **(github)** associate a label to the created release PR (#462) - ([4862866](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/48628668e12f33d063c7e452b54f052aa8a7e522)) - [@Djaytan](https://github.com/Djaytan)
- **(github-actions)** fix release perform workflow (#457) - ([c0b5580](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/c0b55806c7b1195979f2404137a061e82e8b773d)) - [@Djaytan](https://github.com/Djaytan)
- **(renovate)** follow Renovate team's recommendations for configs (#461) - ([09e20d3](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/09e20d38b81cb179c1a633abdb63295055116e4d)) - [@Djaytan](https://github.com/Djaytan)
- activate manual workflow triggering (#456) - ([c59b6b8](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/c59b6b817e629693f06da1e37f81c20cb64d06bb)) - [@Djaytan](https://github.com/Djaytan)
- rely on the GitHub CLI for creating the PR at releasing time (#453) - ([e304166](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/e304166e63e3e184aa78c2d2c18481aea1fb80c7)) - [@Djaytan](https://github.com/Djaytan)
- explicit base branch at PR creation time (#452) - ([8c231a6](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/8c231a643bf44b4f7729a641ead88fcc15048700)) - [@Djaytan](https://github.com/Djaytan)
- fix releasing preparation by updating back pom.xml files with DEV version (#451) - ([ce32e59](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/ce32e59a7397e9f2cae4c9182c4c45040e3ac11e)) - [@Djaytan](https://github.com/Djaytan)
- rebase Renovate PRs only when conflicting with default branch (#449) - ([0fe58d0](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/0fe58d0e66435145604f73e18a7e16e6e58aa4d8)) - [@Djaytan](https://github.com/Djaytan)
- upload only the Spigot plugin JAR file at release publishing time (#448) - ([d20b763](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/d20b763e7ee9514007b123fcc08c0fb9d3ec76b2)) - [@Djaytan](https://github.com/Djaytan)
- rework releasing process by going though a PR before publishing (#447) - ([285e1a4](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/285e1a432adf2673a0e0fb576adb51c06c1373a0)) - [@Djaytan](https://github.com/Djaytan)
- simplify releasing process (#437) - ([c4ffb82](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/c4ffb825a30c500549fff596ecc941cd5338f498)) - [@Djaytan](https://github.com/Djaytan)
- use "fast" Maven profile at releasing time (#435) - ([306559c](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/306559c2944f67bddc61f3902efea7ea4f797b78)) - [@Djaytan](https://github.com/Djaytan)
- rename some GitHub Actions workflows (#434) - ([c3eb518](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/c3eb5182932ab43e8fd97b33d5f6bd9e87671b60)) - [@Djaytan](https://github.com/Djaytan)
- use long formats for commands arguments (#432) - ([1b1b334](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/1b1b3346bc4d8403a8b7857ecd3deebfc387400a)) - [@Djaytan](https://github.com/Djaytan)
- simplify Maven cache setup in GitHub Actions workflows (#431) - ([0367edb](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/0367edb36a8e624a4e02f6196e39e3b55244da09)) - [@Djaytan](https://github.com/Djaytan)
- rename some workflows and jobs (#430) - ([6c6ce9e](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/6c6ce9ea9ce67d45bbb0850c4294932241a3c297)) - [@Djaytan](https://github.com/Djaytan)
- no longer rely on release branches at releasing time (#429) - ([9876020](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/987602056414345bd5871df40c9a5a5339365ef7)) - [@Djaytan](https://github.com/Djaytan)
- reorganize GitHub Actions fields in workflows (#428) - ([29f9b6d](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/29f9b6d9ea411785950ea5f8393e0383607a6a27)) - [@Djaytan](https://github.com/Djaytan)
- check automatically conventional commits compliance (#427) - ([677b430](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/677b4301379e3cce9503b8dae6642eb918dbc5ce)) - [@Djaytan](https://github.com/Djaytan)
#### 🧹 Miscellaneous Chores
- **(deps)** update github/codeql-action action to v3 (#445) - ([6dca907](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/6dca907a008d570e650236cd35f7e3da6949c561)) - renovate[bot]
- **(deps)** update actions/upload-artifact action to v4 (#444) - ([d7ff3c6](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/d7ff3c6a9260dce9b873a5f28dfb1f367c9006c2)) - renovate[bot]
- **(deps)** update actions/setup-java action to v4 (#443) - ([04c6d39](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/04c6d39ea2cb0408aa4967370e5b3192c26a98f9)) - renovate[bot]
- prepare for next development iteration [ci skip] - ([3f295f9](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/3f295f9ece9d32a7c77539b9f6ff5f285f18b91d)) - github-actions[bot]

- - -

## [v3.0.1](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/compare/v3.0.0..v3.0.1) - 2023-12-28
#### ↩️ Revert
- **(renovate)** abort automerge activation for patchs (#354) - ([fede303](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/fede30325b485d782e4b69201e75c71bc2bda72a)) - [@Djaytan](https://github.com/Djaytan)
#### ✅ Tests
- improve names (#404) - ([c3b4ece](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/c3b4ece3dde382786769986aa196532d7888e95f)) - [@Djaytan](https://github.com/Djaytan)
- rename base test classes to remove confusion (#372) - ([1e47a06](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/1e47a0636e93540b3f053fa1ad20b2e074284d42)) - [@Djaytan](https://github.com/Djaytan)
#### 🏗️ Build System
- **(maven)** allow JDK version higher than 17 (#399) - ([e260e61](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/e260e61aeca952574f2e598a2bc8356500f75966)) - [@Djaytan](https://github.com/Djaytan)
- **(maven)** fix broken paths to LICENSE and README.md files (#396) - ([5e7f684](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/5e7f684d158e01b934461b878d7d37a7c4e19613)) - [@Djaytan](https://github.com/Djaytan)
- **(maven)** enable Java compiler linter for most warnings (#380) - ([d52b0f3](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/d52b0f3ec55120af0641dba9467a4b1104453328)) - [@Djaytan](https://github.com/Djaytan)
- **(maven)** get rid of CDS warnings from tests (#376) - ([563dcef](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/563dcefdefa0c4b0eafb4e2bfd1277ce0c0f5262)) - [@Djaytan](https://github.com/Djaytan)
- **(maven)** simplify license plugin config (#374) - ([8455df5](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/8455df57153007524299cc2fa106c38b2b5a82f2)) - [@Djaytan](https://github.com/Djaytan)
- **(maven)** reactivate license header auto-format (#375) - ([d3b0c73](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/d3b0c73f4a59df29e6148e095ed9e5381c84bad0)) - [@Djaytan](https://github.com/Djaytan)
- **(maven)** decouple plugins's configs & executions (#373) - ([1279809](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/1279809d18d4a9c8cb4e85bc302661d443d92a85)) - [@Djaytan](https://github.com/Djaytan)
- **(maven)** rely on conventional naming for ITs (#371) - ([845a9b1](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/845a9b16cc316e02e74f0068585a97c430c2c663)) - [@Djaytan](https://github.com/Djaytan)
- **(maven)** rely exclusively on one JaCoCo agent (#370) - ([2b62b83](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/2b62b831689b0c34737a983f2124243845f66195)) - [@Djaytan](https://github.com/Djaytan)
- **(maven)** generate back coverage reports (#369) - ([ffa4ee3](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/ffa4ee3d3fef49c80d30f65ddcf57ac38bb74354)) - [@Djaytan](https://github.com/Djaytan)
- **(maven)** reactive maven-enforcer-plugin (#368) - ([71d1bad](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/71d1badf0249f681806bc97d9216cb071eebe66a)) - [@Djaytan](https://github.com/Djaytan)
- **(maven)** clean-up POM files and simplify build (#366) - ([1199c70](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/1199c706f4554ab769dd2a8cd22518eec4b6898b)) - [@Djaytan](https://github.com/Djaytan)
- **(maven)** remove no longer required explicit deps (#364) - ([6736fe0](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/6736fe054bc674f586ceeb1bbf6f3a7792d9020e)) - [@Djaytan](https://github.com/Djaytan)
- **(pom)** reorder plugins declarations (#365) - ([659c5e2](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/659c5e28da3bc702a56f448efc135fee19dae86f)) - [@Djaytan](https://github.com/Djaytan)
- **(pom)** reorder dependencies declarations (#360) - ([f1d9440](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/f1d94408b72671ac15b98d9e66581191e19080a3)) - [@Djaytan](https://github.com/Djaytan)
- **(pom)** use a different predefined sort order profile (#359) - ([4f68a00](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/4f68a005191a38a75302dec626d8b36a10ee698b)) - [@Djaytan](https://github.com/Djaytan)
- **(sortpom)** apply more lenient configuration (#357) - ([23907c8](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/23907c8d5f49d69c48b9941f7c77c6d6d5169269)) - [@Djaytan](https://github.com/Djaytan)
- **(test)** get rid of JVM warning when using Mockito (#355) - ([5c796c0](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/5c796c06537d8ba8879636ddf43c920fa9ee7eb1)) - [@Djaytan](https://github.com/Djaytan)
#### 🐛 Bug Fixes
- **(deps)** update dependency org.checkerframework:checker-qual to v3.42.0 (#413) - ([2346c44](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/2346c44762ea62d833bb8f623011cef2707088d3)) - renovate[bot]
- **(deps)** update dependency nl.jqno.equalsverifier:equalsverifier to v3.15.5 (#410) - ([6b4485c](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/6b4485c38b2385a11177bd9b2837436a9de0497b)) - renovate[bot]
- **(deps)** update dependency commons-io:commons-io to v2.15.1 (#409) - ([9e87e56](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/9e87e56d3683750e22829b1d416eb1742dbf6ebe)) - renovate[bot]
- **(deps)** update dependency org.testcontainers:testcontainers-bom to v1.19.3 (#411) - ([ef63980](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/ef639807f5fcce7cf098203c3c090282ff7290c2)) - renovate[bot]
- **(deps)** update dependency org.apache.commons:commons-lang3 to v3.14.0 (#412) - ([f995d77](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/f995d77a2ce8e108413e5b53a3cdcf56d996d28f)) - renovate[bot]
- **(deps)** update dependency org.mariadb.jdbc:mariadb-java-client to v3.3.2 (#415) - ([b1c12a8](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/b1c12a8a5d46692b4648c16f1d390e73af71e233)) - renovate[bot]
- **(deps)** update dependency org.xerial:sqlite-jdbc to v3.44.1.0 (#416) - ([e5b610f](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/e5b610f5a26c45fe82b86a04d1785cf886843841)) - renovate[bot]
- **(deps)** update dependency org.jetbrains:annotations to v24.1.0 (#414) - ([ae913b6](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/ae913b630602416e5ac6efe632c0edf451be1b4a)) - renovate[bot]
#### 📖 Documentation
- **(contributing)** document the project's test policy (#388) - ([466a33e](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/466a33ef29f1ec201e17bca3674bc4f0358039fc)) - [@Djaytan](https://github.com/Djaytan)
- **(contributing)** add Conventional Commits section (#356) - ([efa3a43](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/efa3a430f325ffa4fac73503324d8feaf1f72c04)) - [@Djaytan](https://github.com/Djaytan)
- **(license)** update copyright year (#393) - ([6e8ddbb](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/6e8ddbb16b0eca6e5215bfafd99e1f285f1ddfef)) - [@Djaytan](https://github.com/Djaytan)
- **(license)** rename file (#392) - ([44b39eb](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/44b39ebc1d9955618e7d98bb2060ae71119cac17)) - [@Djaytan](https://github.com/Djaytan)
- **(license)** move LICENSE.md file at root folder for auto-detection (#382) - ([1c7ebc1](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/1c7ebc1c2a6031f358667b79ae9fd35d1a320d19)) - [@Djaytan](https://github.com/Djaytan)
- **(readme)** add a section telling how to support us (#401) - ([5787050](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/5787050a88456ead21b711a73118e2d377a1d2da)) - [@Djaytan](https://github.com/Djaytan)
- **(readme)** reorganize status badges (#400) - ([59e45c5](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/59e45c5dbf99ea58ae2c1422ddca423688f4ec8f)) - [@Djaytan](https://github.com/Djaytan)
- **(readme)** adjust project name (#398) - ([3810ee3](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/3810ee3fdc5c2864f6fa9a48c6038eca5161cbbb)) - [@Djaytan](https://github.com/Djaytan)
- **(readme)** fix dead link to LICENSE file (#397) - ([7ea8de1](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/7ea8de1a0c035b1bb520d10d4092da7cc84107a2)) - [@Djaytan](https://github.com/Djaytan)
- **(readme)** add Scorecard OpenSSF score status badge (#395) - ([e4f05dc](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/e4f05dc43e1ede4550df0f48129387b063e1fea3)) - [@Djaytan](https://github.com/Djaytan)
- **(readme)** organize status badges across two lines (#391) - ([591205d](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/591205d31493d7c9f1b5dcf306ea1473c54c941b)) - [@Djaytan](https://github.com/Djaytan)
- **(readme)** show the OpenSSF Best Practices passing badge (#390) - ([f079429](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/f0794290040344f6f6e7f7b9e19ccd4d96ba7ff7)) - [@Djaytan](https://github.com/Djaytan)
- **(security)** improve security policy based on Scorecard's advices (#386) - ([62a4607](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/62a4607f73e4429eb1421a8211e33ffddffe24c6)) - [@Djaytan](https://github.com/Djaytan)
- update comments in pom.xml file (#418) - ([6cdb03f](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/6cdb03f869bf186fc870c3e956dbcf5a1a912f50)) - [@Djaytan](https://github.com/Djaytan)
- document restricted blocks behavior limitation & add TODOs (#403) - ([9ac573a](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/9ac573a6c094cefe47ca2725e24345863c71d57d)) - [@Djaytan](https://github.com/Djaytan)
- move general doc files under the `.github/` folder (#362) - ([a6d250c](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/a6d250c6bf58c240ae9e16ac92564d5d6b216a68)) - [@Djaytan](https://github.com/Djaytan)
- update README.md file (#351) - ([e9b026c](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/e9b026ca33e3817d5a2ba2407bcafbeb9b41a3a9)) - [@Djaytan](https://github.com/Djaytan)
#### 📦 Continuous Integration
- **(codeql)** run scanner for every modifications (#385) - ([ca4b302](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/ca4b302361cd2c4be2a93edea6cb7cee7a468151)) - [@Djaytan](https://github.com/Djaytan)
- **(github-actions)** define top-level permissions in all workflows (#384) - ([30219a9](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/30219a9816350d292994295824f6f159d00ef6ee)) - [@Djaytan](https://github.com/Djaytan)
- **(maven)** run scanner for every modifications (#387) - ([68634c9](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/68634c9d32ea7d8a704d51ac2c94f30e23596dbe)) - [@Djaytan](https://github.com/Djaytan)
- **(maven)** rename Maven build job (#367) - ([2caf07f](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/2caf07ff4bc2f9717b557c02af89338241cbc263)) - [@Djaytan](https://github.com/Djaytan)
- **(renovate)** move config file under `github/` folder (#363) - ([22ad541](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/22ad541220abbd99ba5a22cc87865458c55797b8)) - [@Djaytan](https://github.com/Djaytan)
- **(renovate)** activate back automerge for patchs (#353) - ([b166e20](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/b166e2011fd05f5c24228806c38ffd9a1384b82d)) - [@Djaytan](https://github.com/Djaytan)
- **(scanners)** schedule them daily instead of weekly (#389) - ([ccea9bd](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/ccea9bd3c3821a1052e4966776e31cc89adf35c3)) - [@Djaytan](https://github.com/Djaytan)
- **(scorecard)** no longer execute scanner on PRs (not well supported) (#394) - ([b2f4324](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/b2f4324d9ae8bc8b6dce3a7f7e074e73d213eada)) - [@Djaytan](https://github.com/Djaytan)
- **(scorecard)** run scanner for every modifications (#383) - ([a3888db](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/a3888db23b2d206084434837324a9f9f46d40084)) - [@Djaytan](https://github.com/Djaytan)
- push the right prefixed tag at releasing time (#426) - ([cbe24cd](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/cbe24cdc3e39cc7d6b3f030e707a4e89a3238c7b)) - [@Djaytan](https://github.com/Djaytan)
- bypass branch protection at releasing time when pushing commit (#425) - ([83fdae7](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/83fdae7e05cd583d910cb013a9099c9216fe19b1)) - [@Djaytan](https://github.com/Djaytan)
- use Maven cache at release time (#424) - ([68c4a94](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/68c4a94b8aac8a3d9332ffdbccf96e093dd6289c)) - [@Djaytan](https://github.com/Djaytan)
- persist Git credentials at release time (#423) - ([20bc069](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/20bc0696d332ad821adbc49ac822d69959b0bb56)) - [@Djaytan](https://github.com/Djaytan)
- stage all changes before committing at release time (#422) - ([df0fd0f](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/df0fd0f38356ef7e161da99889ba13e58953af34)) - [@Djaytan](https://github.com/Djaytan)
- check commits since latest tag only at release time (#421) - ([b0a1665](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/b0a1665d36277c6a28a534b90341e06f55cc2a68)) - [@Djaytan](https://github.com/Djaytan)
- fix release workflow by fetching completely the Git history (#420) - ([0e995bc](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/0e995bc96cbcce23d19e13cea68fac480d88d049)) - [@Djaytan](https://github.com/Djaytan)
- setup automatic SemVer bump and GitHub release publication (#419) - ([b90db86](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/b90db8677b1e5caa92814168a1aea27e56753d94)) - [@Djaytan](https://github.com/Djaytan)
- prepare for next development iteration (#350) - ([7bb0c37](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/7bb0c3774c3a3734ae35dbeacf434ab233e4fdfd)) - [@Djaytan](https://github.com/Djaytan)
#### 🖌️ Style
- **(pom)** increase indent size from 2 to 4 (#358) - ([f0c37d9](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/f0c37d968b228a3036a0c2aa9070ca8d6c380b3e)) - [@Djaytan](https://github.com/Djaytan)
#### 🛠️ Refactoring
- **(jpms)** rename modules to ensure they will be globally unique (#381) - ([fc84dfa](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/fc84dfaba8cdb3b5a07e262ea27787dd93e32af3)) - [@Djaytan](https://github.com/Djaytan)
- **(jpms)** set Jetbrains annotations as optional at runtime (#379) - ([9773774](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/9773774c5910aef3d878d43c92df866c44d5186d)) - [@Djaytan](https://github.com/Djaytan)
- **(jrppb-core)** rename ConnectionPools to DatabaseMediator (#378) - ([b58c78d](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/b58c78dbd6a7e52badb39cdab930109a13084ed0)) - [@Djaytan](https://github.com/Djaytan)
- **(jrppb-core)** remove ambiguity between ConnectionPools' methods (#377) - ([742db24](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/742db240cd8a83a3cbe6a0e0405c5187e96f7eee)) - [@Djaytan](https://github.com/Djaytan)
- stop relying on useless custom exceptions by reworking them (#406) - ([01140bf](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/01140bf2fd42dc6abbe9a23b24a1c8d49d72d03c)) - [@Djaytan](https://github.com/Djaytan)
- explicit validation at Location conversion time (#405) - ([b83b355](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/b83b355f11a6230e5fe99580cf23e638b4013920)) - [@Djaytan](https://github.com/Djaytan)
#### 🧹 Miscellaneous Chores
- **(deps)** update github/codeql-action action to v2.22.12 (#407) - ([311c995](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/311c995776ca9dd54a08940961e81618ec09bfca)) - renovate[bot]
- **(deps)** update maven plugins (#408) - ([974b30e](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/974b30e175a2c7e62af79a4e503cc731ac254055)) - renovate[bot]
- **(editorconfig)** set XML files indent size to 4 (#361) - ([9eeb9a7](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/9eeb9a705c8ccac594c01bcb998fb6789b06e2d4)) - [@Djaytan](https://github.com/Djaytan)

- - -

Changelog generated by [cocogitto](https://github.com/cocogitto/cocogitto).

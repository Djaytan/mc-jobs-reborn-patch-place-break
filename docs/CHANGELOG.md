## [3.0.4](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/compare/v3.0.3...v3.0.4) (2024-08-25)


### 🐛 Bug Fixes

* **core:** fail on overflow/underflow ([#589](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/589)) ([e5427e2](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/e5427e2c1bd263c8c74be6b3e0e1dafe89d92257))


### 🏗️ Build System

* **deps:** bump bukkit-slf4j dependency to v2.0.0 ([#593](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/593)) ([f781c18](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/f781c1867055487cc2c183c4a294e92d8e01c656))
* **deps:** lock file maintenance ([#586](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/586)) ([61f1804](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/61f1804365f210d7170f0446e55a048498f6624c))
* **deps:** lock file maintenance ([#592](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/592)) ([c12927b](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/c12927befc1b6991e7562bcb3a0d5f9e01f15cd7))
* **packaging:** adjust the root pom.xml file ([#594](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/594)) ([8aff837](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/8aff837d4eddbfb4168119f872f9cbd4adbf320b))
* **packaging:** explicitly define the "test" scope for dependencies ([#596](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/596)) ([3e0e81a](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/3e0e81a16b8cfddfd2149895e1bc077df415a5b8))


### ✅ Tests

* rely on Logback instead of slf4j-nop for test logs ([#584](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/584)) ([d08cead](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/d08cead9a9a017bc8c8abe9d5b1a13f3817c811b))
* setup PITest with Maven for mutation testing ([#402](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/402)) ([2a3d87c](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/2a3d87c8a040829eed24f8ced0e703f513d2b50c))
* update junit5 to v5.11.0 & migrate to @AutoClose annotation ([#616](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/616)) ([ca124c2](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/ca124c2927aea9c11d097b67794466415a2db884))


### 📦 Continuous Integration

* allow 'chore' Conv. Commit type for dependencies updates & others ([#595](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/595)) ([556bde9](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/556bde9009ef3d43ca9b970db6379933e073b373))
* **github:** allow installing dependencies from the NPM registry ([#591](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/591)) ([092f9ea](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/092f9ea3e141b25f295407078b725a70ebef8f3e))
* **github:** allow SonarCloud to generate GitHub security events ([#598](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/598)) ([c32ab5b](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/c32ab5b97b6fb2622697a6b966311bf0de8fc62b))
* **github:** extract Bash instructions in script files & improve the existing one ([#582](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/582)) ([9d3ccdc](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/9d3ccdcd162f04d14629a35f89b6f8ecde904a74))
* **github:** fix OpenSSF scans by allowing communications with api.deps.dev host ([#621](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/621)) ([bf22719](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/bf227199197ae4805b7b26d6d956bb1e2051fd79))
* **github:** fix OpenSSF scans by allowing communications with api.scorecard.dev host ([#622](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/622)) ([bb28a2b](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/bb28a2b507031439f2c18db971c7ab43a8cef365))
* **github:** separate commit check from the CI in a dedicated workflow ([#590](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/590)) ([e9ff398](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/e9ff398207d3bfd346990356134ddb6bcdefe008))
* **github:** specify minutes instead of seconds for timeout ([#585](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/585)) ([11a0176](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/11a01762ea14b84a79372e5c972b7bda61a42db9))


### 📖 Documentation

* **changelog:** release v3.0.3 ([#581](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/581)) ([72942d7](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/72942d7a69a31dfabf3e66a2bd1dd19f883bf79f))
* clean-up TODOs comments in code ([#588](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/588)) ([8d80b36](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/8d80b365f5fbbbcfb6f9a1c467400f646e3f9b75))
* **readme:** document Pitest usage for mutation testing ([#597](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/597)) ([107076b](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/107076b867f4f3341b260e699febe8b8abb71bd6))
* **readme:** update & include feature overview section ([#623](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/623)) ([3c566cd](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/3c566cdbfc4f81b96b7b1a125a0d94a5a38856a4))


### 🧹 House Keeping

* **deps:** lock file maintenance ([#599](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/599)) ([0bb9acc](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/0bb9acca1dfdcadc40cee6d28669c90a96bddc67))
* **deps:** lock file maintenance ([#602](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/602)) ([fd0d3c5](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/fd0d3c5d2ef6108b4b1cdfa43700b1e6caea7314))
* **deps:** lock file maintenance ([#603](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/603)) ([966b547](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/966b54784005163fcfebdbd27cee6a154b101822))
* **deps:** lock file maintenance ([#604](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/604)) ([7b969ca](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/7b969cadfaaa125d27b01b3bef9e1f28c0ef534f))
* **deps:** lock file maintenance ([#611](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/611)) ([c450ff5](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/c450ff5ede0e77eb1491d1be2c03581b646fee0d))
* **deps:** lock file maintenance ([#614](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/614)) ([804eec5](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/804eec5e01ea51df323fafe8f0e17ad3dc13b297))
* **deps:** update actions/cache action to v4.0.1 ([#600](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/600)) ([e6a1948](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/e6a19487d27059d838b3cddb11c70f285c7cbf7c))
* **deps:** update actions/cache action to v4.0.2 ([#605](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/605)) ([bb9c9cb](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/bb9c9cbc021cfd4baf40e2b583cffe1cf3957dfc))
* **deps:** update actions/checkout action to v4.1.6 ([#609](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/609)) ([6525b7e](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/6525b7e8c43cdd9a072fa08543eaacf29830a560))
* **deps:** update actions/checkout action to v4.1.7 ([#615](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/615)) ([7984999](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/7984999192b75019f36ae96f53222e730036722f))
* **deps:** update actions/setup-node action to v4.0.3 ([#619](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/619)) ([8166d24](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/8166d24d1c6985d52b499c5e3388855e24a02ba5))
* **deps:** update actions/upload-artifact action to v4.3.3 ([#610](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/610)) ([347027b](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/347027bb88caab0231f14c0c48f5dc775f0a932e))
* **deps:** update actions/upload-artifact action to v4.3.6 ([#620](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/620)) ([ada875a](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/ada875a21a9a359a582d6a75e4dbb003e526b909))
* **deps:** update commitlint monorepo ([#601](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/601)) ([9d7d5d5](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/9d7d5d5b68e679fba5018f0bb0af083eb64fdb36))
* **deps:** update dependency @commitlint/config-conventional to v18.6.3 ([#606](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/606)) ([4e61039](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/4e610393cad2f90ce2707e3699641094263039c8))
* **deps:** update dependency org.apache.maven.plugins:maven-install-plugin to v3.1.2 ([#612](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/612)) ([f5d85b7](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/f5d85b73725e60c5234e36f70c942bf56a797bc6))
* **deps:** update dependency org.apache.maven.plugins:maven-shade-plugin to v3.5.3 ([#607](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/607)) ([e3abb63](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/e3abb631edbd4d35fb8edbbbc77e5a94cfb9adf7))
* **deps:** update dependency org.awaitility:awaitility to v4.2.1 ([#608](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/608)) ([944a60d](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/944a60de07f88f181f7569f07262b363a8180617))
* **deps:** update dependency org.jacoco:jacoco-maven-plugin to v0.8.12 ([#613](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/613)) ([4f643da](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/4f643da0e3add44b28fea0c34895e7e1d29ccdb1))
* **deps:** update npm to v10.8.2 ([#617](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/617)) ([eeead0d](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/eeead0df444b8b2cb1fa568ae9114e5f6ea03f53))
* **deps:** update ossf/scorecard-action action to v2.4.0 ([#618](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/618)) ([565f724](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/565f7241a62891fb5ba6775940e12dc68b8f3b43))

## [3.0.3](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/compare/v3.0.2...v3.0.3) (2024-02-15)


### 🐛 Bug Fixes

* **spigot-adapter:** [#570](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/570) - logs noises when performing unsupported job action types ([#571](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/571)) ([b79c9d2](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/b79c9d2b7803674f590608151e0189883129e5d2))


### 🏗️ Build System

* **deps:** lock file maintenance ([#544](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/544)) ([fff8fad](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/fff8fad916f183b1b57f2df88b152dbd52c9747a))
* **deps:** lock file maintenance ([#568](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/568)) ([615b3ff](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/615b3ffa1a79a3f773591fb6e5c70cfabe92f9be))
* **deps:** lock file maintenance ([#572](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/572)) ([78b2e5f](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/78b2e5f0cd7cd207055e51a53ee2f2c47f47d4a9))
* **deps:** update actions/cache action to v4 ([#547](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/547)) ([ad98fb8](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/ad98fb83ca23709e8f506d816762b66f6dfa77fc))
* **deps:** update actions/setup-node action to v4.0.2 ([#550](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/550)) ([a46fdee](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/a46fdeea1926776dfe769dddf3f288b1222a9e7f))
* **deps:** update actions/upload-artifact action to v4.3.1 ([#554](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/554)) ([0a3acf1](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/0a3acf18427b793736c3118c316a299d26131695))
* **deps:** update commitlint monorepo to v18.6.0 ([#558](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/558)) ([cd1b7c0](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/cd1b7c044be86e64f3871dbc442b7a3dbf704800))
* **deps:** update dependency com.google.errorprone:error_prone_annotations to v2.24.1 ([#546](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/546)) ([3ed7eaf](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/3ed7eaff623486dfccab52d4b5c5bf137ea9d154))
* **deps:** update dependency com.mysql:mysql-connector-j to v8.3.0 ([#559](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/559)) ([2f68b52](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/2f68b52250344c31e01a135a630f9c58c9839645))
* **deps:** update dependency com.spotify.fmt:fmt-maven-plugin to v2.22.1 ([#560](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/560)) ([0b619a6](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/0b619a65285f5a7a4e050bc3c5ed174cc2a79b32))
* **deps:** update dependency nl.jqno.equalsverifier:equalsverifier to v3.15.6 ([#551](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/551)) ([b4c1cb1](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/b4c1cb15c99fd089c863ea9e6a6c694a71eb9b4d))
* **deps:** update dependency org.apache.maven.plugins:maven-failsafe-plugin to v3.2.5 ([#548](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/548)) ([35fadea](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/35fadeacb53402982a0529dafe51853191d1127c))
* **deps:** update dependency org.apache.maven.plugins:maven-surefire-plugin to v3.2.5 ([#549](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/549)) ([f63b969](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/f63b969d26f7207426afcb8137b0d8e7129f234a))
* **deps:** update dependency org.assertj:assertj-core to v3.25.3 ([#552](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/552)) ([99f3b15](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/99f3b15d343305328c2eb4610e34760fe7d3e5ff))
* **deps:** update dependency org.junit:junit-bom to v5.10.2 ([#555](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/555)) ([3eb00a3](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/3eb00a34b2c6de6b2addeef3affed331120dcfd6))
* **deps:** update dependency org.mockito:mockito-bom to v5.10.0 ([#561](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/561)) ([e02463c](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/e02463c09ff8ed6134ed67cbe89e1c26d452f11b))
* **deps:** update dependency org.slf4j:slf4j-bom to v2.0.12 ([#556](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/556)) ([acf06fa](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/acf06faf1670840fd094e62f293417453b33b624))
* **deps:** update dependency org.testcontainers:testcontainers-bom to v1.19.4 ([#557](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/557)) ([793e827](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/793e8270b1a4e6938ac88cad662db1fd6aac60f8))
* **deps:** update dependency org.testcontainers:testcontainers-bom to v1.19.5 ([#569](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/569)) ([d27662d](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/d27662d918a9de62bbc58a70941fa32131e2db5c))
* **deps:** update dependency org.xerial:sqlite-jdbc to v3.45.1.0 ([#562](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/562)) ([54e6bd4](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/54e6bd442ce1edddae89da329674e29443db819f))
* **deps:** update dependency semantic-release to v23.0.2 ([#553](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/553)) ([f67c61d](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/f67c61d2042db8fe835bdb1890910025f0610554))
* **deps:** update github/codeql-action action to v3.24.0 ([#563](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/563)) ([a1ce778](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/a1ce778ef95aa1af1b91469c5176a26e8414c8c4))
* **deps:** update sigstore/cosign-installer action to v3.4.0 ([#564](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/564)) ([095b946](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/095b94687032d033ce4c57bc5759ef8cb4a6ec32))
* **deps:** update step-security/harden-runner action to v2.7.0 ([#565](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/565)) ([953c230](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/953c2303bd317fa8415f7f4fda090c5cbab05d28))
* **deps:** update version.flyway to v10.7.1 ([#566](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/566)) ([32c5a03](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/32c5a03b4119cc7979343948f1942fd95d748264))
* **deps:** update version.flyway to v10.7.2 ([#567](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/567)) ([d73b299](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/d73b2999204aa9d86f2c1f25e8c9402bb58ef195))
* **maven:** rename `plugin.*` properties to `spigotName.*` ([#485](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/485)) ([dc6cb03](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/dc6cb03ebeab53993ce3454b1b9e24481ee8cc71))
* **maven:** stop generating backup files ([#511](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/511)) ([21158bd](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/21158bdf2506f8f41ff01b685e93b6e1e17f6a2e))
* **other:** simplify .gitignore file ([#520](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/520)) ([33297c5](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/33297c56f3e54e519f3f6e3e3c395798664dd7f2))


### 📦 Continuous Integration

* **github:** abort release creation if tag already exists ([#484](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/484)) ([1da8473](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/1da8473ee004509ca40fcd0c80b920cba2c799e8))
* **github:** add a workflow signing a given provided blob file ([#488](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/488)) ([ad00b71](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/ad00b71a51047aec7eedb522b53f1da5334120fc))
* **github:** add timeout for all jobs ([#505](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/505)) ([df4341b](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/df4341bd9c9cdea1bbdf6addca26f8d61ceec316))
* **github:** allow additional GitHub endpoint for security scans ([#517](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/517)) ([8643419](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/864341944be1c881a46d7159a5873a02be0b85ea))
* **github:** allow CodeQL to send requests to uploads.github.com:443 ([#479](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/479)) ([5fc2dcb](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/5fc2dcbcdd743a01f31c5a35309379336592c759))
* **github:** allow Scorecard scanner to read branch protection rules ([#476](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/476)) ([2ee0783](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/2ee0783860d757fd29f1201840af52108467db36))
* **github:** allow sending requests to Maven central ([#483](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/483)) ([938ce9b](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/938ce9be21279a9aa8b4ab4c52e09fd026831fab))
* **github:** allow targeting the Nuget API ([#506](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/506)) ([3bf69fa](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/3bf69fae07cb881b210e0afdc0a2be0976a1a305))
* **github:** always print color when executing Maven commands ([#515](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/515)) ([d4ed508](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/d4ed5083a222625dfe05a1abf978d321588fdcd0))
* **github:** don't rely on regexp option at release signing time ([#493](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/493)) ([a13b90a](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/a13b90a6639042598410b8e6666886163069d787))
* **github:** explicit runner Ubuntu version for jobs ([#521](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/521)) ([9d4c7c6](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/9d4c7c655a9236dceb02e8a4e494c5822737d6da))
* **github:** fail when no file to be signed for a given release ([#500](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/500)) ([35c58bb](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/35c58bb5723fdb5e6f82a52965aaf48ea0fc7491))
* **github:** fix release perform workflow by relying back on mono-job ([#494](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/494)) ([248096b](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/248096bd9b1d4c852d894ef808f2b91b4dbcbb6b))
* **github:** fix release signing workflow ([#490](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/490)) ([ebb4974](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/ebb49745957b00bc2eb79194c5b97ef3386e4e6c))
* **github:** fix semver check at release signing time ([#491](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/491)) ([e16813d](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/e16813d47b674fe587efccace412029e1dff30a9))
* **github:** harden GitHub Actions runners ([#477](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/477)) ([15b520a](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/15b520acea4af34484ee6c6f8a561b90441fc566))
* **github:** harden GitHub Actions runners for Scorecard scanner ([#478](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/478)) ([6af11b7](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/6af11b7fa659535c654644541fa3eaa303efd730))
* **github:** make CI workflow more portable ([#496](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/496)) ([8c17792](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/8c17792079ce6b3211e56f538fec4effa3d4f8ba))
* **github:** merge release workflows ([#522](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/522)) ([0d6c361](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/0d6c3611508cf933de510f6d43144991a62ff5e2))
* **github:** merge some workflows together for simplification ([#498](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/498)) ([9516913](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/9516913765aee3698683603a6163bef64ac9cee1))
* **github:** no longer bypass tag protection at releasing time ([#480](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/480)) ([bb228a5](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/bb228a5356e06e8aa0dcd30e048b07c22b2bfb43))
* **github:** rely on more concise arrays for branches and event types ([#502](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/502)) ([20db59d](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/20db59db93e36fff046cec7f93e1fc46e7a5a008))
* **github:** remove all permissions by default ([#495](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/495)) ([4d29434](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/4d29434282bc27a8b0ee300ed46d0185e80f3e07))
* **github:** reschedule CI to be run every day during the night ([#528](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/528)) ([71fff70](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/71fff70ebfdc59d72cccc310d8bd359b4ca08e12))
* **github:** retrieve plugin name dynamically through Maven ([#486](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/486)) ([870b776](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/870b776ad7cc42b0954e90b6d4daa0cfdd5a3d28))
* **github:** rework signing workflow for specializing it for releases ([#489](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/489)) ([5a973c7](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/5a973c761ddc3d69c7f1a00aa84c82535ba42d07))
* **github:** sign assets at releases creation/publication/... ([#501](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/501)) ([41837dd](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/41837dd61536b71cce54b1475446d45bc2405773))
* **github:** simplify signature verification logic ([#530](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/530)) ([faeb02d](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/faeb02da97fe152a2715461ca102b62f7ce8e110))
* **github:** specify unique & self-explained jobs IDs across workflows ([#526](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/526)) ([d7a6ec2](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/d7a6ec28ad7cab8cdc25c23c5b0a5002ae464acf))
* **github:** split CI job in two for performances improvement ([#503](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/503)) ([57236b9](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/57236b95cdfd32affbaa30ba5b122700788d488e))
* **github:** split release perform workflow in two jobs ([#487](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/487)) ([0dae875](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/0dae875af38ea06ea003f7f9eaa167812db3bb26))
* **github:** use workaround for signing releases automatically ([#504](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/504)) ([40e710e](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/40e710e3a67cbe539ccb2f3d33450f835bb60a8a))
* **release:** only support conv. commits from Angular guidelines ([#508](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/508)) ([0a455a7](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/0a455a7fc1e74ebe62b8a8216c6320bdc074a444))
* **release:** apply workaround for resolving the envvar being interpreted as a Lodash template ([#579](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/579)) ([6e04bdf](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/6e04bdf619c5241eb03e285d13729a89dc62675c))
* **release:** fixing remaining issues & finalizing in pipeline ([#580](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/580)) ([ed0eff4](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/ed0eff409365bdc341c55f2a84b5c6b502efbf18))
* **release:** get rid of broken semantic-release PR dependency ([#577](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/577)) ([b10f7c6](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/b10f7c6b8409bfb1c0c6e99630f9875c344670fd))
* **release:** install Java 17 before releasing a new version ([#576](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/576)) ([08fb581](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/08fb581ff18d6f4d9a470c3c341126ebffdf5c76))
* **release:** lenient max header length for commits checks ([#514](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/514)) ([e9b26e0](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/e9b26e01b3e776ef47db38d906ee001974ab936d))
* **release:** move config file at the expected location ([#524](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/524)) ([78a3e42](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/78a3e424a6827960761803507f31e8bde0f577b7))
* **release:** move the "scripts/" folder under the ".github/" one ([#527](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/527)) ([fdd4874](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/fdd4874b890d1a0689ddf059090125b9f432440f))
* **release:** plugin JAR file nor the tag version name could not be found ([#578](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/578)) ([03ec01c](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/03ec01c123e01274bb736fb947d05bfc37fc7ff0))
* **release:** replace Cocogitto by semantic-release & commitlint ([#513](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/513)) ([b255a3c](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/b255a3c724db16ddea60044c096de680236e2096))
* **release:** setup more secure and stable download of JS deps ([#519](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/519)) ([64c6913](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/64c6913f12343c29e3d8662467e42e76edd18dfe))
* **release:** setup the GITHUB_TOKEN environment variable ([#525](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/525)) ([baf2614](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/baf2614904645218c5b652c9e0fbcacdcc4dfe7f))
* **release:** simplify semantic-release config ([#529](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/529)) ([35b69c1](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/35b69c111e84b516bbde2a0fafcb461f9bbb28ff))
* **release:** support next and next-major release branches ([#518](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/518)) ([7b3d855](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/7b3d855f7eb13def41d6c285c0c57b1ff5a8b68e))
* **release:** resolve permission denied issue when executing script ([#574](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/574)) ([48fb6f6](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/48fb6f63d459cbaac1d0d3a8ea5d9616bbb9eef3))
* **release:** specify the right root dir path when generating plugin ([#575](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/575)) ([dbfecbb](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/dbfecbb47012e7d702f8e096986fafb4f2a27f44))
* **renovate:** force "build" types for Renovate's commits ([#545](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/545)) ([f523ed8](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/f523ed80d8082ffe821293172c06b0d3b0167785))
* **renovate:** migrate config file to JSON5 ([#533](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/533)) ([022ebdd](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/022ebdd9961562aecbf1b78c0da3bc7034738606))
* **renovate:** rework Renovate configuration ([#532](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/532)) ([0171967](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/0171967e17636fefa357a2331e549d94ce61077e))


### 📖 Documentation

* **contributing:** cover commit message body and header ([#510](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/510)) ([453eb07](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/453eb07a796b7bbda1c0bb573f1f7d299a29f6eb))
* **contributing:** share allowed types and scopes for conv commits ([#509](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/509)) ([c67b16a](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/c67b16ad54a3566b3385429c9e5affe1c01daee1))
* create the "docs/" folder and move most of documentation files there ([#534](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/534)) ([eeb4163](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/eeb41637a0287394a1bcc0262104a868e04febb9))
* **readme:** add missing tools in the "Built with" section ([#507](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/507)) ([e199fa5](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/e199fa5cb30afdf6608d8c036a0897068426c034))
* **readme:** add tools used specifically for the CI ([#497](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/497)) ([5f87693](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/5f87693f67826821840294858858512bed1e6821))
* **readme:** adjust the signature verification section ([#512](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/512)) ([de0a1ca](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/de0a1ca54e8ad870f301c42de6762065a03a3660))
* **readme:** fix signature verification command ([#531](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/531)) ([2c310ea](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/2c310ead0fcc78c1592c217c64a0eecfe770a596))
* **readme:** provide steps to follow for signature verification ([#492](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/492)) ([5ba0a85](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/5ba0a85fce8ba8c9c2c2f2ea9adf9e226298eaff))
* **readme:** update outdated CI status badge ([#499](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/issues/499)) ([eb2b9c2](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/commit/eb2b9c2fc6f594ac9b6554160a084b28494d9c5e))

module.exports = {
  preset: 'conventionalcommits',
  // Conventional Changelog config specifications can be found here:
  // -> https://github.com/conventional-changelog/conventional-changelog-config-spec/tree/master/versions
  presetConfig: {
    types: [
      {type: 'feat', section: '🌟 Features'},
      {type: 'fix', section: '🐛 Bug Fixes'},
      {type: 'perf', section: '⚡ Performances Improvements'},
      {type: 'revert', section: '🔄 Revert'},
      {type: 'refactor', section: '🛠️ Refactoring'},
      {type: 'build', section: '🏗️ Build System'},
      {type: 'test', section: '✅ Tests'},
      {type: 'ci', section: '📦 Continuous Integration'},
      {type: 'docs', section: '📖 Documentation'},
      {type: 'chore', section: '🧹 House Keeping'}
    ]
  },
  plugins: [
    '@semantic-release/commit-analyzer',
    '@semantic-release/release-notes-generator',
    [
      '@semantic-release/github',
      {
        assets: [
          {
            path: '../../src/paper-plugin/target/JobsReborn-PatchPlaceBreak-*.*.*.jar'
          }
        ],
        labels: ['t:release']
      }
    ],
    [
      '@semantic-release/exec',
      {
        prepareCmd: './scripts/generate_papermc_plugin.sh ${nextRelease.version}'
      }
    ],
  ]
}

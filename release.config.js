const CHANGELOG_FILE = '.github/CHANGELOG.md';

module.exports = {
  preset: 'conventionalcommits',

  branches: [
    'main',
    'next',
    'next-major',
    '+([0-9])?(.{+([0-9]),x}).x',
    {name: 'beta', prerelease: true},
    {name: 'alpha', prerelease: true}
  ],

  plugins: [
    [
      '@semantic-release/commit-analyzer',
      {
        releaseRules: [
          {type: 'refactor', release: 'patch'},
          {type: 'build', release: 'patch'}
        ]
      }
    ],
    [
      '@semantic-release/release-notes-generator',
      {
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
            {type: 'docs', section: '📖 Documentation'}
          ],
          releaseCommitMessageFormat: 'docs(changelog): release {{currentTag}}',
        }
      }
    ],
    [
      '@semantic-release/changelog',
      {
        changelogFile: CHANGELOG_FILE
      }
    ],
    [
      'semantic-release-github-pullrequest',
      {
        assets: [CHANGELOG_FILE],
        pullrequestTitle: 'docs(changelog): release {{currentTag}}',
        labels: ['t:release']
      }
    ],
    [
      '@semantic-release/exec',
      {
        prepareCmd: './scripts/generate-plugin.sh ${nextRelease.version}',
        publishCmd: 'export TAG_NAME=${nextRelease.gitTag}'
      }
    ],
    [
      '@semantic-release/github',
      {
        assets: [
          {
            path: 'src/spigot-plugin/target/JobsReborn-PatchPlaceBreak-${nextRelease.version}.jar',
            label: 'Spigot plugin for Minecraft servers'
          }
        ],
        labels: ['t:release'],
        releasedLabels: ['t:released-${nextRelease.gitTag}']
      }
    ]
  ]
}

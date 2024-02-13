module.exports = {
  preset: 'conventionalcommits',

  branches: [
    'main',
    'next',
    'next-major',
    'release/v+([0-9])?(.{+([0-9]),x}).x',
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
          ]
        }
      }
    ],
    [
      '@semantic-release/changelog',
      {
        changelogFile: process.env.CHANGELOG_FILE
      }
    ],
    [
      '@semantic-release/exec',
      {
        prepareCmd: './scripts/generate-plugin.sh ${nextRelease.version}',
        publishCmd: 'echo "Printing tag version name in temporary file..." && '
          + 'touch "${TMP_TAG_VERSION_NAME_FILE}" && '
          + 'echo "${nextRelease.gitTag}" > ${TMP_TAG_VERSION_NAME_FILE}'
      }
    ],
    [
      '@semantic-release/github',
      {
        assets: [
          {
            path: '../src/spigot-plugin/target/JobsReborn-PatchPlaceBreak-*.*.*.jar',
            label: 'Spigot plugin for Minecraft servers'
          }
        ],
        labels: ['t:release'],
        releasedLabels: ['t:released-${nextRelease.gitTag}']
      }
    ]
  ]
}

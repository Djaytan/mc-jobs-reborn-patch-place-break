module.exports = {
  extends: ['@commitlint/config-conventional'],
  rules: {
    'scope-enum': [2, 'always', [
      'core', 'spigot-adapter', 'spigot-plugin', 'readme', 'contributing',
      'changelog', 'packaging', 'deps', 'other', 'github', 'renovate', 'release'
    ]],
    'type-enum': [2, 'always', [
      'feat', 'fix', 'perf', 'revert', 'refactor', 'build', 'test', 'ci', 'docs'
    ]]
  }
}

module.exports = {
  extends: ['@commitlint/config-conventional'],
  rules: {
    // Project specific rules
    'scope-enum': [2, 'always', [
      'core', 'paper-adapter', 'paper-plugin', 'readme', 'contributing',
      'packaging', 'deps', 'other', 'github', 'renovate', 'release'
    ]],

    // Compliant with Angular conventions (getting rid of "style" and "chore" types)
    'type-enum': [2, 'always', [
      'feat', 'fix', 'perf', 'revert', 'refactor', 'build', 'test', 'ci',
      'docs', 'chore'
    ]],

    // Adjust some rules predefined by @commitlint/config-conventional
    'body-max-line-length': [0, 'always', 'Infinity'],
    'footer-max-line-length': [0, 'always', 'Infinity']
  }
}

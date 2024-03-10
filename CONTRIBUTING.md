# Versioning

## Versioning rules

This library is versioned as follows

`<major>.<minor>.<patch>`

- Upgrades to `<major>` include disruptive changes, such as feature additions and modifications.
- Version upgrades for `<minor>` include non-disruptive changes.
- Version upgrades for `<patch>` include bug fixes and other non-disruptive changes.

## git operations

- **Rule 1** For each major and minor version, create a version branch (in the format `develop/<major>.<minor>.<patch>`) and have all working branches based on the version branch.
- **Rule 2** Whenever a version is updated, merge the version branch of that version into the main branch. Also, when merging, tag with the format `v<major>.<minor>.<patch>` and create a github release for each version tag. (With jitpack set up, each release triggers documentation updates, allowing users to access the library.)

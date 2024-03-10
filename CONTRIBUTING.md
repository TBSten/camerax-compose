# Versioning

## Versioning rules

This library is versioned as follows

`<major>.<minor>.<patch>`

- Upgrades to `<major>` include disruptive changes, such as feature additions and modifications.
- Version upgrades for `<minor>` include non-disruptive changes.
- Version upgrades for `<patch>` include bug fixes and other non-disruptive changes.

## git operations

- **Rule 1** For each major and minor version, create a version branch (format is `develop/<major>.<minor>.<patch>`) and have all working branches based on the version branch.
- **Rule 2** To release the version in question, do the following This will ensure that the latest released version will always be present in HEAD on the main branch.
  - Merge the version branch into the main branch.
  - After the merge, tag the main branch merge commit with the format `v<major>.<minor>.<patch>`.
  - Create a github release.
    - With jitpack set up, each release triggers documentation updates, allowing users to access the library.

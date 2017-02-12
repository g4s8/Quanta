# Quanta
[![Build Status](https://img.shields.io/travis/g4s8/Quanta.svg?style=flat-square)](https://travis-ci.org/g4s8/Quanta)
[![License](https://img.shields.io/github/license/g4s8/Quanta.svg?style=flat-square)](https://github.com/g4s8/Quanta/blob/master/LICENSE)
[![Bintray](https://img.shields.io/bintray/v/g4s8/maven-android/com.g4s8.quanta.svg?style=flat-square)](https://bintray.com/g4s8/maven-android/com.g4s8.quanta/_latestVersion)

## «Quality Analysis Tool for Android»
Inspired by [Qulice](https://github.com/teamed/qulice)

[Quanta](https://github.com/g4s8/Quanta/) aggregates multiple quality checks in one gradle plugin.

Included tools:
 - Checkstyle
 - PMD
 - Android lint
 - Findbugs

## Setup
1. Add plugin to your buildscript
```gradle
// root build.gradle
buildscript {
  repositories {
      jcenter()
  }
  dependencies {
      classpath 'com.g4s8:quanta:0.1.1'
  }
}
```
```gradle
// app build.gradle
apply plugin: 'com.g4s8.quanta'
 ```
 
2. Run `./gradlew check`
`check` task will trigger all analyzer tasks: `checkstyle`, `pmd`, `lint`, `findbugs`


 
 ### Credits:
 - Thank [@yegor256](https://github.com/yegor256) for [idea](https://github.com/teamed/qulice)
 - Thank [@vincentbrison](https://github.com/vincentbrison/) for some [configuration files](https://github.com/vincentbrison/vb-android-app-quality)

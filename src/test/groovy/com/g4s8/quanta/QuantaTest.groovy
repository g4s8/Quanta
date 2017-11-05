package com.g4s8.quanta

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

/**
 * FIXME: Caused by: groovy.lang.MissingPropertyException:
 *  Could not get unknown property 'reportsDir' for root project 'test' of type org.gradle.api.Project.
 */
class QuantaTest {

  Project proj

  @Before
  void setUp() {
    proj = ProjectBuilder.builder().build()
    proj.pluginManager.apply('com.g4s8.quanta')
    proj.tasks.create('check')
    proj.evaluationDependsOnChildren()
  }

  @Test
  @Ignore
  void checkstyle() {
    def task = proj.tasks.findByName('checkstyle')
    MatcherAssert.assertThat(task, Matchers.notNullValue())
    def check = proj.tasks.findByName('check')
    MatcherAssert.assertThat(check.dependsOn.contains(task), Matchers.is(true))
  }

  @Test
  @Ignore
  void pmd() {
    def task = proj.tasks.findByName('pmd')
    MatcherAssert.assertThat(task, Matchers.notNullValue())
    def check = proj.tasks.findByName('check')
    MatcherAssert.assertThat(check.dependsOn.contains(task), Matchers.is(true))
  }

  @Test
  @Ignore
  void findbugs() {
    def task = proj.tasks.findByName('findbugs')
    MatcherAssert.assertThat(task, Matchers.notNullValue())
    def check = proj.tasks.findByName('check')
    MatcherAssert.assertThat(check.dependsOn.contains(task), Matchers.is(true))
  }
}

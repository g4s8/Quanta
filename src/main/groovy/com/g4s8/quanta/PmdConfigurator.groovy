package com.g4s8.quanta

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.plugins.quality.Pmd

final class PmdConfigurator implements Action<Pmd> {

  private final Project project

  PmdConfigurator(Project project) {
    this.project = project
  }

  @Override
  void execute(final Pmd self) {
    self.ignoreFailures = false
    self.ruleSetFiles = project.files(project.quanta.pmdConfig.configuration)
    self.ruleSets = []

    self.source = 'src'
    self.include = '**/*.java'
    self.exclude = '**/gen/**'

    self.reports {
      xml.enabled = false
      html.enabled = true
      xml {
        destination "${project.reportsDir}/pmd/pmd.xml"
      }
      html {
        destination "${project.reportsDir}/pmd/pmd.html"
      }
    }
  }
}

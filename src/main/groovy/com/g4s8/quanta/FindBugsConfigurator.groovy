package com.g4s8.quanta

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.plugins.quality.FindBugs

final class FindBugsConfigurator implements Action<FindBugs> {

  private final Project project

  FindBugsConfigurator(Project project) {
    this.project = project
  }

  @Override
  void execute(final FindBugs self) {
    self.ignoreFailures = false
    self.effort = "max"
    self.reportLevel = "high"
    self.excludeFilter = project.quanta.findBugsConfig.exclude
    self.classes = project.files("${project.buildDir}/intermediates/classes")

    self.source = 'src'
    self.include '**/*.java'
    self.exclude '**/gen/**'

    self.reports {
      xml.enabled = false
      html.enabled = true
      xml {
        destination "${project.reportsDir}/findbugs/findbugs.xml"
      }
      html {
        destination "${project.reportsDir}/findbugs/findbugs.html"
      }
    }
  }
}

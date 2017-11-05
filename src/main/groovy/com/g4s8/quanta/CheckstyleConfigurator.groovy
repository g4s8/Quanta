package com.g4s8.quanta

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.plugins.quality.Checkstyle

final class CheckstyleConfigurator implements Action<Checkstyle> {

  private final Project project

  CheckstyleConfigurator(Project project) {
    this.project = project
  }

  @Override
  void execute(final Checkstyle self) {
    def cfg = project.quanta.checkstyleConfig
    self.configFile = cfg.configuration
    if (cfg.suppressions != null) {
      self.configProperties.checkstyleSuppressionsPath = cfg.suppressions
    }
    self.source = 'src'
    self.include '**/*.java'
    self.exclude '**/gen/**'
    self.classpath = project.files()
  }
}

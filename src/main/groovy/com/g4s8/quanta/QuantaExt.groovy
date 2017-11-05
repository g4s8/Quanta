package com.g4s8.quanta

import com.g4s8.quanta.config.CheckstyleConfig
import com.g4s8.quanta.config.FindBugsConfig
import com.g4s8.quanta.config.LintConfig
import com.g4s8.quanta.config.PmdConfig

class QuantaExt {
  private final CheckstyleConfig checkstyleConfig = new CheckstyleConfig()
  private final FindBugsConfig findBugsConfig = new FindBugsConfig()
  private final PmdConfig pmdConfig = new PmdConfig()
  private final LintConfig lintConfig = new LintConfig()

  def checkstyle(Closure<CheckstyleConfig> closure) {
    closure(checkstyleConfig)
  }

  def findBugs(Closure<FindBugsConfig> closure) {
    closure(findBugsConfig)
  }

  def pmd(Closure<PmdConfig> closure) {
    closure(pmdConfig)
  }

  def lint(Closure<LintConfig> closure) {
    closure(lintConfig)
  }

  def getCheckstyleConfig() {
    checkstyleConfig
  }

  def getFindBugsConfig() {
    findBugsConfig
  }

  def getPmdConfig() {
    pmdConfig
  }

  def getLintConfig() {
    lintConfig
  }
}
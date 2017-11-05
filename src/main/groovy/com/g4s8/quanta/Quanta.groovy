package com.g4s8.quanta

import java.util.zip.ZipFile
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.quality.Checkstyle
import org.gradle.api.plugins.quality.FindBugs
import org.gradle.api.plugins.quality.Pmd

/**
 * Plugin implementation.
 */
class Quanta implements Plugin<Project> {

  @Override
  void apply(Project project) {
    project.extensions.create('quanta', QuantaExt)
    initialize(project).with { qnt ->
      if (qnt.checkstyleConfig.enabled) {
        project.apply plugin: 'checkstyle'
        project.tasks.create("checkstyle", Checkstyle.class, new CheckstyleConfigurator(project))
        project.tasks.getByName('check').dependsOn 'checkstyle'
        project.logger.info 'Added Checkstyle task'
      }

      if (qnt.pmdConfig.enabled) {
        project.apply plugin: 'pmd'
        project.tasks.create("pmd", Pmd.class, new PmdConfigurator(project))
        project.tasks.getByName('check').dependsOn 'pmd'
        project.logger.info 'Added PMD task'
      }

      if (qnt.findBugsConfig.enabled) {
        project.apply plugin: 'findbugs'
        project.tasks.create('findbugs', FindBugs.class, new FindBugsConfigurator(project))
        project.tasks.getByName('findbugs').dependsOn 'assembleDebug'
        project.tasks.getByName('check').dependsOn 'findbugs'
        project.logger.info 'Added Findbugs task'
      }

      if (qnt.lintConfig.enabled) {
        project.android {
          lintOptions {
            abortOnError true
            xmlReport false
            htmlReport true
            lintConfig qnt.lintConfig.configuration
            htmlOutput project.file("${project.reportsDir}/lint/lint-result.html")
            xmlOutput project.file("${project.reportsDir}/lint/lint-result.xml")
          }
        }
        project.tasks.getByName('check').dependsOn 'lint'
        project.logger.info 'Added Android-Lint task'
      }
    }
  }

  private static QuantaExt initialize(Project project) {
    def out = new File(project.buildDir, 'quanta').with {
      it.delete()
      it.mkdirs()
      it
    }
    project.logger.info "Initializing Quanta in: $out"
    def qnt = project.quanta
    if (qnt.checkstyleConfig.enabled) {
      qnt.checkstyleConfig.configuration = qnt.checkstyleConfig.configuration ?:
        copyFromBundle(out, '/config/checkstyle', 'checkstyle.xml')
      qnt.checkstyleConfig.suppressions = qnt.checkstyleConfig.suppressions ?:
        copyFromBundle(out, '/config/checkstyle', 'suppressions.xml')
    }
    if (qnt.pmdConfig.enabled) {
      qnt.pmdConfig.configuration = qnt.pmdConfig.configuration ?:
        copyFromBundle(out, '/config/pmd', 'pmd-ruleset.xml')
    }
    if (qnt.findBugsConfig.enabled) {
      qnt.findBugsConfig.exclude = qnt.findBugsConfig.exclude ?:
        copyFromBundle(out, '/config/findbugs', 'findbugs-filter.xml')
    }
    if (qnt.lintConfig.enabled) {
      qnt.lintConfig.configuration = qnt.lintConfig.configuration ?:
        copyFromBundle(out, '/config/lint', 'lint.xml')
    }
    qnt
  }

  private static File copyFromBundle(File outdir, String path, String name) {
    def file = new File(outdir, name)
    bundledStream(new File(path, name).absolutePath).withCloseable {
      def src = it
      file.newOutputStream().withCloseable { it.bytes = src.bytes }
    }
    file
  }

  private static InputStream bundledStream(String name) {
    URL url = Quanta.class.classLoader.getResource(name.startsWith('/') ? name.substring(1) : name)
    if (url == null) {
      throw new IOException("Resource was not found: $name")
    }

    InputStream res
    try {
      res = url.openStream()
    } catch (IOException ignored) {
      // fallback to read JAR directly
      def connection = url.openConnection() as JarURLConnection
      def jarFile = connection.jarFileURL.toURI()
      ZipFile zip
      try {
        zip = new ZipFile(new File(jarFile))
      } catch (FileNotFoundException err) {
        throw new IOException("Resource was not found in jar: $name", err)
      }
      res = zip.getInputStream((zip.getEntry(name)))
    }
    res
  }
}

package com.g4s8.quanta

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
        def resDir = File.createTempFile('quanta', '.res')
        resDir.delete()
        resDir.mkdir()
        resDir.deleteOnExit()
        project.logger.info "Created temporary resource directory: $resDir"

        if (project.quanta.checkstyle) {
            project.apply plugin: 'checkstyle'
            project.tasks.create("checkstyle", Checkstyle.class) {
                configFile copy("/config/checkstyle/checkstyle.xml", resDir)
                configProperties.checkstyleSuppressionsPath = copy("/config/checkstyle/suppressions.xml", resDir).absolutePath
                source 'src'
                include '**/*.java'
                exclude '**/gen/**'
                classpath = project.files()
            }
            project.tasks.getByName('check').dependsOn 'checkstyle'
        }

        if (project.quanta.pmd) {
            project.apply plugin: 'pmd'
            project.tasks.create("pmd", Pmd.class) {
                ignoreFailures = false
                ruleSetFiles = project.files(copy("config/pmd/pmd-ruleset.xml", resDir))
                ruleSets = []

                source 'src'
                include '**/*.java'
                exclude '**/gen/**'

                reports {
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
            project.tasks.getByName('check').dependsOn 'pmd'
        }

        if (project.quanta.lint) {
            project.android {
                lintOptions {
                    abortOnError true
                    xmlReport false
                    htmlReport true
                    lintConfig copy("/config/lint/lint.xml", resDir)
                    htmlOutput project.file("${project.reportsDir}/lint/lint-result.html")
                    xmlOutput project.file("${project.reportsDir}/lint/lint-result.xml")
                }
            }
            project.tasks.getByName('check').dependsOn 'lint'
        }

        if (project.quanta.findbugs) {
            project.apply plugin: 'findbugs'
            project.tasks.create('findbugs', FindBugs.class) {
                ignoreFailures = false
                effort = "max"
                reportLevel = "high"
                excludeFilter = copy("/config/findbugs/findbugs-filter.xml", resDir)
                classes = project.files("${project.buildDir}/intermediates/classes")

                source 'src'
                include '**/*.java'
                exclude '**/gen/**'

                reports {
                    xml.enabled = false
                    html.enabled = true
                    xml {
                        destination "${project.reportsDir}/findbugs/findbugs.xml"
                    }
                    html {
                        destination "${project.reportsDir}/findbugs/findbugs.html"
                    }
                }

                classpath = project.files()
            }
            project.tasks.getByName('findbugs').dependsOn 'assembleDebug'
            project.tasks.getByName('check').dependsOn 'findbugs'
        }
    }

    private static File copy(String res, File resDir) {
        def out = new File(resDir, res)
        out.mkdirs()
        out.delete()
        new FileOutputStream(out).write(new ResInputStream(res).bytes)
        return out
    }
}

package org.jetbrains.gui.classpath

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.util.*

/**
 * @author Sergey Karashevich
 */

class ClassPathBuilder(val ideaLibPath: String, val jdkPath: String, val jUnitPath: String, val festLibsPath: String, val testGuiFrameworkPath: String) {

    val LOG: Logger = LogManager.getLogger(this.javaClass)

    val isJar : (String) -> Boolean = { path -> path.endsWith(".jar") }
    val isFestJar : (String) -> Boolean = { path -> path.endsWith(".jar") && path.toLowerCase().contains("fest")}
    val toPath : (File) -> String = { file -> file.path }

    fun build(guiTestPath: String) = build(listOf(guiTestPath))

    fun build(guiTestPaths: List<String>): String {
        val cp = ArrayList<String>()
        val ideaLibJars = File(ideaLibPath).listFiles().map(toPath).filter(isJar)
        val jdkJars = File(jdkPath + File.separator + "lib").getFilesRecursive().filter(isJar) +
                File(jdkPath + File.separator + "jre" + File.separator + "lib").getFilesRecursive().filter(isJar)
        val festJars = File(festLibsPath).listFiles().map(toPath).filter(isFestJar)

        LOG.info("Building classpath for IDE path: $ideaLibPath \t (${ideaLibJars.size} jar(s))")
        LOG.info("Building classpath for JDK path: $jdkPath \t (${jdkJars.size} jar(s))")
        LOG.info("Building classpath for JUnit path: $jUnitPath")
        LOG.info("Building classpath for FEST jars path: $festLibsPath (${festJars.size} jar(s))")
        LOG.info("Building classpath for testGuiFramework path: $testGuiFrameworkPath")
        LOG.info("Building classpath for GUI tests paths: $guiTestPaths")

        return cp.plus(ideaLibJars)
                .plus(jdkJars)
                .plus(festJars)
                .plus(jUnitPath)
                .plus(testGuiFrameworkPath)
                .plus(guiTestPaths)
                .buildOsSpecific()
    }

    private fun File.getFilesRecursive(): List<String> {
        if (this.isFile) return listOf(this.path)
        else return this.listFiles().map {file -> file.getFilesRecursive()}.flatten()
    }


    private fun List<String>.buildOsSpecific() = if (System.getProperty("os.name").toLowerCase().contains("win")) this.buildForWin() else this.buildForUnix()
    private fun List<String>.buildForUnix() = this.joinToString(separator = ":")
    private fun List<String>.buildForWin() = this.joinToString(separator = ";")

}
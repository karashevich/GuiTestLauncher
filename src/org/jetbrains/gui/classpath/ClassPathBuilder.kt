package org.jetbrains.gui.classpath

import java.io.File
import java.util.*

/**
 * @author Sergey Karashevich
 */

class ClassPathBuilder(val ideaLibPath: String, val jdkPath: String, val jUnitPath: String, val festLibsPath: String, val testGuiFrameworkPath: String) {

    val isJar : (String) -> Boolean = { path -> path.endsWith(".jar") }
    val isFestJar : (String) -> Boolean = { path -> path.endsWith(".jar") && path.toLowerCase().contains("fest")}
    val toPath : (File) -> String = { file -> file.path }

    fun build(guiTestPath: String): String {
        val cp = ArrayList<String>()
        val ideaLibJars = File(ideaLibPath).listFiles().map(toPath).filter(isJar)
        val jdkJars = File(jdkPath + File.separator + "lib").getFilesRecursive().filter(isJar) +
                File(jdkPath + File.separator + "jre" + File.separator + "lib").getFilesRecursive().filter(isJar)
        val festJars = File(festLibsPath).listFiles().map(toPath).filter(isFestJar)

        return cp.plus(ideaLibJars)
                .plus(jdkJars)
                .plus(festJars)
                .plus(jUnitPath)
                .plus(testGuiFrameworkPath)
                .plus(guiTestPath)
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
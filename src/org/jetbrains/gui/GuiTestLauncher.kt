package org.jetbrains.gui

import org.jetbrains.gui.classpath.ClassPathBuilder
import org.jetbrains.gui.teamcity.TeamCityManager.FEST_LIB_PATH
import org.jetbrains.gui.teamcity.TeamCityManager.GUI_TEST_DATA_DIR
import org.jetbrains.gui.teamcity.TeamCityManager.IDEA_PATH
import org.jetbrains.gui.teamcity.TeamCityManager.JDK_PATH
import org.jetbrains.gui.teamcity.TeamCityManager.PLATFORM_PREFIX
import org.jetbrains.gui.teamcity.TeamCityManager.TEST_GUI_FRAMEWORK_PATH
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors

/**
 * @author Sergey Karashevich
 */


object GuiTestLauncher {

    val demoTestClass = "com.intellij.testGuiFramework.tests.SimpleGitTest"
    val demoTestClassPath = "/Users/jetbrains/IdeaProjects/idea-ultimate/out/classes/test/git4idea/"

    val JUNIT_STARTER = "com.intellij.rt.execution.junit.JUnitStarter"
    val IDE_VERSION = "-ideVersion5"

    val ideaLibPath = "$IDEA_PATH${File.separator}lib"
    val javaExec = "$JDK_PATH${File.separator}bin${File.separator}java"
    val jUnitPath = "/Users/jetbrains/Library/Application Support/JetBrains/Toolbox/apps/IDEA-U/ch-0/171.2601/IntelliJ IDEA 2017.1 EAP.app/Contents/plugins/junit/lib/junit-rt.jar"
    val SET_GUI_TEST_DATA_DIR = "-DGUI_TEST_DATA_DIR=$GUI_TEST_DATA_DIR"

    val jdkPath = "/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jbsdk/Contents/Home/"
    val festLibPath = "/Users/jetbrains/IdeaProjects/GuiScriptRecorder/lib"
    val testGuiFrameworkPath = "/Users/jetbrains/IdeaProjects/GuiScriptRecorder/lib/testGuiFramework-platform.jar"
    val MY_PLATFORM_PREFIX = "-Didea.platform.prefix=Idea"

    fun createArgs(ideaLibPath: String, testClass: String = demoTestClass, testClassPath: String = demoTestClassPath): List<String> {
        val classpath = ClassPathBuilder(ideaLibPath, JDK_PATH, jUnitPath, FEST_LIB_PATH, TEST_GUI_FRAMEWORK_PATH).build(testClassPath)
        return listOf(javaExec,
                PLATFORM_PREFIX,
                SET_GUI_TEST_DATA_DIR,
                "-classpath",
                classpath,
                JUNIT_STARTER,
                IDE_VERSION,
                testClass)
    }


    @JvmStatic
    fun main(args: Array<String>) {
        val ideaTestThread = Thread(runnable, "IdeaTestThread")
        ideaTestThread.run()
        println("Type anything to close program...")
        System.`in`.read()
    }

    val runnable: () -> Unit = {
        val ideaStartTest = ProcessBuilder().inheritIO().command(createArgs(ideaLibPath))
        val process = ideaStartTest.start()
        val wait = process.waitFor(600, TimeUnit.MINUTES)
        assert(wait)
        if (process.exitValue() != 1) println("Execution successful") else {
            System.err.println("Process execution error:")
            System.err.println(BufferedReader(InputStreamReader(process.errorStream)).lines().collect(Collectors.joining("\n")))
        }
    }
}
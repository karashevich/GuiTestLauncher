package org.jetbrains.gui

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.jetbrains.gui.classpath.ClassPathBuilder
import org.jetbrains.gui.file.PathManager
import org.jetbrains.gui.file.PathManager.toSysSpecPath
import org.jetbrains.gui.ide.Ide
import org.jetbrains.gui.ide.IdeRunArgs
import org.jetbrains.gui.ide.IdeTestFixture
import org.jetbrains.gui.teamcity.TeamCityManager.CUSTOM_CONFIG_PATH
import org.jetbrains.gui.teamcity.TeamCityManager.FEST_LIB_PATH
import org.jetbrains.gui.teamcity.TeamCityManager.GUI_TEST_DATA_DIR
import org.jetbrains.gui.teamcity.TeamCityManager.IDEA_PATH
import org.jetbrains.gui.teamcity.TeamCityManager.JDK_PATH
import org.jetbrains.gui.teamcity.TeamCityManager.PLATFORM_PREFIX
import org.jetbrains.gui.teamcity.TeamCityManager.TEST_CLASSES_DIR
import org.jetbrains.gui.teamcity.TeamCityManager.TEST_GUI_FRAMEWORK_PATH
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.stream.Collectors

/**
 * @author Sergey Karashevich
 */


object GuiTestLauncher {

    val JUNIT_STARTER = "org.junit.runner.JUnitCore"
    val IDE_VERSION = "-ideVersion5"

    val javaExec = "$JDK_PATH${File.separator}bin${File.separator}java"
    val jUnitPath = "$IDEA_PATH${File.separator}${toSysSpecPath("plugins/junit/lib/junit-rt.jar")}"
    val SET_GUI_TEST_DATA_DIR = "-DGUI_TEST_DATA_DIR=$GUI_TEST_DATA_DIR"
    val SET_CUSTOM_CONFIG_PATH= "-DCUSTOM_CONFIG_PATH=${CUSTOM_CONFIG_PATH ?: ""}"

    val LOG: Logger = LogManager.getLogger(this.javaClass)

    fun createArgs(ideaLibPath: String, testClass: String, testClassPath: String): List<String> {
        val classpath = ClassPathBuilder(ideaLibPath, JDK_PATH, jUnitPath, FEST_LIB_PATH, TEST_GUI_FRAMEWORK_PATH).build(testClassPath)
        return listOf(javaExec,
                "-Didea.platform.prefix=$PLATFORM_PREFIX",
                SET_GUI_TEST_DATA_DIR,
                SET_CUSTOM_CONFIG_PATH,
                "-classpath",
                classpath,
                JUNIT_STARTER,
//                IDE_VERSION,
                testClass)
    }

    fun createArgs(ideaLibPath: String, testClasses: List<String>, testClassPath: String): List<String> {
        val classpath = ClassPathBuilder(ideaLibPath, JDK_PATH, jUnitPath, FEST_LIB_PATH, TEST_GUI_FRAMEWORK_PATH).build(testClassPath)
        return listOf(javaExec,
                "-Didea.platform.prefix=$PLATFORM_PREFIX",
                SET_GUI_TEST_DATA_DIR,
                SET_CUSTOM_CONFIG_PATH,
                "-classpath",
                classpath,
                JUNIT_STARTER,
                IDE_VERSION,
                testClasses.joinToString(", "))
    }

    fun createArgs(ideaLibPath: String, ideRunArgs: IdeRunArgs): List<String> {
        val classpath = ClassPathBuilder(ideaLibPath, JDK_PATH, jUnitPath, FEST_LIB_PATH, TEST_GUI_FRAMEWORK_PATH).build(TEST_CLASSES_DIR)
        return (listOf<String>(javaExec)
                .plus(ideRunArgs.jvmParams)
                .plus(ideRunArgs.ideParams)
                .plus("-classpath")
                .plus(classpath)
                .plus(JUNIT_STARTER)
                .plus(IDE_VERSION)
                .plus(ideRunArgs.testClasses))
    }

    fun runIde(ide: Ide, testClass: String): Unit = runIde(ide, listOf(testClass))

    fun runIde(ide: Ide, ideRunArgs: IdeRunArgs): Unit {
        LOG.info("Running $ide with $ideRunArgs")
        val pathToSave = PathManager.getWorkDirPath()
        LOG.info("IDE path to save is set to: $pathToSave")
        val runnable: () -> Unit = {
            val ideaStartTest = ProcessBuilder().inheritIO().command(
                    createArgs(ideaLibPath = PathManager.getSystemSpecificIdeLibPath(pathToSave), ideRunArgs = ideRunArgs))
            val process = ideaStartTest.start()
            val wait = process.waitFor()
            if (process.exitValue() != 1) println("Execution successful") else {
                System.err.println("Process execution error:")
                System.err.println(BufferedReader(InputStreamReader(process.errorStream)).lines().collect(Collectors.joining("\n")))
            }
        }
        val ideaTestThread = Thread(runnable, "IdeaTestThread")
        ideaTestThread.run()
    }

    fun runIde(ide: Ide, testClasses: List<String>): Unit {
        LOG.info("Running $ide for test classes: ${testClasses.joinToString(", ")}")
        val pathToSave = PathManager.getWorkDirPath()
        val runnable: () -> Unit = {
            val args = createArgs(ideaLibPath = PathManager.getSystemSpecificIdeLibPath(pathToSave), testClasses = testClasses, testClassPath = TEST_CLASSES_DIR)
            val ideaStartTest = ProcessBuilder().inheritIO().command(args)
            val process = ideaStartTest.start()
            val wait = process.waitFor()
            if (process.exitValue() != 1) println("Execution successful") else {
                System.err.println("Process execution error:")
                System.err.println(BufferedReader(InputStreamReader(process.errorStream)).lines().collect(Collectors.joining("\n")))
            }
        }
        val ideaTestThread = Thread(runnable, "IdeaTestThread")
        ideaTestThread.run()
    }

    fun runIde(ide: Ide, func: IdeTestFixture.() -> Unit) {
        val ideTestFixture = IdeTestFixture(ide)
        func.invoke(ideTestFixture)
        runIde(ide, ideTestFixture.buildArgs())
    }

    fun multiDpiTest(ide: Ide, dpiScale: String, func: IdeTestFixture.() -> Unit) {
        val ideTestFixture = IdeTestFixture(ide)
        func.invoke(ideTestFixture)
        runIde(ide, ideTestFixture.buildArgs())
        ideTestFixture.jvmConfig(listOf("-Dsun.java2d.uiScale.enabled=true", "-Dsun.java2d.uiScale=$dpiScale"))
        runIde(ide, ideTestFixture.buildArgs())
    }

    fun multiDpiTest(ide: Ide, dpiScale: Float, func: IdeTestFixture.() -> Unit) = multiDpiTest(ide, dpiScale.toString(), func)

}
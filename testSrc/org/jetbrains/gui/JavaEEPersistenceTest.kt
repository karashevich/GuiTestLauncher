package org.jetbrains.gui

import junit.framework.Assert.fail
import org.jetbrains.gui.GuiTestLauncher.createArgs
import org.jetbrains.gui.file.PathManager
import org.jetbrains.gui.ide.Ide
import org.jetbrains.gui.ide.IdeType
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors

/**
 * @author Sergey Karashevich
 */

class JavaEEPersistenceTest {

    val ide = Ide(ideType = IdeType.IDEA_COMMUNITY, version = 171, build = 3085)
    var pathToSave: String? = null

//    internal fun setUp() {
//        pathToSave = PathManager.getWorkDirPath()
//        val path = "$pathToSave${File.separator}${ide.ideType.id}-${ide.version}.${ide.build}.zip"
//
//        IdeDownloader.download(IdeDownloader.buildUrl(ide), path)
//        IdeDownloader.unpack(path)
//        IdeDownloader.unscramble(ide, pathToSave!!)
//    }

    @Test
    fun testRunIdea() {
        val runnable: () -> Unit = {
            val ideaStartTest = ProcessBuilder().inheritIO().command(createArgs(ideaLibPath = PathManager.getSystemSpecificIdeLibPath(pathToSave!!),
                    testClass = "com.intellij.testGuiFramework.tests.JavaEEProjectTest",
                    testClassPath = "/Users/jetbrains/IdeaProjects/idea-ultimate/out/classes/test/javaee_tests/"))
            val process = ideaStartTest.start()
            val wait = process.waitFor(600, TimeUnit.MINUTES)
            assert(wait)
            if (process.exitValue() != 1) println("Execution successful") else {
                val errMessage = BufferedReader(InputStreamReader(process.errorStream)).lines().collect(Collectors.joining("\n"))
                System.err.println(errMessage)
                fail(errMessage)
            }
        }

        val ideaTestThread = Thread(runnable, "IdeaTestThread")
        ideaTestThread.run()
    }
}
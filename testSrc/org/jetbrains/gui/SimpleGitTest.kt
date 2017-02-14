package org.jetbrains.gui

import org.jetbrains.gui.GuiTestLauncher.createArgs
import org.jetbrains.gui.download.IdeDownloader
import org.jetbrains.gui.file.PathManager
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors

/**
 * @author Sergey Karashevich
 */

class SimpleGitTest {

    val ide = IdeDownloader.Ide(ideType = IdeDownloader.IdeType.IDEA_COMMUNITY, version = 171, build = 3085)
    var pathToSave: String? = null

    @BeforeEach
    internal fun setUp() {
        pathToSave = PathManager.getWorkDirPath()
//        val path = "$pathToSave${File.separator}${ide.ideType.id}-${ide.version}.${ide.build}.zip"
//
//        IdeDownloader.download(IdeDownloader.buildUrl(ide), path)
//        IdeDownloader.unpack(path)
//        IdeDownloader.unscramble(ide, pathToSave!!)
    }

    @Test
    fun testGit() {
        val runnable: () -> Unit = {
            val ideaStartTest = ProcessBuilder().inheritIO().command(createArgs(ideaLibPath = "$pathToSave${File.separator}lib"))
            val process = ideaStartTest.start()
            val wait = process.waitFor(600, TimeUnit.MINUTES)
            assert(wait)
            if (process.exitValue() != 1) println("Execution successful") else  {
                val errMessage = BufferedReader(InputStreamReader(process.errorStream)).lines().collect(Collectors.joining("\n"))
                System.err.println(errMessage)
                Assertions.fail(errMessage)
            }
        }

        val ideaTestThread = Thread(runnable, "IdeaTestThread")
        ideaTestThread.run()
    }
}
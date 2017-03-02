package org.jetbrains.gui

import org.jetbrains.gui.file.PathManager
import org.jetbrains.gui.ide.Ide
import org.jetbrains.gui.ide.IdeType
import org.jetbrains.gui.system.SystemInfo
import org.junit.jupiter.api.Test
import java.io.File

/**
 * Created by Sergey.Karashevich on 2/28/2017.
 */
class PostCSSPluginTest {

    val ide = Ide(ideType = IdeType.IDEA_COMMUNITY, version = 172, build = 160)
    val ext = SystemInfo.getExt()
    val pathToSave = PathManager.getWorkDirPath()
    val path = "$pathToSave${File.separator}${ide.ideType.id}-${ide.version}.${ide.build}.$ext"

//    @Test
//    fun installTest() {
//        IdeDownloader.download(IdeDownloader.buildUrl(ide = ide, extension = ext), path)
//        IdeDownloader.unpack(path)
//        IdeDownloader.unscramble(ide, pathToSave)
//    }

    @Test
    fun testPluginInstall() {
        GuiTestLauncher.runIde(ide) {
            config("/Users/jetbrains/IdeaProjects/temp/config")
            installPlugin("PostCSS")
        }
    }

}


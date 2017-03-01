package org.jetbrains.gui

import org.jetbrains.gui.GuiTestLauncher.multiDpiTest
import org.jetbrains.gui.dpi.DpiComparator
import org.jetbrains.gui.ide.Ide
import org.jetbrains.gui.ide.IdeType
import org.junit.jupiter.api.Test

/**
 * @author Sergey Karashevich
 */
class MultiDpiTest {

    @Test
    fun testMultiDpi() {
        val testIde = Ide(ideType = IdeType.IDEA_COMMUNITY, version = 171, build = 3085)

        multiDpiTest(testIde, 2.0f) {
            config("/Users/jetbrains/IdeaProjects/temp/config")
            debug(port = 1044, suspend = false)
            test(testClassName = "com.intellij.testGuiFramework.tests.ScreenshotTest")
        }

//        DpiComparator.process(TODO("set path to screenshot dir"), TODO("add comparator here"))
    }
}
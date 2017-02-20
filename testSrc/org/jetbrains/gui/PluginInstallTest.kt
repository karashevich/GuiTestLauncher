package org.jetbrains.gui

import org.jetbrains.gui.GuiTestLauncher.runIde
import org.jetbrains.gui.ide.Ide
import org.jetbrains.gui.ide.IdeType
import org.junit.jupiter.api.Test

/**
 * @author Sergey Karashevich
 */
class PluginInstallTest {


    @Test
    fun testPluginInstall() {
        val testIde = Ide(ideType = IdeType.IDEA_COMMUNITY, version = 171, build = 3085)
//        runIde(testIde) {
//            config("/Users/jetbrains/IdeaProjects/temp/config")
//            test("com.intellij.testGuiFramework.tests.InstallPluginTest")
//            test("com.intellij.testGuiFramework.tests.IdeFeaturesTrainerPluginTest")
//        }
        runIde(testIde) {
            jvmConfig(listOf("-Dsun.java2d.uiScale.enabled=true", "-Dsun.java2d.uiScale=2"))
            config("/Users/jetbrains/IdeaProjects/temp/config")
            installPlugin("IDE Feature Trainer")
        }
    }
}
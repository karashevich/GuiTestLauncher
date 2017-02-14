package org.jetbrains.gui.teamcity

/**
 * @author Sergey Karashevich
 */
object TeamCityManager {

    val baseUrl: String  = "http://buildserver.labs.intellij.net"
    val guestAuth = "guestAuth/repository/download"

    val JDK_PATH: String = getSystemOrEnv("JDK_PATH")
    val IDEA_PATH: String = getSystemOrEnv("IDEA_PATH")
    val FEST_LIB_PATH: String = getSystemOrEnv("FEST_LIB_PATH")
    val TEST_GUI_FRAMEWORK_PATH: String = getSystemOrEnv("TEST_GUI_FRAMEWORK_PATH")
    val GUI_TEST_DATA_DIR: String = getSystemOrEnv("GUI_TEST_DATA_DIR")
    val PLATFORM_PREFIX: String = getSystemOrEnv("PLATFORM_PREFIX")

    fun getSystemOrEnv(key: String): String = if (System.getenv(key) != null) System.getenv(key) else System.getProperty(key)

}
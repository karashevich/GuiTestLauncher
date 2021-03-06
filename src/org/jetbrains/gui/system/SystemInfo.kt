package org.jetbrains.gui.system

import org.jetbrains.gui.system.SystemInfo.SystemType.*

/**
 * @author Sergey Karashevich
 */
object SystemInfo {

    enum class SystemType {
        WINDOWS, UNIX, MAC
    }

    fun getSystemType(): SystemType {
        val osName = System.getProperty("os.name").toLowerCase()
        if (osName.contains("win")) return WINDOWS
        else if (osName.contains("mac")) return MAC
        else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) return UNIX
        else throw Exception("Unknown operation system with name: \"$osName\"")
    }

    fun getExt(): String {
        val sysType = getSystemType()
        return when(sysType) {
            WINDOWS -> "exe"
            MAC -> "sit"
            UNIX -> "tar.gz"
        }
    }
}
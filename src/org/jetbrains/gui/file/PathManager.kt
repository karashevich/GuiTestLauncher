package org.jetbrains.gui.file

import org.jetbrains.gui.system.SystemInfo
import java.io.File

/**
 * @author Sergey Karashevich
 */
object PathManager {

    fun getWorkDirPath() = "C:\\Users\\Sergey.Karashevich\\IdeaProjects\\temp"

    fun getSystemSpecificIdePath(workDir: String): String {
        when (SystemInfo.getSystemType()) {
            SystemInfo.SystemType.WINDOWS -> return workDir
            SystemInfo.SystemType.UNIX -> TODO()
            SystemInfo.SystemType.MAC -> return FileUtils.getAppFilePath(workDir)
        }
    }

    fun getSystemSpecificIdeLibPath(workDir: String): String {
        var path = getSystemSpecificIdePath(workDir)
        if (SystemInfo.getSystemType() == SystemInfo.SystemType.MAC)
            path += "${File.separator}Contents"
        path += "${File.separator}lib"
        val file = File(path)
        return file.path
    }

    fun toSysSpecPath(unixStylePath: String) = if (SystemInfo.getSystemType() == SystemInfo.SystemType.WINDOWS) unixStylePath.replace("/", File.separator) else unixStylePath

}
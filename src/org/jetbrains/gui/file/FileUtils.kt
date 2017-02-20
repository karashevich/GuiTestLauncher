package org.jetbrains.gui.file

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.jetbrains.gui.system.SystemInfo
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption


/**
 * @author Sergey Karashevich
 */
object FileUtils {

    val LOG: Logger = LogManager.getLogger(FileUtils::class)

    fun copy(from: String, to: String, copyOption: StandardCopyOption = StandardCopyOption.REPLACE_EXISTING) {
        LOG.info("copying files from $from to $to")
        val fromPath = Paths.get(from)
        val toPath = Paths.get(to) //convert from String to Path
        Files.copy(fromPath, toPath, copyOption)
        LOG.info("done")
    }

    fun getAppFilePath(location: String): String {
        val fromPath = Paths.get(location)
        val fileApp = fromPath.toFile().listFiles().filter {file -> file.name.endsWith(".app")}.firstOrNull() ?: throw FileNotFoundException("Unable to find .app file in $location")
        return fileApp.path
    }

}
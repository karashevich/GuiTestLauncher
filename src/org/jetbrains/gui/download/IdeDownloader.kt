package org.jetbrains.gui.download

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.jetbrains.gui.file.FileUtils
import org.jetbrains.gui.file.PathManager
import org.jetbrains.gui.file.PathManager.getSystemSpecificIdeLibPath
import org.jetbrains.gui.ide.Ide
import org.jetbrains.gui.ide.IdeType
import org.jetbrains.gui.system.SystemInfo
import org.jetbrains.gui.teamcity.TeamCityManager.baseUrl
import org.jetbrains.gui.teamcity.TeamCityManager.guestAuth
import org.jetbrains.gui.zip.ZipUtils
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels


/**
 * @author Sergey Karashevich
 */
object IdeDownloader {

    val LOG: Logger = LogManager.getLogger(IdeDownloader::class)

    val buildArtifactPath = "-{build.number}"
    val unscramblePath = "unscrambled/idea.jar"


    fun download(url: URL, pathFile: String) {
        LOG.info("downloading from URL: $url to $pathFile ...")
        val rbc = Channels.newChannel(url.openStream())
        val fos = FileOutputStream(pathFile)
        fos.channel.transferFrom(rbc, 0, java.lang.Long.MAX_VALUE)
        LOG.info("downloading done")
    }

    fun unpack(pathFile: String) {
        //check extension
        LOG.info("unpacking $pathFile in the same dir ...")
        val workDir = File(pathFile).parentFile
        when(SystemInfo.getSystemType()) {

            SystemInfo.SystemType.WINDOWS -> ZipUtils.unzip(pathFile, workDir.path)
            SystemInfo.SystemType.UNIX -> TODO()
            SystemInfo.SystemType.MAC -> ZipUtils.extractSit(pathFile, workDir.path)
        }
        LOG.info("unpacking done")
    }

    fun unscramble(ide: Ide, workDir: String) {
        val pathToFile = "$workDir${File.separator}idea.jar"
        LOG.info("unscrambling $pathToFile")
        download(buildUnscrambleUrl(ide), pathToFile)
        FileUtils.copy(from = pathToFile, to = "${getSystemSpecificIdeLibPath(workDir)}${File.separator}idea.jar")
        LOG.info("unscrambling done")
    }

    fun buildUrl(ide: Ide, extension: String = "zip"): URL = URL("$baseUrl/$guestAuth/${ide.ideType.buildTypeExtId}/${ide.version}.${ide.build}/${ide.ideType.id}$buildArtifactPath.$extension")
    fun buildUnscrambleUrl(ide: Ide): URL = URL("$baseUrl/$guestAuth/${ide.ideType.buildTypeExtId}/${ide.version}.${ide.build}/$unscramblePath")

    @JvmStatic
    fun main(args: Array<String>) {
        val ext = SystemInfo.getExt()
        val ide = Ide(ideType = IdeType.IDEA_COMMUNITY, version = 171, build = 3085)
        val pathToSave = PathManager.getWorkDirPath()
        val path = "$pathToSave${File.separator}${ide.ideType.id}-${ide.version}.${ide.build}.$ext"

        download(buildUrl(ide = ide, extension = ext), path)
        unpack(path)
        unscramble(ide, pathToSave)
    }
}
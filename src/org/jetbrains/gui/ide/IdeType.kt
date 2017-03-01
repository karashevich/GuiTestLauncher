package org.jetbrains.gui.ide

/**
 * @author Sergey Karashevich
 */
enum class IdeType(val id: String, val buildTypeExtId: String, val platformPrefix: String, val ideJarName: String){
    IDEA_COMMUNITY(id = "IdeaIC", buildTypeExtId = "ijplatform_master_Idea_Installers", platformPrefix = "Idea", ideJarName = "idea.jar"),
    IDEA_ULTIMATE(id = "IdeaIU", buildTypeExtId = "ijplatform_master_Idea_Installers", platformPrefix = "", ideJarName = "idea.jar"),
    WEBSTORM(id = "WebStorm-EAP", buildTypeExtId = "bt3948", platformPrefix = "WebStorm", ideJarName = "webstorm.jar")
}

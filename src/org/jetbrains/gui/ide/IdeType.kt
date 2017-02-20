package org.jetbrains.gui.ide

/**
 * @author Sergey Karashevich
 */
enum class IdeType(val id: String, val buildTypeExtId: String, val platformPrefix: String){
    IDEA_COMMUNITY("IdeaIC", "ijplatform_master_Idea_Installers", "Idea"),
    IDEA_ULTIMATE("IdeaIU", "ijplatform_master_Idea_Installers", "")
}

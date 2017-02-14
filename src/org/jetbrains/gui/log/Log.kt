package org.jetbrains.gui.log

/**
 * @author Sergey Karashevich
 */

object Log {

    fun log(message: String) = println(message)
    fun error(errorMessage: String) = System.err.println(errorMessage)

}
package fr.cdbs.oegen.data.io.xml

import fr.cdbs.oegen.data.io.IoMessages

@Deprecated("Use IoMessages", ReplaceWith("IoMessages"))
object Messages {
    fun getString(key: String): String = IoMessages.getString(key)
}

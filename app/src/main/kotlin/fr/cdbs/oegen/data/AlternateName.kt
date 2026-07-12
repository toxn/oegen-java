package fr.cdbs.oegen.data

import fr.cdbs.oegen.ui.Messages

enum class AlternateNameKind(val messageKey: String) {
    ALIAS("Person.AlternateName.Alias"),
    NICKNAME("Person.AlternateName.Nickname"),
    MAIDEN("Person.AlternateName.Maiden"),
    OTHER("Person.AlternateName.Other"),
}

data class AlternateName(
    var firstName: String? = null,
    var lastName: String? = null,
    var kind: AlternateNameKind = AlternateNameKind.ALIAS,
) {
    fun displayLabel(): String {
        val kindLabel = Messages.getString(kind.messageKey)
        val name = listOfNotNull(lastName?.takeIf { it.isNotBlank() }, firstName?.takeIf { it.isNotBlank() })
            .joinToString(" ")
        return if (name.isBlank()) kindLabel else "$kindLabel: $name"
    }
}

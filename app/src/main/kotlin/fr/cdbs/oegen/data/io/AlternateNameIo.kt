package fr.cdbs.oegen.data.io

import fr.cdbs.oegen.data.AlternateName
import fr.cdbs.oegen.data.AlternateNameKind
import fr.cdbs.oegen.data.io.model.AlternateNameNode

internal object AlternateNameIo {
    fun toNode(name: AlternateName): AlternateNameNode {
        return AlternateNameNode(
            kind = name.kind.name.takeUnless { it == AlternateNameKind.ALIAS.name },
            firstName = name.firstName,
            lastName = name.lastName,
        )
    }

    fun fromNode(node: AlternateNameNode): AlternateName {
        val kind = node.kind?.let { value ->
            AlternateNameKind.entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
        } ?: AlternateNameKind.ALIAS
        return AlternateName(
            firstName = node.firstName,
            lastName = node.lastName,
            kind = kind,
        )
    }
}

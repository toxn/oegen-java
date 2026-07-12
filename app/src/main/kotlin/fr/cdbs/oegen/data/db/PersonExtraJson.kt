package fr.cdbs.oegen.data.db

import fr.cdbs.oegen.data.AlternateName
import fr.cdbs.oegen.data.PersonLifeEvents
import fr.cdbs.oegen.data.io.OegenJsonMapper

object PersonExtraJson {
    data class Payload(
        val lifeEvents: PersonLifeEvents? = null,
        val alternateNames: List<AlternateName>? = null,
    )

    data class PersonExtra(
        val lifeEvents: PersonLifeEvents = PersonLifeEvents(),
        val alternateNames: List<AlternateName> = emptyList(),
    )

    fun encode(extra: PersonExtra): String? {
        val payload = Payload(
            lifeEvents = extra.lifeEvents.takeUnless { it == PersonLifeEvents() },
            alternateNames = extra.alternateNames.takeIf { it.isNotEmpty() },
        )
        if (payload.lifeEvents == null && payload.alternateNames == null) return null
        return OegenJsonMapper.mapper.writeValueAsString(payload)
    }

    fun decode(json: String?): PersonExtra {
        if (json.isNullOrBlank()) return PersonExtra()
        val payload = OegenJsonMapper.mapper.readValue(json, Payload::class.java)
        return PersonExtra(
            lifeEvents = payload.lifeEvents ?: PersonLifeEvents(),
            alternateNames = payload.alternateNames.orEmpty(),
        )
    }
}

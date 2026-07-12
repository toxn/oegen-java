package fr.cdbs.oegen.data.db

import fr.cdbs.oegen.data.PersonLifeEvents
import fr.cdbs.oegen.data.io.OegenJsonMapper

object PersonExtraJson {
    data class Payload(val lifeEvents: PersonLifeEvents? = null)

    fun encode(lifeEvents: PersonLifeEvents): String? {
        if (lifeEvents == PersonLifeEvents()) return null
        return OegenJsonMapper.mapper.writeValueAsString(Payload(lifeEvents))
    }

    fun decode(json: String?): PersonLifeEvents {
        if (json.isNullOrBlank()) return PersonLifeEvents()
        return OegenJsonMapper.mapper.readValue(json, Payload::class.java).lifeEvents ?: PersonLifeEvents()
    }
}

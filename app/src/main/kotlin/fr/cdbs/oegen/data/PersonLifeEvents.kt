package fr.cdbs.oegen.data

import fr.cdbs.oegen.data.date.FuzzyDate
import fr.cdbs.oegen.data.date.LifeEventKind

/**
 * Life-event dates and simple places attached to a [Person].
 */
data class PersonLifeEvents(
    var birth: FuzzyDate? = null,
    var birthPlace: String? = null,
    var baptism: FuzzyDate? = null,
    var baptismPlace: String? = null,
    var marriage: FuzzyDate? = null,
    var marriagePlace: String? = null,
    var death: FuzzyDate? = null,
    var deathPlace: String? = null,
    var funeral: FuzzyDate? = null,
    var funeralPlace: String? = null,
) {
    fun get(kind: LifeEventKind): FuzzyDate? = when (kind) {
        LifeEventKind.BIRTH -> birth
        LifeEventKind.BAPTISM -> baptism
        LifeEventKind.MARRIAGE -> marriage
        LifeEventKind.DEATH -> death
        LifeEventKind.FUNERAL -> funeral
    }

    fun getPlace(kind: LifeEventKind): String? = when (kind) {
        LifeEventKind.BIRTH -> birthPlace
        LifeEventKind.BAPTISM -> baptismPlace
        LifeEventKind.MARRIAGE -> marriagePlace
        LifeEventKind.DEATH -> deathPlace
        LifeEventKind.FUNERAL -> funeralPlace
    }

    fun set(kind: LifeEventKind, value: FuzzyDate?) {
        when (kind) {
            LifeEventKind.BIRTH -> birth = value
            LifeEventKind.BAPTISM -> baptism = value
            LifeEventKind.MARRIAGE -> marriage = value
            LifeEventKind.DEATH -> death = value
            LifeEventKind.FUNERAL -> funeral = value
        }
    }

    fun setPlace(kind: LifeEventKind, value: String?) {
        val normalized = value?.trim()?.takeIf { it.isNotEmpty() }
        when (kind) {
            LifeEventKind.BIRTH -> birthPlace = normalized
            LifeEventKind.BAPTISM -> baptismPlace = normalized
            LifeEventKind.MARRIAGE -> marriagePlace = normalized
            LifeEventKind.DEATH -> deathPlace = normalized
            LifeEventKind.FUNERAL -> funeralPlace = normalized
        }
    }
}

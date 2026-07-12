package fr.cdbs.oegen.data

import fr.cdbs.oegen.data.date.FuzzyDate
import fr.cdbs.oegen.data.date.LifeEventKind

/**
 * Life-event dates attached to a [Person]. Each field is optional and uses the fuzzy date model.
 */
data class PersonLifeEvents(
    var birth: FuzzyDate? = null,
    var baptism: FuzzyDate? = null,
    var marriage: FuzzyDate? = null,
    var death: FuzzyDate? = null,
    var funeral: FuzzyDate? = null,
) {
    fun get(kind: LifeEventKind): FuzzyDate? = when (kind) {
        LifeEventKind.BIRTH -> birth
        LifeEventKind.BAPTISM -> baptism
        LifeEventKind.MARRIAGE -> marriage
        LifeEventKind.DEATH -> death
        LifeEventKind.FUNERAL -> funeral
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
}

package fr.cdbs.oegen.data.date

enum class LifeEventKind(val messageKey: String) {
    BIRTH("Person.LifeEvent.birth"),
    BAPTISM("Person.LifeEvent.baptism"),
    MARRIAGE("Person.LifeEvent.marriage"),
    DEATH("Person.LifeEvent.death"),
    FUNERAL("Person.LifeEvent.funeral"),
}

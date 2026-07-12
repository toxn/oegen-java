package fr.cdbs.oegen.data.io.xml

import fr.cdbs.oegen.data.Person

object XmlConstants {
    const val DB_NAME = "oegendb"
    const val DB_NAMESPACE = "fr.cdbs.oegen"
    const val W3C_INSTANCE_NS = "http://www.w3.org/2001/XMLSchema-instance"
    const val SCHEMA_LOCATION = "fr.cdbs.oegen oegen-kiss.xsd"

    const val PERSON_NAME = Person.CLASSNAME
    const val PERSON_ATTR_RELATION = "relation"
    const val PERSON_FIRSTNAME = Person.PROPERTY_FIRSTNAME
    const val PERSON_LASTNAME = Person.PROPERTY_LASTNAME
    const val PERSON_GENDER = Person.PROPERTY_GENDER
    const val PERSON_ID = Person.PROPERTY_ID

    const val PERSONREF_NAME = "personRef"
    const val PERSONREF_ATTR_REF = "ref"

    const val LISTOF_NAME = "list"
    const val LISTOF_ATTR_OF = "of"

    const val P2PRELTYPE_CHILD = Person.TAG_CHILD
    const val P2PRELTYPE_FATHER = Person.PROPERTY_FATHER
    const val P2PRELTYPE_MOTHER = Person.PROPERTY_MOTHER
    const val P2PRELTYPE_PARENT = "parent"
}

package fr.cdbs.oegen.data

import fr.cdbs.oegen.data.date.FuzzyDate
import fr.cdbs.oegen.data.io.PersonGraphReader
import fr.cdbs.oegen.data.io.PersonGraphWriter
import fr.cdbs.oegen.data.io.xml.OegenXmlMapper
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class PersonLifeEventsTest {
    @AfterTest
    fun tearDown() {
        Person.clearAll()
    }

    @Test
    fun birthDatePropertyUpdatesLifeEvents() {
        val person = Person()
        val birth = FuzzyDate.Precise(1900, 1, 2)
        person.birthDate = birth
        assertEquals(birth, person.lifeEvents.birth)
    }

    @Test
    fun xmlRoundTripPreservesFuzzyBirthDate() {
        val person = Person().apply {
            firstName = "Jean"
            lastName = "Dupont"
            birthDate = FuzzyDate.Text("spring 1840")
            birthPlace = "Paris"
        }

        val exported = ByteArrayOutputStream()
        OegenXmlMapper.write(exported, PersonGraphWriter.exportDatabase(isComplete = true))

        Person.clearAll()
        OegenXmlMapper.read(ByteArrayInputStream(exported.toByteArray())).let(PersonGraphReader::importDocument)

        val restored = Person.persons.single()
        assertEquals(FuzzyDate.Text("spring 1840"), restored.birthDate)
        assertEquals("Paris", restored.birthPlace)
    }

    @Test
    fun birthPlacePropertyUpdatesLifeEvents() {
        val person = Person()
        person.birthPlace = "Lyon"
        assertEquals("Lyon", person.lifeEvents.birthPlace)
    }
}

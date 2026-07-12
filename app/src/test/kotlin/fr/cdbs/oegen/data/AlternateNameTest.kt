package fr.cdbs.oegen.data

import fr.cdbs.oegen.data.io.PersonGraphReader
import fr.cdbs.oegen.data.io.PersonGraphWriter
import fr.cdbs.oegen.data.io.xml.OegenXmlMapper
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AlternateNameTest {
    @AfterTest
    fun tearDown() {
        Person.clearAll()
    }

    @Test
    fun alternateNamesStoredOnPerson() {
        val person = Person().apply {
            alternateNames = listOf(
                AlternateName(firstName = "Jeanne", lastName = "Martin", kind = AlternateNameKind.MAIDEN),
            )
        }
        assertEquals(1, person.alternateNames.size)
        assertEquals("Martin", person.alternateNames[0].lastName)
    }

    @Test
    fun xmlRoundTripPreservesAlternateNames() {
        Person().apply {
            firstName = "Marie"
            lastName = "Dupont"
            alternateNames = listOf(
                AlternateName(firstName = "Mary", kind = AlternateNameKind.NICKNAME),
                AlternateName(lastName = "Martin", kind = AlternateNameKind.MAIDEN),
            )
        }

        val exported = ByteArrayOutputStream()
        OegenXmlMapper.write(exported, PersonGraphWriter.exportDatabase(isComplete = true))

        Person.clearAll()
        OegenXmlMapper.read(ByteArrayInputStream(exported.toByteArray())).let(PersonGraphReader::importDocument)

        val restored = Person.persons.single()
        assertEquals(2, restored.alternateNames.size)
        assertEquals(AlternateNameKind.NICKNAME, restored.alternateNames[0].kind)
        assertEquals("Martin", restored.alternateNames[1].lastName)
    }
}

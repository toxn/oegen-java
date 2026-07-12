package fr.cdbs.oegen.data.io.xml

import fr.cdbs.oegen.data.Person
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class XmlRoundTripTest {
    @AfterTest
    fun tearDown() {
        Person.clearAll()
    }

    @Test
    fun importFamilyFixture() {
        val xml = javaClass.getResourceAsStream("/family.xml")!!
        Importer(xml).doImport()

        assertEquals(2, Person.persons.size)

        val root = Person.persons.first { it.lastName == "Dupont" && it.firstName == "Jean" }
        assertEquals(Person.Gender.Male, root.gender)
        assertEquals(1, root.children.size)
        assertEquals("Marie", root.children[0]!!.firstName)
    }

    @Test
    fun exportAndReimportPreservesPersonCount() {
        Importer(javaClass.getResourceAsStream("/family.xml")!!).doImport()
        val initialCount = Person.persons.size

        val exported = ByteArrayOutputStream()
        Exporter(exported).doExport()

        Person.clearAll()
        Importer(ByteArrayInputStream(exported.toByteArray())).doImport()

        assertEquals(initialCount, Person.persons.size)
        assertTrue(Person.persons.any { it.firstName == "Jean" && it.lastName == "Dupont" })
    }

    @Test
    fun importComplexNestedFamily() {
        Importer(javaClass.getResourceAsStream("/oegen.xml")!!).doImport()

        assertTrue(Person.persons.size >= 10)
        val root = Person.persons.first { it.firstName == "Old'pa" && it.lastName == "Oiseau" }
        assertTrue(root.children.size > 0)
    }
}

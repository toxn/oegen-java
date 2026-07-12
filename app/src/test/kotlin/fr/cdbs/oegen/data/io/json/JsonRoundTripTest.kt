package fr.cdbs.oegen.data.io.json

import fr.cdbs.oegen.data.Person
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JsonRoundTripTest {
    @AfterTest
    fun tearDown() {
        Person.clearAll()
    }

    @Test
    fun importFamilyFixture() {
        Importer(javaClass.getResourceAsStream("/family.json")).doImport()
        assertEquals(2, Person.persons.size)
        val root = Person.persons.first { it.lastName == "Dupont" && it.firstName == "Jean" }
        assertEquals(Person.Gender.Male, root.gender)
        assertEquals(1, root.children.size)
        assertEquals("Marie", root.children[0]!!.firstName)
    }

    @Test
    fun exportAndReimportPreservesPersonCount() {
        Importer(javaClass.getResourceAsStream("/family.json")).doImport()
        val initialCount = Person.persons.size
        val exported = ByteArrayOutputStream()
        Exporter(exported).doExport()
        Person.clearAll()
        Importer(ByteArrayInputStream(exported.toByteArray())).doImport()
        assertEquals(initialCount, Person.persons.size)
    }
}

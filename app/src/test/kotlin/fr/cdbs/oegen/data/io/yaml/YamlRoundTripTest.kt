package fr.cdbs.oegen.data.io.yaml

import fr.cdbs.oegen.data.Person
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class YamlRoundTripTest {
    @AfterTest
    fun tearDown() {
        Person.clearAll()
    }

    @Test
    fun importFamilyFixture() {
        Importer(javaClass.getResourceAsStream("/family.yaml")).doImport()
        assertEquals(2, Person.persons.size)
    }

    @Test
    fun exportAndReimportPreservesPersonCount() {
        Importer(javaClass.getResourceAsStream("/family.yaml")).doImport()
        val initialCount = Person.persons.size
        val exported = ByteArrayOutputStream()
        Exporter(exported).doExport()
        Person.clearAll()
        Importer(ByteArrayInputStream(exported.toByteArray())).doImport()
        assertEquals(initialCount, Person.persons.size)
    }
}

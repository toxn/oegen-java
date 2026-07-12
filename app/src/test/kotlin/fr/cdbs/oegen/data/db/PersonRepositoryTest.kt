package fr.cdbs.oegen.data.db

import fr.cdbs.oegen.data.Person
import fr.cdbs.oegen.ui.toList
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class PersonRepositoryTest {
    @AfterTest
    fun tearDown() {
        DatabaseService.close()
        Person.clearAll()
    }

    @Test
    fun saveAndLoadPersonsWithParentLinks() {
        DatabaseService.initialize(DbConfig.inMemorySqlite())

        val father = Person()
        father.firstName = "Jean"
        father.lastName = "Dupont"
        father.gender = Person.Gender.Male

        val mother = Person()
        mother.firstName = "Anne"
        mother.lastName = "Dupont"
        mother.gender = Person.Gender.Female

        val child = Person()
        child.firstName = "Marie"
        child.lastName = "Dupont"
        child.father = father
        child.mother = mother

        DatabaseService.saveFromMemory()

        val fatherId = father.id
        val motherId = mother.id
        val childId = child.id

        Person.clearAll()
        DatabaseService.loadIntoMemory()

        assertEquals(3, Person.persons.size)
        val loadedChild = Person.indexId[childId!!]
        assertNotNull(loadedChild)
        assertEquals("Marie", loadedChild.firstName)
        assertEquals(fatherId, loadedChild.father?.id)
        assertEquals(motherId, loadedChild.mother?.id)
        assertEquals(1, Person.indexId[fatherId!!]!!.children.size)
    }

    @Test
    fun clearDatabaseRemovesAllRows() {
        DatabaseService.initialize(DbConfig.inMemorySqlite())
        Person().firstName = "Solo"
        DatabaseService.saveFromMemory()
        DatabaseService.clearDatabase()
        assertEquals(0, Person.persons.size)
        DatabaseService.loadIntoMemory()
        assertEquals(0, Person.persons.size)
    }

    @Test
    fun emptyDatabaseLoadLeavesMemoryEmpty() {
        DatabaseService.initialize(DbConfig.inMemorySqlite())
        DatabaseService.loadIntoMemory()
        assertEquals(0, Person.persons.size)
    }
}

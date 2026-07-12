package fr.cdbs.oegen.data

import fr.cdbs.oegen.ui.PersonList
import fr.cdbs.oegen.ui.searchByName
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PersonTest {
    @AfterTest
    fun tearDown() {
        Person.clearAll()
    }

    @Test
    fun clearAllRemovesPersonsAndIndex() {
        Person()
        Person()
        assertEquals(2, Person.persons.size)
        assertEquals(2, Person.indexId.size)

        Person.clearAll()

        assertEquals(0, Person.persons.size)
        assertEquals(0, Person.indexId.size)
    }

    @Test
    fun searchByNameMatchesFirstOrLastName() {
        val jean = Person().apply {
            firstName = "Jean"
            lastName = "Dupont"
        }
        Person().apply {
            firstName = "Marie"
            lastName = "Martin"
        }

        val matches = Person.persons.searchByName("dup")
        assertEquals(1, matches.size)
        assertEquals(jean, matches[0])
    }

    @Test
    fun searchByIdUsesIndex() {
        val person = Person().apply { id = "P42" }
        assertEquals(person, Person.persons.search("P42"))
        assertEquals(null, Person.persons.search("missing"))
    }
}

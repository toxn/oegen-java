package fr.cdbs.oegen.data

import fr.cdbs.oegen.ui.PersonList
import fr.cdbs.oegen.ui.PersonSortOrder
import fr.cdbs.oegen.ui.searchByName
import fr.cdbs.oegen.ui.sortedByPersonOrder
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
    fun searchByNameMatchesMultiTokenQuery() {
        val jean = Person().apply {
            firstName = "Jean"
            lastName = "Dupont"
        }
        Person().apply {
            firstName = "Marie"
            lastName = "Martin"
        }

        val matches = Person.persons.searchByName("Jean Dupont")
        assertEquals(1, matches.size)
        assertEquals(jean, matches[0])
    }

    @Test
    fun sortedByPersonOrderSortsByLastNameThenFirstName() {
        Person().apply {
            firstName = "Zoé"
            lastName = "Martin"
        }
        Person().apply {
            firstName = "Alice"
            lastName = "Dupont"
        }

        val sorted = Person.persons.toList().sortedByPersonOrder(PersonSortOrder.LAST_FIRST)
        assertEquals("Dupont", sorted[0].lastName)
        assertEquals("Martin", sorted[1].lastName)
    }

    @Test
    fun searchByIdUsesIndex() {
        val person = Person().apply { id = "P42" }
        assertEquals(person, Person.persons.search("P42"))
        assertEquals(null, Person.persons.search("missing"))
    }
}

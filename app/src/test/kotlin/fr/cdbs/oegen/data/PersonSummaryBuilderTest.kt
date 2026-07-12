package fr.cdbs.oegen.data

import fr.cdbs.oegen.data.date.FuzzyDate
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class PersonSummaryBuilderTest {
    @AfterTest
    fun tearDown() {
        Person.clearAll()
    }

    @Test
    fun marriageGroupsChildrenByOtherParent() {
        val father = Person().apply {
            firstName = "Jean"
            lastName = "Dupont"
            gender = Person.Gender.Male
        }
        val mother = Person().apply {
            firstName = "Marie"
            lastName = "Martin"
            gender = Person.Gender.Female
        }
        Person().apply {
            firstName = "Paul"
            lastName = "Dupont"
            this.father = father
            this.mother = mother
        }

        val groups = PersonSummaryBuilder.marriageGroups(father)
        assertEquals(1, groups.size)
        assertEquals(mother, groups[0].spouse)
        assertEquals(1, groups[0].children.size)
    }

    @Test
    fun ancestorGenerationsWalksParents() {
        val grandpa = Person().apply { firstName = "GP" }
        val parent = Person().apply {
            firstName = "P"
            father = grandpa
        }
        val child = Person().apply {
            firstName = "C"
            father = parent
        }

        val generations = PersonSummaryBuilder.ancestorGenerations(child, 2)
        assertEquals(2, generations.size)
        assertEquals(parent, generations[0].single())
        assertEquals(grandpa, generations[1].single())
    }
}

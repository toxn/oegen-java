package fr.cdbs.oegen.ui.compose

import fr.cdbs.oegen.data.Person
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PersonTreeLayoutTest {
    @AfterTest
    fun tearDown() {
        Person.clearAll()
    }

    @Test
    fun layoutIncludesCenterParentsAndChildren() {
        val jean = Person().apply {
            firstName = "Jean"
            lastName = "Dupont"
        }
        val marie = Person().apply {
            firstName = "Marie"
            lastName = "Dupont"
        }
        marie.father = jean

        val layout = PersonTreeLayoutEngine.compute(
            center = jean,
            generations = 4,
            metrics = TreeMetrics(
                boxWidth = 180f,
                boxHeight = 40f,
                boxSpace = 20f,
                marriageSpace = 20f,
            ),
        )

        val persons = layout.boxes.map { it.person }.toSet()
        assertTrue(jean in persons)
        assertTrue(marie in persons)
        assertTrue(layout.width > 0f)
        assertTrue(layout.height > 0f)
        assertEquals(2, layout.boxes.size)
    }
}

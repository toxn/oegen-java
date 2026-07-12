package fr.cdbs.oegen.data.date

import kotlin.test.Test
import kotlin.test.assertEquals

class FuzzyDatePartialIsoTest {
    @Test
    fun parsePartialIsoDateComponents() {
        val date = FuzzyDateParser.parse("1850-03-??")
        assertEquals(
            FuzzyDate.Partial(year = "1850", month = "03", day = "??"),
            date,
        )
    }

    @Test
    fun parseFrenchRepublicanTextDate() {
        val date = FuzzyDateParser.parse("12 frimaire an II")
        assertEquals(
            FuzzyDate.Text("12 frimaire an II", CalendarSystem.FRENCH_REPUBLICAN),
            date,
        )
    }

    @Test
    fun parseFrenchRepublicanWithExplicitCalendarSuffix() {
        val date = FuzzyDateParser.parse("an VIII [FRENCH_REPUBLICAN]")
        assertEquals(
            FuzzyDate.Text("an VIII", CalendarSystem.FRENCH_REPUBLICAN),
            date,
        )
    }
}

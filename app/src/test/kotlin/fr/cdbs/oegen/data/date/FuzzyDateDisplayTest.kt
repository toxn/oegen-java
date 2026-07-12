package fr.cdbs.oegen.data.date

import kotlin.test.Test
import kotlin.test.assertEquals

class FuzzyDateDisplayTest {
    @Test
    fun displayPreciseAndPartial() {
        assertEquals(
            "~1850",
            FuzzyDateDisplay.format(FuzzyDate.Precise(1850, precision = DatePrecision.CIRCA)),
        )
        assertEquals(
            "18??",
            FuzzyDateDisplay.format(FuzzyDate.Partial(year = "18??")),
        )
        assertEquals(
            "12 frimaire an II [Republican]",
            FuzzyDateDisplay.format(FuzzyDate.Text("12 frimaire an II", CalendarSystem.FRENCH_REPUBLICAN)),
        )
    }
}

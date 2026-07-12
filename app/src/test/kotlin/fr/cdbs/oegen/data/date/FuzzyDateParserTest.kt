package fr.cdbs.oegen.data.date

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class FuzzyDateParserTest {
    @Test
    fun parsePreciseIso() {
        val date = FuzzyDateParser.parse("1850-03-15")
        assertEquals(
            FuzzyDate.Precise(1850, 3, 15, DatePrecision.EXACT, CalendarSystem.GREGORIAN),
            date,
        )
    }

    @Test
    fun parseCircaYear() {
        val date = FuzzyDateParser.parse("c. 1842")
        assertEquals(FuzzyDate.Precise(1842, precision = DatePrecision.CIRCA), date)
    }

    @Test
    fun parsePartialYear() {
        val date = FuzzyDateParser.parse("18??")
        assertEquals(FuzzyDate.Partial(year = "18??"), date)
    }

    @Test
    fun parseTextFallback() {
        val date = FuzzyDateParser.parse("during the war")
        assertEquals(FuzzyDate.Text("during the war"), date)
    }

    @Test
    fun parseCalendarSuffix() {
        val date = FuzzyDateParser.parse("1850 [JULIAN]")
        assertEquals(FuzzyDate.Precise(1850, precision = DatePrecision.EXACT, calendar = CalendarSystem.JULIAN), date)
    }

    @Test
    fun parseBooleanComposition() {
        val date = FuzzyDateParser.parse("1850 OR 1851 AND not 1852")
        assertIs<FuzzyDate.Or>(date)
        val or = date as FuzzyDate.Or
        assertEquals(2, or.operands.size)
        assertIs<FuzzyDate.Precise>(or.operands[0])
        assertIs<FuzzyDate.And>(or.operands[1])
    }
}

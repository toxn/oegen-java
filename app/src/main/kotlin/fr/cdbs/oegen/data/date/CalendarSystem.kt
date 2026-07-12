package fr.cdbs.oegen.data.date

/**
 * Calendar used to interpret a [FuzzyDate]. Conversion between calendars is out of scope for now.
 */
enum class CalendarSystem {
    GREGORIAN,
    JULIAN,
    HEBREW,
    ISLAMIC,
    FRENCH_REPUBLICAN,
}

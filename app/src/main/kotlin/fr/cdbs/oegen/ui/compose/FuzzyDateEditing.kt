package fr.cdbs.oegen.ui.compose

import fr.cdbs.oegen.data.date.FuzzyDate
import fr.cdbs.oegen.data.date.FuzzyDateDisplay
import fr.cdbs.oegen.data.date.FuzzyDateParser

fun FuzzyDate?.toEditableText(): String = this?.let { FuzzyDateDisplay.format(it) }.orEmpty()

fun parseEditableDate(input: String): FuzzyDate? {
    val trimmed = input.trim()
    if (trimmed.isEmpty()) return null
    return FuzzyDateParser.parse(trimmed)
}

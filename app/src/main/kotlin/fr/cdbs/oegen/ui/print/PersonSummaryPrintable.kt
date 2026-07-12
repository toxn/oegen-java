package fr.cdbs.oegen.ui.print

import fr.cdbs.oegen.data.Person
import fr.cdbs.oegen.data.PersonSummaryBuilder
import fr.cdbs.oegen.data.date.FuzzyDateDisplay
import fr.cdbs.oegen.data.date.LifeEventKind
import fr.cdbs.oegen.ui.Messages
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.print.PageFormat
import java.awt.print.Printable
import java.awt.print.PrinterException

class PersonSummaryPrintable(
    private val person: Person,
    private val summaryGenerations: Int,
) : Printable {
    private val lines: List<String> by lazy { buildLines() }

    override fun print(graphics: Graphics, pageFormat: PageFormat, pageIndex: Int): Int {
        if (pageIndex > 0) return Printable.NO_SUCH_PAGE

        val g2 = graphics as Graphics2D
        g2.translate(pageFormat.imageableX, pageFormat.imageableY)

        var y = 0
        val lineHeight = g2.fontMetrics.height + 4
        g2.font = Font(Font.SANS_SERIF, Font.BOLD, 16)
        g2.drawString(formatHeading(person), 0, y + g2.fontMetrics.ascent)
        y += lineHeight + 8

        g2.font = Font(Font.SANS_SERIF, Font.PLAIN, 12)
        for (line in lines) {
            if (y + lineHeight > pageFormat.imageableHeight) break
            g2.drawString(line, 0, y + g2.fontMetrics.ascent)
            y += lineHeight
        }

        return Printable.PAGE_EXISTS
    }

    private fun buildLines(): List<String> {
        val result = mutableListOf<String>()

        LifeEventKind.entries.forEach { kind ->
            val date = person.lifeEvents.get(kind)
            val place = person.lifeEvents.getPlace(kind)
            if (date == null && place.isNullOrBlank()) return@forEach
            val label = Messages.getString(kind.messageKey)
            val details = buildList {
                date?.let { add(FuzzyDateDisplay.format(it)) }
                place?.takeIf { it.isNotBlank() }?.let { add(it) }
            }.joinToString(" — ")
            result += "$label: $details"
        }

        result += ""
        result += Messages.getString("Person.Relations.Parents")
        person.father?.let { result += "  • ${formatHeading(it)}" }
        person.mother?.let { result += "  • ${formatHeading(it)}" }

        PersonSummaryBuilder.ancestorGenerations(person, summaryGenerations).forEachIndexed { index, generation ->
            result += ""
            result += Messages.getString("Person.Summary.AncestorsGeneration", index + 1)
            generation.forEach { ancestor -> result += "  • ${formatHeading(ancestor)}" }
        }

        val marriages = PersonSummaryBuilder.marriageGroups(person)
        if (marriages.isNotEmpty()) {
            result += ""
            result += Messages.getString("Person.Summary.Marriages")
            marriages.forEach { group ->
                val spouseLabel = group.spouse?.let { formatHeading(it) }
                    ?: Messages.getString("Person.Summary.UnknownSpouse")
                result += "  • $spouseLabel"
                group.children.forEach { child -> result += "    - ${formatHeading(child)}" }
            }
        } else {
            result += ""
            result += Messages.getString("Person.children")
            person.children.filterNotNull().forEach { child -> result += "  • ${formatHeading(child)}" }
        }

        return result
    }

    private fun formatHeading(person: Person): String {
        return "${person.gender?.symbol.orEmpty()} ${person.lastName.orEmpty()} ${person.firstName.orEmpty()}".trim()
    }
}

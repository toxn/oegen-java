package fr.cdbs.oegen.data.date

import fr.cdbs.oegen.ui.Messages
import java.text.MessageFormat

object FuzzyDateDisplay {
    fun format(date: FuzzyDate): String = when (date) {
        is FuzzyDate.Text -> appendCalendar(date.text, date.calendar)
        is FuzzyDate.Partial -> formatPartial(date)
        is FuzzyDate.Precise -> formatPrecise(date)
        is FuzzyDate.And -> date.operands.joinToString(Messages.getString("FuzzyDate.separator.and")) {
            format(it)
        }
        is FuzzyDate.Or -> date.operands.joinToString(Messages.getString("FuzzyDate.separator.or")) {
            format(it)
        }
        is FuzzyDate.Not -> MessageFormat.format(
            Messages.getString("FuzzyDate.not"),
            format(date.operand),
        )
    }

    private fun formatPartial(date: FuzzyDate.Partial): String {
        val y = date.year.orEmpty()
        val m = date.month.orEmpty()
        val d = date.day.orEmpty()
        val core = when {
            y.isNotEmpty() && m.isNotEmpty() && d.isNotEmpty() -> "$y-$m-$d"
            y.isNotEmpty() && m.isNotEmpty() -> "$y-$m"
            y.isNotEmpty() -> y
            else -> listOfNotNull(date.year, date.month, date.day).joinToString("-")
        }
        return appendCalendar(core, date.calendar)
    }

    private fun formatPrecise(date: FuzzyDate.Precise): String {
        val core = buildString {
            append(precisionPrefix(date.precision))
            when {
                date.day != null && date.month != null ->
                    append(String.format("%04d-%02d-%02d", date.year, date.month, date.day))
                date.month != null -> append(String.format("%04d-%02d", date.year, date.month))
                else -> append(date.year)
            }
        }
        return appendCalendar(core, date.calendar)
    }

    private fun precisionPrefix(precision: DatePrecision): String = when (precision) {
        DatePrecision.EXACT -> ""
        DatePrecision.BEFORE -> Messages.getString("FuzzyDate.precision.before")
        DatePrecision.AFTER -> Messages.getString("FuzzyDate.precision.after")
        DatePrecision.CIRCA -> Messages.getString("FuzzyDate.precision.circa")
    }

    private fun appendCalendar(text: String, calendar: CalendarSystem): String {
        if (calendar == CalendarSystem.GREGORIAN) return text
        val key = "FuzzyDate.calendar.${calendar.name}"
        val label = runCatching { Messages.getString(key) }.getOrElse { calendar.name }
        return "$text [$label]"
    }
}

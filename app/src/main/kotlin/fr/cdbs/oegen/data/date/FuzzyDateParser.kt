package fr.cdbs.oegen.data.date

object FuzzyDateParser {
    private val calendarSuffix = Regex("""\s*\[([A-Z_]+)]\s*$""", RegexOption.IGNORE_CASE)
    private val isoPartial = Regex("""^[\d?]+-[\d?]+-[\d?]+$""")
    private val isoPrecise = Regex("""^(\d{4})(?:-(\d{1,2})(?:-(\d{1,2}))?)?$""")
    private val slashPartial = Regex("""^[\d?]+/[\d?]+/[\d?]+$""")
    private val slashPrecise = Regex("""^(\d{1,2})/(\d{1,2})/(\d{4})$""")
    private val yearOnlyPartial = Regex("""^(\d|\?)+$""")

    fun parse(input: String, defaultCalendar: CalendarSystem = CalendarSystem.GREGORIAN): FuzzyDate {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) {
            return FuzzyDate.Text("", defaultCalendar)
        }

        val (body, calendar) = extractCalendar(trimmed, defaultCalendar)
        return parseComposition(body, calendar)
    }

    private fun parseComposition(body: String, calendar: CalendarSystem): FuzzyDate {
        val notMatch = Regex("""^(?:not|!)\s+(.+)$""", RegexOption.IGNORE_CASE).matchEntire(body)
        if (notMatch != null) {
            return FuzzyDate.Not(parseComposition(notMatch.groupValues[1].trim(), calendar), calendar)
        }

        splitTopLevel(body, " OR ", ignoreCase = true)?.let { parts ->
            return FuzzyDate.Or(parts.map { parseComposition(it.trim(), calendar) }, calendar)
        }

        splitTopLevel(body, " AND ", ignoreCase = true)?.let { parts ->
            return FuzzyDate.And(parts.map { parseComposition(it.trim(), calendar) }, calendar)
        }

        return parseSingle(body, calendar)
    }

    private fun parseSingle(body: String, calendar: CalendarSystem): FuzzyDate {
        var rest = body.trim()
        var precision = DatePrecision.EXACT

        when {
            rest.startsWith("<", ignoreCase = false) -> {
                precision = DatePrecision.BEFORE
                rest = rest.drop(1).trim()
            }
            Regex("""^(?:bef\.?|before)\s+""", RegexOption.IGNORE_CASE).containsMatchIn(rest) -> {
                precision = DatePrecision.BEFORE
                rest = rest.replaceFirst(Regex("""^(?:bef\.?|before)\s+""", RegexOption.IGNORE_CASE), "")
            }
            rest.startsWith(">", ignoreCase = false) -> {
                precision = DatePrecision.AFTER
                rest = rest.drop(1).trim()
            }
            Regex("""^(?:aft\.?|after)\s+""", RegexOption.IGNORE_CASE).containsMatchIn(rest) -> {
                precision = DatePrecision.AFTER
                rest = rest.replaceFirst(Regex("""^(?:aft\.?|after)\s+""", RegexOption.IGNORE_CASE), "")
            }
            Regex("""^(?:c\.|circa|ca\.?|~)\s*""", RegexOption.IGNORE_CASE).containsMatchIn(rest) -> {
                precision = DatePrecision.CIRCA
                rest = rest.replaceFirst(Regex("""^(?:c\.|circa|ca\.?|~)\s*""", RegexOption.IGNORE_CASE), "")
            }
        }

        rest = rest.trim()

        if (rest.contains('?')) {
            isoPartial.matchEntire(rest)?.let { match ->
                return FuzzyDate.Partial(match.groupValues[1], match.groupValues[2], match.groupValues[3], calendar)
            }
            slashPartial.matchEntire(rest)?.let { match ->
                return FuzzyDate.Partial(match.groupValues[3], match.groupValues[2], match.groupValues[1], calendar)
            }
            if (yearOnlyPartial.matches(rest)) {
                return FuzzyDate.Partial(year = rest, calendar = calendar)
            }
        }

        isoPrecise.matchEntire(rest)?.let { match ->
            return FuzzyDate.Precise(
                year = match.groupValues[1].toInt(),
                month = match.groupValues.getOrNull(2)?.takeIf { it.isNotEmpty() }?.toInt(),
                day = match.groupValues.getOrNull(3)?.takeIf { it.isNotEmpty() }?.toInt(),
                precision = precision,
                calendar = calendar,
            )
        }
        slashPrecise.matchEntire(rest)?.let {
            val (d, m, y) = it.destructured
            return FuzzyDate.Precise(
                year = y.toInt(),
                month = m.toInt(),
                day = d.toInt(),
                precision = precision,
                calendar = calendar,
            )
        }

        if (precision != DatePrecision.EXACT) {
            return FuzzyDate.Text(body, calendar)
        }

        return FuzzyDate.Text(body, calendar)
    }

    private fun extractCalendar(input: String, defaultCalendar: CalendarSystem): Pair<String, CalendarSystem> {
        val match = calendarSuffix.find(input) ?: return input to defaultCalendar
        val name = match.groupValues[1].uppercase()
        val calendar = runCatching { CalendarSystem.valueOf(name) }.getOrDefault(defaultCalendar)
        return input.removeRange(match.range).trim() to calendar
    }

    private fun splitTopLevel(input: String, separator: String, ignoreCase: Boolean): List<String>? {
        val sep = separator.trim()
        val flags = if (ignoreCase) RegexOption.IGNORE_CASE else RegexOption.IGNORE_CASE
        var depth = 0
        var i = 0
        val parts = mutableListOf<String>()
        var start = 0
        while (i <= input.length - sep.length) {
            when (input[i]) {
                '(' -> depth++
                ')' -> if (depth > 0) depth--
            }
            if (depth == 0 && input.regionMatches(i, sep, 0, sep.length, ignoreCase)) {
                parts += input.substring(start, i)
                i += sep.length
                start = i
                continue
            }
            i++
        }
        if (parts.isEmpty()) return null
        parts += input.substring(start)
        return parts
    }
}

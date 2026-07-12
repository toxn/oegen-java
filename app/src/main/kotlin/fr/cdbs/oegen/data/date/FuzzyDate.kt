package fr.cdbs.oegen.data.date

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "kind")
@JsonSubTypes(
    JsonSubTypes.Type(value = FuzzyDate.Text::class, name = "text"),
    JsonSubTypes.Type(value = FuzzyDate.Partial::class, name = "partial"),
    JsonSubTypes.Type(value = FuzzyDate.Precise::class, name = "precise"),
    JsonSubTypes.Type(value = FuzzyDate.And::class, name = "and"),
    JsonSubTypes.Type(value = FuzzyDate.Or::class, name = "or"),
    JsonSubTypes.Type(value = FuzzyDate.Not::class, name = "not"),
)
sealed interface FuzzyDate {
    val calendar: CalendarSystem

    data class Text(
        val text: String,
        override val calendar: CalendarSystem = CalendarSystem.GREGORIAN,
    ) : FuzzyDate

    /**
     * Year, month and day as digit strings; `?` marks unknown digits (e.g. `18??`, `03`, `1?`).
     */
    data class Partial(
        val year: String? = null,
        val month: String? = null,
        val day: String? = null,
        override val calendar: CalendarSystem = CalendarSystem.GREGORIAN,
    ) : FuzzyDate

    data class Precise(
        val year: Int,
        val month: Int? = null,
        val day: Int? = null,
        val precision: DatePrecision = DatePrecision.EXACT,
        override val calendar: CalendarSystem = CalendarSystem.GREGORIAN,
    ) : FuzzyDate

    data class And(
        val operands: List<FuzzyDate>,
        override val calendar: CalendarSystem = CalendarSystem.GREGORIAN,
    ) : FuzzyDate {
        init {
            require(operands.isNotEmpty()) { "AND requires at least one operand" }
        }
    }

    data class Or(
        val operands: List<FuzzyDate>,
        override val calendar: CalendarSystem = CalendarSystem.GREGORIAN,
    ) : FuzzyDate {
        init {
            require(operands.isNotEmpty()) { "OR requires at least one operand" }
        }
    }

    data class Not(
        val operand: FuzzyDate,
        override val calendar: CalendarSystem = CalendarSystem.GREGORIAN,
    ) : FuzzyDate
}

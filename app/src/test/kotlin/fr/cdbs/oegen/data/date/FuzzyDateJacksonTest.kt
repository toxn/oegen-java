package fr.cdbs.oegen.data.date

import com.fasterxml.jackson.module.kotlin.readValue
import fr.cdbs.oegen.data.io.OegenJsonMapper
import fr.cdbs.oegen.data.io.model.OegenDbDocument
import fr.cdbs.oegen.data.io.model.PersonNode
import kotlin.test.Test
import kotlin.test.assertEquals

class FuzzyDateJacksonTest {
    @Test
    fun roundTripFuzzyDateInDocument() {
        val document = OegenDbDocument(
            persons = listOf(
                PersonNode(
                    firstName = "Ada",
                    lastName = "Lovelace",
                    birth = FuzzyDate.Precise(1815, 12, 10, DatePrecision.EXACT),
                    death = FuzzyDate.And(
                        listOf(
                            FuzzyDate.Precise(1852, 11, 27, DatePrecision.AFTER),
                            FuzzyDate.Text("London"),
                        ),
                    ),
                ),
            ),
        )

        val json = OegenJsonMapper.mapper.writeValueAsString(document)
        val restored = OegenJsonMapper.mapper.readValue<OegenDbDocument>(json)
        assertEquals(document, restored)
    }
}

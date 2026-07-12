package fr.cdbs.oegen.data.io

import com.fasterxml.jackson.databind.ObjectMapper
import fr.cdbs.oegen.data.io.json.Importer as JsonImporter
import fr.cdbs.oegen.data.io.yaml.Importer as YamlImporter
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class OegenSchemaValidationTest {
    @Test
    fun rejectsJsonWithoutPersonsArray() {
        val json = """{"schemaLocation":"x"}"""
        val ex = assertFailsWith<SchemaValidationException> {
            JsonImporter(json.byteInputStream()).doImport()
        }
        assertTrue(ex.details.isNotEmpty())
    }

    @Test
    fun rejectsYamlWithInvalidGenderEnum() {
        val yaml = """
            persons:
              - firstName: Ada
                gender: Robot
        """.trimIndent()
        assertFailsWith<SchemaValidationException> {
            YamlImporter(yaml.byteInputStream()).doImport()
        }
    }

    @Test
    fun acceptsCanonicalJsonDocument() {
        val tree = ObjectMapper().readTree(
            OegenSchemaValidationTest::class.java.getResourceAsStream("/family.json"),
        )
        OegenSchemaValidator.validate(tree)
    }
}

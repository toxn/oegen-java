package fr.cdbs.oegen.data.io

import com.fasterxml.jackson.databind.JsonNode
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import com.networknt.schema.ValidationMessage
import java.text.MessageFormat

import fr.cdbs.oegen.data.io.model.OegenDbDocument

object OegenSchemaValidator {
    private val schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012)
    private val schema by lazy {
        val stream = OegenSchemaValidator::class.java.getResourceAsStream("/oegen-schema.json")
            ?: error("Missing resource oegen-schema.json")
        schemaFactory.getSchema(stream)
    }

    fun validate(tree: JsonNode) {
        val messages: Set<ValidationMessage> = schema.validate(tree)
        if (messages.isEmpty()) {
            return
        }
        val details = messages.map { it.message }.sorted()
        val summary = IoMessages.getString("SchemaValidation.failed")
        val detailLine = MessageFormat.format(
            IoMessages.getString("SchemaValidation.detail"),
            details.joinToString("; "),
        )
        throw SchemaValidationException("$summary $detailLine", details)
    }

    fun validateDocument(document: OegenDbDocument) {
        val tree = OegenJsonMapper.mapper.valueToTree<JsonNode>(document)
        validate(tree)
    }
}

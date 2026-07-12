package fr.cdbs.oegen.data.io

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.kotlinModule
import fr.cdbs.oegen.data.io.model.OegenDbDocument
import java.io.InputStream
import java.io.OutputStream

object OegenJsonMapper {
    val mapper: ObjectMapper = ObjectMapper()
        .registerModule(kotlinModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(SerializationFeature.INDENT_OUTPUT, true)
        .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)

    fun read(inputStream: InputStream): OegenDbDocument {
        return mapper.readValue(inputStream, OegenDbDocument::class.java)
    }

    fun write(outputStream: OutputStream, document: OegenDbDocument) {
        mapper.writeValue(outputStream, document)
    }
}

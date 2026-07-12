package fr.cdbs.oegen.data.io.xml

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator
import com.fasterxml.jackson.module.kotlin.kotlinModule
import fr.cdbs.oegen.data.io.xml.model.OegenDbDocument
import java.io.InputStream
import java.io.OutputStream

object OegenXmlMapper {
    private val mapper: XmlMapper = XmlMapper.builder()
        .defaultUseWrapper(false)
        .addModule(kotlinModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true)
        .configure(SerializationFeature.INDENT_OUTPUT, true)
        .serializationInclusion(JsonInclude.Include.NON_EMPTY)
        .build()

    fun read(inputStream: InputStream): OegenDbDocument {
        return mapper.readValue(inputStream, OegenDbDocument::class.java)
    }

    fun write(outputStream: OutputStream, document: OegenDbDocument) {
        mapper.writeValue(outputStream, document)
    }
}

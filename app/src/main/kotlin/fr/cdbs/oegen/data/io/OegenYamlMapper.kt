package fr.cdbs.oegen.data.io

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import fr.cdbs.oegen.data.io.model.OegenDbDocument
import java.io.InputStream
import java.io.OutputStream

object OegenYamlMapper {
    val mapper: YAMLMapper = YAMLMapper.builder()
        .addModule(kotlinModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(SerializationFeature.INDENT_OUTPUT, true)
        .configure(YAMLGenerator.Feature.MINIMIZE_QUOTES, true)
        .serializationInclusion(JsonInclude.Include.NON_EMPTY)
        .build()

    fun read(inputStream: InputStream): OegenDbDocument {
        return mapper.readValue(inputStream, OegenDbDocument::class.java)
    }

    fun write(outputStream: OutputStream, document: OegenDbDocument) {
        mapper.writeValue(outputStream, document)
    }
}

package fr.cdbs.oegen.data.io.yaml

import fr.cdbs.oegen.data.io.OegenSchemaValidator
import fr.cdbs.oegen.data.io.OegenYamlMapper
import fr.cdbs.oegen.data.io.PersonGraphReader
import java.io.InputStream

class Importer(inputStream: InputStream?) : fr.cdbs.oegen.data.io.Importer(inputStream) {
    override fun doImport() {
        requireNotNull(inputStream) { "Input stream must not be null" }
        inputStream.use { stream ->
            val tree = OegenYamlMapper.mapper.readTree(stream)
            OegenSchemaValidator.validate(tree)
            val document = OegenYamlMapper.mapper.treeToValue(tree, fr.cdbs.oegen.data.io.model.OegenDbDocument::class.java)
            PersonGraphReader.importDocument(document)
        }
    }
}

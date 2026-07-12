package fr.cdbs.oegen.data.io.xml

import fr.cdbs.oegen.data.io.OegenSchemaValidator
import fr.cdbs.oegen.data.io.PersonGraphReader
import java.io.InputStream

class Importer(inputStream: InputStream?) : fr.cdbs.oegen.data.io.Importer(inputStream) {
    override fun doImport() {
        requireNotNull(inputStream) { "Input stream must not be null" }
        inputStream.use { stream ->
            val document = OegenXmlMapper.read(stream)
            OegenSchemaValidator.validateDocument(document)
            PersonGraphReader.importDocument(document)
        }
    }
}

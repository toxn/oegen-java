package fr.cdbs.oegen.data.io.xml

import fr.cdbs.oegen.data.io.xml.model.OegenDbDocument
import java.io.InputStream

class Importer(inputStream: InputStream?) : fr.cdbs.oegen.data.io.Importer(inputStream) {
    override fun doImport() {
        requireNotNull(inputStream) { "Input stream must not be null" }
        inputStream.use { stream ->
            val document = OegenXmlMapper.read(stream)
            PersonXmlReader.importDocument(document)
        }
    }
}

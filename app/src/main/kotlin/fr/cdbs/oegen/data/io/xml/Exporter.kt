package fr.cdbs.oegen.data.io.xml

import fr.cdbs.oegen.data.io.IoMessages
import fr.cdbs.oegen.data.io.PersonGraphWriter
import java.io.OutputStream

class Exporter(outputStream: OutputStream?) : fr.cdbs.oegen.data.io.Exporter(outputStream) {
    override fun doExport() {
        requireNotNull(outputStream) { "Output stream must not be null" }
        try {
            val document = PersonGraphWriter.exportDatabase(isComplete)
            outputStream.use { stream ->
                OegenXmlMapper.write(stream, document)
            }
        } catch (e: Exception) {
            throw RuntimeException(IoMessages.getString("Exporter.internalError"), e)
        }
    }
}

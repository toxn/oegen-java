package fr.cdbs.oegen.data.io.xml

import fr.cdbs.oegen.ui.Messages
import java.io.OutputStream

class Exporter(outputStream: OutputStream?) : fr.cdbs.oegen.data.io.Exporter(outputStream) {
    override fun doExport() {
        requireNotNull(outputStream) { "Output stream must not be null" }
        try {
            val document = PersonXmlWriter.exportDatabase(isComplete)
            outputStream.use { stream ->
                OegenXmlMapper.write(stream, document)
            }
        } catch (e: Exception) {
            throw RuntimeException(Messages.getString("Exporter.internalError"), e)
        }
    }
}

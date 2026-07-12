package fr.cdbs.oegen.data.io

import java.io.File
import java.io.InputStream
import java.io.OutputStream

enum class DatabaseFileFormat(val extensions: Set<String>, val mimeLabel: String) {
    XML(setOf("xml"), "XML"),
    JSON(setOf("json"), "JSON"),
    YAML(setOf("yaml", "yml"), "YAML"),
    ;

    companion object {
        fun fromFile(file: File): DatabaseFileFormat {
            val ext = file.extension.lowercase()
            return entries.firstOrNull { ext in it.extensions }
                ?: throw IllegalArgumentException("Unsupported file extension: .$ext")
        }

        fun fromFileName(fileName: String): DatabaseFileFormat? {
            val ext = fileName.substringAfterLast('.', "").lowercase()
            if (ext.isEmpty()) return null
            return entries.firstOrNull { ext in it.extensions }
        }
    }
}

fun createImporter(inputStream: InputStream, format: DatabaseFileFormat): fr.cdbs.oegen.data.io.Importer {
    return when (format) {
        DatabaseFileFormat.XML -> fr.cdbs.oegen.data.io.xml.Importer(inputStream)
        DatabaseFileFormat.JSON -> fr.cdbs.oegen.data.io.json.Importer(inputStream)
        DatabaseFileFormat.YAML -> fr.cdbs.oegen.data.io.yaml.Importer(inputStream)
    }
}

fun createExporter(outputStream: OutputStream, format: DatabaseFileFormat): fr.cdbs.oegen.data.io.Exporter {
    return when (format) {
        DatabaseFileFormat.XML -> fr.cdbs.oegen.data.io.xml.Exporter(outputStream)
        DatabaseFileFormat.JSON -> fr.cdbs.oegen.data.io.json.Exporter(outputStream)
        DatabaseFileFormat.YAML -> fr.cdbs.oegen.data.io.yaml.Exporter(outputStream)
    }
}

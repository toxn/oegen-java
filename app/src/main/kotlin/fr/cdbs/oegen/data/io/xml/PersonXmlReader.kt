package fr.cdbs.oegen.data.io.xml

import fr.cdbs.oegen.data.io.PersonGraphReader
import fr.cdbs.oegen.data.io.model.OegenDbDocument

@Deprecated("Use PersonGraphReader", ReplaceWith("PersonGraphReader"))
object PersonXmlReader {
    fun importDocument(document: OegenDbDocument) = PersonGraphReader.importDocument(document)
}

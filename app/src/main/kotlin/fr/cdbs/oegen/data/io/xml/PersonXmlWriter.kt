package fr.cdbs.oegen.data.io.xml

import fr.cdbs.oegen.data.io.PersonGraphWriter
import fr.cdbs.oegen.data.io.model.OegenDbDocument

@Deprecated("Use PersonGraphWriter", ReplaceWith("PersonGraphWriter"))
object PersonXmlWriter {
    fun exportDatabase(isComplete: Boolean): OegenDbDocument = PersonGraphWriter.exportDatabase(isComplete)
}

/**
 *
 */
package fr.cdbs.oegen.data.io.xml

import fr.cdbs.oegen.data.Person
import org.w3c.dom.Document
import org.xml.sax.SAXException
import java.io.File
import java.io.IOException
import java.io.InputStream
import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.validation.Schema
import javax.xml.validation.SchemaFactory

/**
 * Singelton class wrapping Xml setup
 *
 * @author toxn
 */
class XmlIOContext private constructor() {
    private var db: DocumentBuilder? = null

    init {
        try {
            val sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
            val schema: Schema? = sf.newSchema(File(PATH_TO_XSD))

            val dbf = DocumentBuilderFactory.newInstance()
            dbf.schema = schema
            db = dbf.newDocumentBuilder()
        } catch (e: ParserConfigurationException) {
            // TODO Bloc catch généré automatiquement
            e.printStackTrace()
        } catch (e: SAXException) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val PATH_TO_XSD = "oegen-kiss.xsd" //$NON-NLS-1$

        private val context = XmlIOContext()

        const val DB_NAME: String = "oegendb" //$NON-NLS-1$
        const val DB_SCHEMA_W3C_PREFIX: String = "xmlns:xsi" //$NON-NLS-1$
        const val DB_SCHEMA_W3C_URI: String = "http://www.w3.org/2001/XMLSchema-instance" //$NON-NLS-1$
        const val DB_SCHEMA_OEGEN_PREFIX: String = "xsi:schemaLocation" //$NON-NLS-1$

        const val DB_SCHEMA_OEGEN_URI: String = "fr.cdbs.oegen oegen-kiss.xsd" //$NON-NLS-1$

        const val OEBJECT_ID: String = Person.PROPERTY_ID
        const val PERSON_NAME: String = Person.CLASSNAME
        const val PERSON_ATTR_RELATION: String = "relation" //$NON-NLS-1$
        const val PERSON_FIRSTNAME: String = Person.PROPERTY_FIRSTNAME
        const val PERSON_LASTNAME: String = Person.PROPERTY_LASTNAME

        const val PERSON_GENDER: String = Person.PROPERTY_GENDER
        const val PERSONREF_NAME: String = "personRef" //$NON-NLS-1$

        const val PERSONREF_ATTR_REF: String = "ref" //$NON-NLS-1$
        const val LISTOF_NAME: String = "list" //$NON-NLS-1$

        const val LISTOF_ATTR_OF: String = "of" //$NON-NLS-1$

        const val P2PRELTYPE_CHILD: String = Person.TAG_CHILD
        const val P2PRELTYPE_FATHER: String = Person.PROPERTY_FATHER
        const val P2PRELTYPE_MOTHER: String = Person.PROPERTY_MOTHER
        const val P2PRELTYPE_PARENT: String = "parent" //$NON-NLS-1$

        @Throws(SAXException::class, IOException::class)
        fun getDocument(inputStream: InputStream?): Document? {
            return if (inputStream == null) context.db!!.newDocument()
            else context.db!!.parse(inputStream)
        }
    }
}

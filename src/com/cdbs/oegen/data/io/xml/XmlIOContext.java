/**
 *
 */
package com.cdbs.oegen.data.io.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.cdbs.oegen.data.Person;

/**
 * Singelton class wrapping Xml setup
 *
 * @author toxn
 *
 */
public class XmlIOContext {
    private static final String PATH_TO_XSD = "oegen-kiss.xsd"; //$NON-NLS-1$

    private static XmlIOContext context = new XmlIOContext();

    public static final String DB_NAME = "oegendb"; //$NON-NLS-1$
    public static final String DB_SCHEMA_W3C_PREFIX = "xmlns:xsi"; //$NON-NLS-1$
    public static final String DB_SCHEMA_W3C_URI = "http://www.w3.org/2001/XMLSchema-instance"; //$NON-NLS-1$
    public static final String DB_SCHEMA_OEGEN_PREFIX = "xsi:schemaLocation"; //$NON-NLS-1$

    public static final String DB_SCHEMA_OEGEN_URI = "com.cdbs.oegen oegen-kiss.xsd"; //$NON-NLS-1$

    public static final String OEBJECT_ID = Person.PROPERTY_ID;
    public static final String PERSON_NAME = Person.CLASSNAME;
    public static final String PERSON_ATTR_RELATION = "relation"; //$NON-NLS-1$
    public static final String PERSON_FIRSTNAME = Person.PROPERTY_FIRSTNAME;
    public static final String PERSON_LASTNAME = Person.PROPERTY_LASTNAME;

    public static final String PERSON_GENDER = Person.PROPERTY_GENDER;
    public static final String PERSONREF_NAME = "personRef"; //$NON-NLS-1$

    public static final String PERSONREF_ATTR_REF = "ref"; //$NON-NLS-1$
    public static final String LISTOF_NAME = "list"; //$NON-NLS-1$

    public static final String LISTOF_ATTR_OF = "of"; //$NON-NLS-1$

    public static final String P2PRELTYPE_CHILD = Person.TAG_CHILD;
    public static final String P2PRELTYPE_FATHER = Person.PROPERTY_FATHER;
    public static final String P2PRELTYPE_MOTHER = Person.PROPERTY_MOTHER;
    public static final String P2PRELTYPE_PARENT = "parent"; //$NON-NLS-1$

    public static Document getDocument(InputStream inputStream) throws SAXException, IOException {
	if (inputStream == null)
	    return context.db.newDocument();
	else
	    return context.db.parse(inputStream);
    }
    private DocumentBuilder db;

    private XmlIOContext() {
	try {
	    SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	    Schema schema = sf.newSchema(new File(PATH_TO_XSD));

	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    dbf.setSchema(schema);
	    db = dbf.newDocumentBuilder();

	} catch (ParserConfigurationException | SAXException e) {
	    // TODO Bloc catch généré automatiquement
	    e.printStackTrace();
	}
    }
}

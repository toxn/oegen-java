package com.cdbs.oegen.data.io.xml;

import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.cdbs.oegen.data.Person;
import com.cdbs.oegen.data.Person.Gender;

public class Exporter extends com.cdbs.oegen.data.io.Exporter {
    private static final String PERSON_REF = "personRef"; //$NON-NLS-1$
    DocumentBuilderFactory dbf;
    DocumentBuilder db;
    Document doc;

    HashMap<Person, Element> exportedPersons = new HashMap<>();

    public Exporter(OutputStream is) {
	super(is);
	dbf = DocumentBuilderFactory.newInstance();

	try {

	    // dbf.setValidating(true);
	    // dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
	    // javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
	    // dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource",
	    // new File("oegen-kiss.xsd"));
	    SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	    Schema schema = sf.newSchema(new File("oegen-kiss.xsd"));
	    dbf.setSchema(schema);
	    db = dbf.newDocumentBuilder();
	    doc = db.newDocument();

	} catch (ParserConfigurationException | SAXException e) {
	    // TODO Bloc catch généré automatiquement
	    e.printStackTrace();
	}
    }


    @Override
    public void doExport() {
	Element db = doc.createElement("oegendb");
	db.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
	db.setAttribute("xsi:schemaLocation", "com.cdbs.oegen oegen-kiss.xsd");
	doc.appendChild(db);
	for(Person p : Person.persons) {
	    if (exportedPersons.containsKey(p)) {
		// Person is already exported, let's skip it
		continue;
	    }

	    db.appendChild(exportPerson(p, null));
	}

	TransformerFactory transformerFactory = TransformerFactory.newInstance();
	try {
	    Transformer transformer = transformerFactory.newTransformer();
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
	    DOMSource source = new DOMSource(doc);
	    StreamResult result = new StreamResult(outputStream);
	    transformer.transform(source, result);
	} catch (TransformerException e) {
	    // TODO Bloc catch généré automatiquement
	    e.printStackTrace();
	}
    }

    /**
     * Add a Person to an element
     * @param person person to add
     */
    public Element exportPerson(Person person, Person source) {
	Element personElement;
	String id = person.getId();
	String firstName = person.getFirstName();
	String lastName = person.getLastName();
	Gender gender = person.getGender();
	Person father = person.getFather();
	Person mother = person.getMother();

	if (exportedPersons.containsKey(person)) {
	    personElement = doc.createElement(PERSON_REF);
	    Element ep = exportedPersons.get(person);
	    if (!ep.hasAttribute(Person.PROPERTY_ID)) {
		ep.setAttribute(Person.PROPERTY_ID, id);
	    }

	    personElement.setAttribute("ref", id);
	} else {
	    personElement = doc.createElement(Person.CLASSNAME);
	    if (!id.startsWith(Person.GENERATED_ID_PREFIX)) {
		personElement.setAttribute(Person.PROPERTY_ID, id);
	    }
	    exportedPersons.put(person, personElement);

	    if (firstName != null) {
		Element firstNameElement = doc.createElement(Person.PROPERTY_FIRSTNAME);
		firstNameElement.setTextContent(firstName);
		personElement.appendChild(firstNameElement);
	    }

	    if (lastName != null) {
		Element lastNameElement = doc.createElement(Person.PROPERTY_LASTNAME);
		lastNameElement.setTextContent(lastName);
		personElement.appendChild(lastNameElement);
	    }

	    if (gender != Gender.Unknown) {
		Element genderElement = doc.createElement(Person.PROPERTY_GENDER);
		genderElement.setTextContent(gender.toString());
		personElement.appendChild(genderElement);
	    }

	    if (father != null && father != source) {
		Element fatherElement = exportPerson(person.getFather(), person);
		fatherElement.setAttribute(Importer.PERSON_TAG_RELATION, Person.PROPERTY_FATHER);
		personElement.appendChild(fatherElement);
	    }

	    if (mother != null && source != mother) {
		Element motherElement = exportPerson(person.getMother(), person);
		motherElement.setAttribute(Importer.PERSON_TAG_RELATION, Person.PROPERTY_MOTHER);
		personElement.appendChild(motherElement);
	    }

	    if (!person.children.isEmpty()) {
		for (Person child : person.children) {
		    if (child != source) {
			Element childElement = exportPerson(child, person);
			childElement.setAttribute(Importer.PERSON_TAG_RELATION, Person.TAG_CHILD);
			personElement.appendChild(childElement);
		    }
		}
	    }
	}

	return personElement;
    }

}

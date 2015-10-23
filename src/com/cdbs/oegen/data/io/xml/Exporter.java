package com.cdbs.oegen.data.io.xml;

import java.io.OutputStream;
import java.util.HashMap;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.cdbs.oegen.data.Person;
import com.cdbs.oegen.data.Person.Gender;
import com.cdbs.oegen.ui.Messages;

public class Exporter extends com.cdbs.oegen.data.io.Exporter {

    Document doc;

    HashMap<Person, Element> exportedPersons = new HashMap<>();

    public Exporter(OutputStream is) {
	super(is);

    }

    @Override
    public void doExport() {
	try {
	    doc = XmlIOContext.getDocument(null);
	} catch (Exception e1) {
	    throw new RuntimeException(Messages.getString("Exporter.internalError"), e1); //$NON-NLS-1$
	}

	Element db = doc.createElement(XmlIOContext.DB_NAME);
	db.setAttribute(XmlIOContext.DB_SCHEMA_W3C_PREFIX, XmlIOContext.DB_SCHEMA_W3C_URI);
	db.setAttribute(XmlIOContext.DB_SCHEMA_OEGEN_PREFIX, XmlIOContext.DB_SCHEMA_OEGEN_URI);
	doc.appendChild(db);

	for(Person p : Person.persons) {
	    if (!isComplete() && exportedPersons.containsKey(p)) {
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
	    personElement = doc.createElement(XmlIOContext.PERSONREF_NAME);
	    Element ep = exportedPersons.get(person);
	    if (!ep.hasAttribute(Person.PROPERTY_ID)) {
		ep.setAttribute(Person.PROPERTY_ID, id);
	    }

	    personElement.setAttribute(XmlIOContext.PERSONREF_ATTR_REF, id);
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

	    if (father != null && (father != source || isComplete())) {
		Element fatherElement = exportPerson(person.getFather(), person);
		fatherElement.setAttribute(XmlIOContext.PERSON_ATTR_RELATION, Person.PROPERTY_FATHER);
		personElement.appendChild(fatherElement);
	    }

	    if (mother != null && (source != mother || isComplete())) {
		Element motherElement = exportPerson(person.getMother(), person);
		motherElement.setAttribute(XmlIOContext.PERSON_ATTR_RELATION, Person.PROPERTY_MOTHER);
		personElement.appendChild(motherElement);
	    }

	    /*
	     * In case we are in complete mode, children list is appened even if
	     * it's empty. If else, children list is not appened whenever it is
	     * empty or it only contains the parent node (ie source).
	     */
	    if (isComplete() || !(person.children.isEmpty()
		    || person.children.size() == 1 && person.children.contains(source))) {
		Element childrenElement = doc.createElement(XmlIOContext.LISTOF_NAME);
		personElement.appendChild(childrenElement);

		childrenElement.setAttribute(XmlIOContext.LISTOF_ATTR_OF, XmlIOContext.P2PRELTYPE_CHILD);

		for (Person child : person.children) {
		    if (child != source || isComplete()) {
			childrenElement.appendChild(exportPerson(child, person));
		    }
		}
	    }
	}

	return personElement;
    }

}

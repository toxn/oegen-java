package data.io.xml;

import java.io.OutputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import data.Person;
import data.Person.Gender;

public class Exporter extends data.io.Exporter {
    DocumentBuilderFactory dbf;
    DocumentBuilder db;
    Document doc;

    HashMap<Person, Element> exportedPersons = new HashMap<>();

    public Exporter(OutputStream is) {
	super(is);
	dbf = DocumentBuilderFactory.newInstance();

	try {
	    db = dbf.newDocumentBuilder();
	    doc = db.newDocument();

	} catch (ParserConfigurationException e) {
	    // TODO Bloc catch généré automatiquement
	    e.printStackTrace();
	}
    }


    @Override
    public void doExport() {
	for(Person p : Person.persons) {
	    if (exportedPersons.containsKey(p)) {
		// Person is already exported, let's skip it
		continue;
	    }

	    doc.appendChild(exportPerson(p, null));
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
	Element personElement = doc.createElement(Person.CLASSNAME);
	String id = person.getId();
	String firstName = person.getFirstName();
	String lastName = person.getLastName();
	Gender gender = person.getGender();
	Person father = person.getFather();
	Person mother = person.getMother();

	if (exportedPersons.containsKey(person)) {
	    Element ep = exportedPersons.get(person);
	    if (!ep.hasAttribute(Person.PROPERTY_ID)) {
		ep.setAttribute(Person.PROPERTY_ID, id);
	    }

	    personElement.setAttribute(Person.PROPERTY_ID, id);
	} else {
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

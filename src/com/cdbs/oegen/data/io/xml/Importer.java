package com.cdbs.oegen.data.io.xml;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.cdbs.oegen.data.Person;

public class Importer extends com.cdbs.oegen.data.io.Importer {
    private static void createRelation(Person source, Person destination, String relation) {
	switch (relation) {
	case XmlIOContext.P2PRELTYPE_CHILD:
	    source.children.addElement(destination);
	    break;

	case XmlIOContext.P2PRELTYPE_FATHER:
	    source.setFather(destination);
	    break;

	case XmlIOContext.P2PRELTYPE_MOTHER:
	    source.setMother(destination);
	    break;

	case XmlIOContext.P2PRELTYPE_PARENT:
	    switch (destination.getGender()) {
	    case Male:
		source.setFather(destination);
		break;

	    case Female:
		source.setMother(destination);
		break;

	    default:
		throw new RuntimeException("Parent relation without Male/Female gender.");
	    }
	}
    }

    private static void importDB(Element element) {

	assert element.getNodeName() == XmlIOContext.DB_NAME;

	Node child = element.getFirstChild();

	while (child != null) {
	    if (child.getNodeType() == Node.ELEMENT_NODE) {
		switch (child.getNodeName()) {
		case XmlIOContext.PERSON_NAME:
		    importPerson((Element) child);
		}
	    }

	    child = child.getNextSibling();
	}
	return;

    }

    private static Person importPerson(Element element) {
	Person currentPerson = new Person();
	Person subPerson;

	String id = element.getAttribute(XmlIOContext.OEBJECT_ID);
	if (!id.isEmpty()) {
	    currentPerson.setId(id);
	}

	NodeList nl = element.getChildNodes();

	for (int i = 0; i < nl.getLength(); i++) {
	    Node n = nl.item(i);

	    if (n.getNodeType() == Node.ELEMENT_NODE) {
		Element e = (Element)n;

		switch (e.getNodeName()) {
		case XmlIOContext.PERSON_FIRSTNAME:
		    currentPerson.setFirstName(e.getTextContent());
		    break;

		case XmlIOContext.PERSON_LASTNAME:
		    currentPerson.setLastName(e.getTextContent());
		    break;

		case XmlIOContext.PERSON_GENDER:
		    currentPerson.setGender(Person.Gender.valueOf(e.getTextContent()));
		    break;

		case XmlIOContext.PERSON_NAME:
		    subPerson = importPerson(e);

		    switch (e.getAttribute(XmlIOContext.PERSON_ATTR_RELATION)) {
		    case Person.PROPERTY_FATHER:
			currentPerson.setFather(subPerson);
			break;

		    case Person.PROPERTY_MOTHER:
			currentPerson.setMother(subPerson);
			break;

		    case Person.TAG_CHILD:
			currentPerson.children.addElement(subPerson);
			break;

		    default:
			throw new RuntimeException(
				MessageFormat.format("", //$NON-NLS-1$
					e.getAttribute(XmlIOContext.PERSON_ATTR_RELATION)));
		    }
		    break;

		case XmlIOContext.PERSONREF_NAME:
		    createRelation(currentPerson, Person.persons.search(e.getAttribute(XmlIOContext.PERSONREF_ATTR_REF)), e.getAttribute(XmlIOContext.LISTOF_ATTR_OF));

		    break;

		case XmlIOContext.LISTOF_NAME:
		    String relation = e.getAttribute(XmlIOContext.LISTOF_ATTR_OF);

		    Node nodeFromList = e.getFirstChild();

		    while (nodeFromList != null) {
			if (nodeFromList.getNodeType() == Node.ELEMENT_NODE) {
			    Element elFromList = (Element) nodeFromList;
			    switch(nodeFromList.getNodeName()) {
			    case XmlIOContext.PERSON_NAME:
				createRelation(currentPerson, importPerson(elFromList), relation);
				break;

			    case XmlIOContext.PERSONREF_NAME:
				createRelation(currentPerson,
					Person.persons.search(elFromList.getAttribute(XmlIOContext.PERSONREF_ATTR_REF)),
					relation);
				break;

			    }
			}

			nodeFromList = nodeFromList.getNextSibling();
		    }
		}
	    }
	}

	return currentPerson;
    }

    public Importer(InputStream is) {
	super(is);
    }

    @Override
    public void doImport() {

	try {
	    NodeList nl = XmlIOContext.getDocument(inputStream).getChildNodes();


	    for (int i = 0; i < nl.getLength(); i++) {
		Node n = nl.item(i);

		if (n.getNodeType() == Node.ELEMENT_NODE) {
		    if (n.getNodeName() == XmlIOContext.DB_NAME) {
			importDB((Element) n);
			// every following nodes are ignored
			return;
		    }
		}
	    }
	} catch (SAXException e) {
	    // TODO Bloc catch généré automatiquement
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Bloc catch généré automatiquement
	    e.printStackTrace();
	}
    }

}

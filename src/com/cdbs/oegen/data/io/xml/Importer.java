package com.cdbs.oegen.data.io.xml;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.cdbs.oegen.data.Person;
import com.cdbs.oegen.ui.Messages;

public class Importer extends com.cdbs.oegen.data.io.Importer {
    private static void importNode(Node node) {
	switch (node.getNodeName()) {
	case Person.CLASSNAME:
	    importPerson(node);
	    return;

	default:
	    // Just skip unknown com.cdbs.oegen.data
	    System.err.println(MessageFormat.format(Messages.getString("Importer.0"), node.getNodeName())); //$NON-NLS-1$
	}

    }

    private static Person importPerson(Node node) {
	Person p = new Person();

	Node idAttr = node.getAttributes().getNamedItem(Person.PROPERTY_ID);
	if(idAttr != null) {
	    p.setId(idAttr.getNodeValue());
	}

	NodeList nl = node.getChildNodes();

	for (int i = 0; i < nl.getLength(); i++) {
	    Node n = nl.item(i);

	    if (n.getNodeType() == Node.ELEMENT_NODE) {
		switch (n.getNodeName()) {
		case Person.PROPERTY_FIRSTNAME:
		    p.setFirstName(n.getNodeValue());
		    break;

		case Person.PROPERTY_LASTNAME:
		    p.setLastName(n.getNodeValue());
		    break;

		case Person.PROPERTY_GENDER:
		    p.setGender(Enum.valueOf(Person.Gender.class, n.getNodeValue()));
		    break;

		case Person.CLASSNAME:
		    Person p2 = importPerson(n);

		    switch (n.getAttributes().getNamedItem(XmlIOContext.PERSON_ATTR_RELATION).getNodeValue()) {
		    case Person.PROPERTY_FATHER:
			p.setFather(p2);
			break;

		    case Person.PROPERTY_MOTHER:
			p.setMother(p2);
			break;

		    case Person.TAG_CHILD:
			p.children.addElement(p2);
			break;

		    default:
			throw new RuntimeException(
				MessageFormat.format(Messages.getString("Importer.UnkownRelationType"), //$NON-NLS-1$
					n.getAttributes().getNamedItem(XmlIOContext.PERSON_ATTR_RELATION).getNodeValue()));
		    }
		}
	    }
	}

	return p;
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
		    importNode(n);
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

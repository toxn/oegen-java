package data.io;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import data.Person;
import ui.Messages;

public class XmlImporter extends Importer {
    DocumentBuilderFactory dbf;
    DocumentBuilder db;
    Document doc;

    public XmlImporter(InputStream is) {
	super(is);

	dbf = DocumentBuilderFactory.newInstance();

	try {
	    db = dbf.newDocumentBuilder();
	    Document doc = db.parse(inputStream);

	} catch (ParserConfigurationException | SAXException | IOException e) {
	    // TODO Bloc catch généré automatiquement
	    e.printStackTrace();
	}
    }

    @Override
    public void doImport() {
	NodeList nl = doc.getChildNodes();

	Node n;
	for (int i = 0; i < nl.getLength(); i++) {
	    importNode(nl.item(i));
	}
    }

    private void importNode(Node node) {
	switch (node.getNodeName()) {
	case Person.CLASSNAME:
	    importPerson(node);
	    return;

	default:
	    // Just skip unknown data
	    System.err.println(MessageFormat.format(Messages.getString("Importer.0"), node.getNodeName())); //$NON-NLS-1$
	}

    }

    private void importPerson(Node node) {
	Person p = new Person();


    }

}

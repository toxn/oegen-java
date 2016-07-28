/**
 *
 */
package com.cdbs.oegen.ui.swing;

import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;

import com.cdbs.oegen.data.Person;
import com.cdbs.oegen.ui.PersonListModel;

/**
 * @author toxn
 *
 */
public class PersonRelations extends PersonRelationalComponent {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static String addPersonsToHtmlList(PersonListModel children) {
	String result = ""; //$NON-NLS-1$

	if (children == null)
	    return result;

	for (Person p : children) {
	    result += addPersonToHtmlList(p);
	}

	return result;
    }

    private static String addPersonToHtmlList(Person person) {
	if (person == null)
	    return ""; //$NON-NLS-1$

	return "<li>" //$NON-NLS-1$
		+ "     <span class=\"name\">" //$NON-NLS-1$
		+ "         <span class=\"" + Person.PROPERTY_GENDER + "\">" + person.getGender().getSymbol() //$NON-NLS-1$ //$NON-NLS-2$
		+ "</span> " //$NON-NLS-1$
		+ "         <span class=\"" + Person.PROPERTY_LASTNAME + "\" id=\"" + Person.PROPERTY_LASTNAME + "\">" //$NON-NLS-1$//$NON-NLS-2$
		+ person.getLastName() + "</span> " //$NON-NLS-1$
		+ "         <span class=\"" + Person.PROPERTY_FIRSTNAME + "\">" //$NON-NLS-1$//$NON-NLS-2$
		+ person.getFirstName() + "</span>" //$NON-NLS-1$
		+ "     </span>" //$NON-NLS-1$
		+ "</li"; //$NON-NLS-1$

    }

    private static void changeField(HTMLDocument doc, String id, String newValue) {
	try {
	    // doc.setInnerHTML(doc.getElement(id), newValue);
	    doc.setOuterHTML(doc.getElement(id),
		    "<span class=\"" + id + "\" id=\"" + id + "\">" + newValue + "</span>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	} catch (BadLocationException e) {
	    // TODO Bloc catch généré automatiquement
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Bloc catch généré automatiquement
	    e.printStackTrace();
	}

	System.out.println(doc);
    }

    private final JTextPane textPane = new JTextPane();

    private final String defaultText = "<html>"  //$NON-NLS-1$
	    + "  <head>" //$NON-NLS-1$
	    + "   <title>An example HTMLDocument</title>" //$NON-NLS-1$
	    + "   </head>" //$NON-NLS-1$
	    + "   <body>" //$NON-NLS-1$
	    + "     <span id=\"name\">" //$NON-NLS-1$
	    + "       <h1>" //$NON-NLS-1$
	    + "         <span id=\"" + Person.PROPERTY_GENDER + "\">?</span> " //$NON-NLS-1$ //$NON-NLS-2$
	    + "         <span class=\"" + Person.PROPERTY_LASTNAME + "\" id=\"" + Person.PROPERTY_LASTNAME //$NON-NLS-1$ //$NON-NLS-2$
	    + "\">Last Name</span> "
	    + "         <span id=\"" + Person.PROPERTY_FIRSTNAME + "\">First Name</span>" //$NON-NLS-1$ //$NON-NLS-2$
	    + "       </h1>" //$NON-NLS-1$
	    + "     </span>" //$NON-NLS-1$
	    + "   </body>" //$NON-NLS-1$
	    + "</html>"; //$NON-NLS-1$

    public PersonRelations() {
	setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	textPane.setEditable(false);
	textPane.setContentType("text/html");

	// Create style sheet
	((HTMLDocument) textPane.getDocument()).getStyleSheet()
	.addRule(".name." + Person.PROPERTY_LASTNAME + " { font-variant: small-caps; }");

	// Insert default text
	textPane.setText(defaultText);

	this.add(textPane);

	// Change text to actual person
	rebuild();
    }

    @Override
    protected void rebuild() {
	Person center = getCenter();

	if (center == null)
	    return;

	textPane.setText("<html>" //$NON-NLS-1$
		+ "  <head>" //$NON-NLS-1$
		+ "   <title>An example HTMLDocument</title>" //$NON-NLS-1$
		+ "   </head>" //$NON-NLS-1$
		+ "   <body>" //$NON-NLS-1$
		+ "     <span class=\"name\">" //$NON-NLS-1$
		+ "       <h1>" //$NON-NLS-1$
		+ "         <span id=\"" + Person.PROPERTY_GENDER + "\">" + center.getGender().getSymbol() + "</span> " //$NON-NLS-1$ //$NON-NLS-2$
		+ "         <span class=\"" + Person.PROPERTY_LASTNAME + "\" id=\"" + Person.PROPERTY_LASTNAME //$NON-NLS-1$ //$NON-NLS-2$
		+ "\">" + center.getLastName() + "</span> " + "         <span id=\"" + Person.PROPERTY_FIRSTNAME + "\">" //$NON-NLS-3$ //$NON-NLS-4$
		+ center.getFirstName() + "</span>" + "       </h1>" //$NON-NLS-2$
		+ "     </span>" //$NON-NLS-1$
		+ "     <ul>" //$NON-NLS-1$
		+ "       <li>Parents" //$NON-NLS-1$
		+ "         <ul>" //$NON-NLS-1$
		+ addPersonToHtmlList(center.getFather()) + addPersonToHtmlList(center.getMother()) + "         </ul" //$NON-NLS-1$
		+ "       </li>" //$NON-NLS-1$
		+ "       <li>Siblings" //$NON-NLS-1$
		+ "         <ul>" //$NON-NLS-1$
		+ addPersonsToHtmlList(center.getFather() == null ? null : center.getFather().children)
		+ "         </ul>" //$NON-NLS-1$
		+ "       </li>" //$NON-NLS-1$
		+ "       <li>Children" //$NON-NLS-1$
		+ "         <ul>" //$NON-NLS-1$
		+ addPersonsToHtmlList(center.children)
		+ "         </ul>" //$NON-NLS-1$
		+ "       </li>" //$NON-NLS-1$
		+ "     </ul>" //$NON-NLS-1$
		+ "   </body>" //$NON-NLS-1$
		+ "</html>"); //$NON-NLS-1$
	/*
	 * HTMLDocument doc = (HTMLDocument) textPane.getDocument();
	 * changeField(doc, Person.PROPERTY_GENDER,
	 * center.getGender().getSymbol()); changeField(doc,
	 * Person.PROPERTY_FIRSTNAME, center.getFirstName()); changeField(doc,
	 * Person.PROPERTY_LASTNAME, center.getLastName());
	 */
    }

}

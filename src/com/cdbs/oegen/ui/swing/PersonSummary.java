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
import com.cdbs.oegen.ui.PersonList;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import javax.swing.event.EventListenerList;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * @author toxn
 *
 */
public class PersonSummary extends PersonRelationalComponent implements HyperlinkListener{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public void addPersonClickListener(PersonClickListener pcl) {
        listeners.add(pcl);
    }
    
    public void removePersonClickListener(PersonClickListener pcl) {
        listeners.remove(pcl);
    }

    /**
     *
     */
    public class PersonClickEvent extends EventObject {
        private final Person person;
        
        public PersonClickEvent(Object source, Person person) {
            super(source);
            
            this.person = person;
        }
        
        public Person getPerson() {
            return person;
        }
    }
    
    public interface PersonClickListener extends EventListener {
        void personClick(PersonClickEvent pce);
    }
    
    private final ArrayList<PersonClickListener> listeners = new ArrayList<>();
    
    private void firePersonClick(Person person) {
        PersonClickEvent evt = new PersonClickEvent(this, person);
        
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for(PersonClickListener pcl : listeners) {
            pcl.personClick(evt);
        }
    }
    
    private static String personListToHtmlList(PersonList children) {
	String result = ""; //$NON-NLS-1$

	if (children == null)
	    return result;

	for (Person p : children) {
	    result += personToHtmlList(p);
	}

	return result;
    }

    private static String personToHtmlList(Person person) {
	if (person == null)
	    return ""; //$NON-NLS-1$

	return "<li>" //$NON-NLS-1$
		+ personToHtml(person, true)
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
	    + "     <h1>" //$NON-NLS-1$
	    + "       <span id=\"name\">" //$NON-NLS-1$
	    + "         <span id=\"" + Person.PROPERTY_GENDER + "\">?</span> " //$NON-NLS-1$ //$NON-NLS-2$
	    + "         <span class=\"" + Person.PROPERTY_LASTNAME + "\" id=\"" + Person.PROPERTY_LASTNAME //$NON-NLS-1$ //$NON-NLS-2$
	    + "\">Last Name</span> "
	    + "         <span id=\"" + Person.PROPERTY_FIRSTNAME + "\">First Name</span>" //$NON-NLS-1$ //$NON-NLS-2$
	    + "       </span>" //$NON-NLS-1$
	    + "     </h1>" //$NON-NLS-1$
	    + "   </body>" //$NON-NLS-1$
	    + "</html>"; //$NON-NLS-1$

    public PersonSummary() {
	setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	textPane.setEditable(false);
	textPane.setContentType("text/html");

	// Create style sheet
	((HTMLDocument) textPane.getDocument()).getStyleSheet()
                .addRule("." + Person.PROPERTY_LASTNAME + " { font-variant: small-caps; }");

	// Insert default text
	textPane.setText(defaultText);
        
        textPane.addHyperlinkListener(this);


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
		+ "       <h1>" //$NON-NLS-1$
		+ personToHtml(center, false)
                + "       </h1>" //$NON-NLS-2$
		+ "     <ul>" //$NON-NLS-1$
		+ "       <li>Parents" //$NON-NLS-1$
		+ "         <ul>" //$NON-NLS-1$
		+ personToHtmlList(center.getFather()) 
                + personToHtmlList(center.getMother()) 
                + "         </ul" //$NON-NLS-1$
		+ "       </li>" //$NON-NLS-1$
		+ "       <li>Siblings" //$NON-NLS-1$
		+ "         <ul>" //$NON-NLS-1$
		+ personListToHtmlList(center.getSiblings())
		+ "         </ul>" //$NON-NLS-1$
		+ "       </li>" //$NON-NLS-1$
		+ "       <li>Children" //$NON-NLS-1$
		+ "         <ul>" //$NON-NLS-1$
		+ personListToHtmlList(center.children)
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

    private static String personToHtml(Person person, boolean link) {
        String result = "";
        
        if (person == null)
            return result;
        
        result += "<span class=\"" + Person.PROPERTY_GENDER + "\">" + person.getGender().getSymbol() //$NON-NLS-1$ //$NON-NLS-2$
		+ "</span> " //$NON-NLS-1$
		+ "<span class=\"" + Person.PROPERTY_LASTNAME + "\" id=\"" + Person.PROPERTY_LASTNAME + "\">" //$NON-NLS-1$//$NON-NLS-2$
		+ person.getLastName() + "</span> " //$NON-NLS-1$
		+ "<span class=\"" + Person.PROPERTY_FIRSTNAME + "\">" //$NON-NLS-1$//$NON-NLS-2$
		+ person.getFirstName() + "</span>"; //$NON-NLS-1$

        if(link)
            result = "<a href=\"" + person.getId() + "\">" //$NON-NLS-1$
		+ result
		+ "</a"; //$NON-NLS-1$
        
        return result;
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
            firePersonClick(Person.persons.search(e.getDescription()));
    }

}

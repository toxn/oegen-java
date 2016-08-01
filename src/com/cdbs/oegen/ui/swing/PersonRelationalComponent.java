/**
 *
 */
package com.cdbs.oegen.ui.swing;

import javax.swing.JComponent;

import com.cdbs.oegen.data.Person;

/**
 * @author toxn
 *
 */
public abstract class PersonRelationalComponent extends JComponent
implements com.cdbs.oegen.ui.PersonRelationalComponent {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Person center;

    private Scope scope;

    /**
     * @return le center
     */
    public Person getCenter() {
	return center;
    }

    /**
     * @return le scope
     */
    public Scope getScope() {
	return scope;
    }

    /**
     * Recreate the whole graphic
     */
    abstract protected void rebuild();

    public void setCenter(Person newCenter) {
	if (center != newCenter) {
	    center = newCenter;
	    rebuild();
	}
    }


    /**
     * @param scope
     *            le scope à définir
     */
    public void setScope(Scope newScope) {
	if (scope != newScope) {
	    scope = newScope;
	    rebuild();
	}
    }
}
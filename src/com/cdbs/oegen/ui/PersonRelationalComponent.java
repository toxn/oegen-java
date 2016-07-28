/**
 *
 */
package com.cdbs.oegen.ui;

import com.cdbs.oegen.data.Person;

/**
 * @author toxn
 *
 */
public interface PersonRelationalComponent {

    public enum Scope {
	ASCENDANTS, // Display only center and ascendants
	DESCENDANTS, // Display only center and descendants
	BOTH // Display center and both ascendants and descendants
    }

    /**
     * @return le center
     */
    public Person getCenter();

    /**
     * @return le scope
     */
    public Scope getScope();

    public void setCenter(Person newCenter);

    /**
     * @param scope
     *            le scope à définir
     */
    public void setScope(Scope newScope);

}

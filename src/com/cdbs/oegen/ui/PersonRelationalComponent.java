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

    /**
     * The scope tells if the components displays ascendants, descendants, or
     * both, of the center person.
     *
     * @author toxn
     *
     */
    public enum Scope {
	/**
	 * Display only center person and its ascendants
	 */
	ASCENDANTS,
	/**
	 * Display only center person and its descendants
	 */
	DESCENDANTS,
	/**
	 * Display center person and both its ascendants and descendants
	 */
	BOTH
    }

    /**
     * Get the current center person of the component.
     *
     * @return The center person. Can return Null if no center person is set.
     *         defaults to null.
     */
    public Person getCenter();

    /**
     * Get the current scope of the component.
     *
     * The scope tells if the components displays ascendants, descendants, or
     * both, of the center person.
     *
     * @return The current scope.
     * @see Scope
     * @see getCenter
     * @see setScope
     */
    public Scope getScope();

    /**
     * Change the person around which the component is centered.
     *
     * Other persons displayed by the component will be either ascendents or
     * descendants from that center person.
     *
     * @return The center person
     * @see getCenter
     */
    public void setCenter(Person newCenter);

    /**
     * Change the scope of the component
     *
     * @param scope
     *            new Scope
     *
     * @see Scope
     * @see getScope
     */
    public void setScope(Scope newScope);

}

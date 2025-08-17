/**
 *
 */
package fr.cdbs.oegen.ui

import fr.cdbs.oegen.data.Person

/**
 * @author toxn
 */
interface PersonRelationalComponent {
    /**
     * The scope tells if the components displays ascendants, descendants, or
     * both, of the center person.
     *
     * @author toxn
     */
    enum class Scope {
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
     * defaults to null.
     */
    /**
     * Change the person around which the component is centered.
     *
     * Other persons displayed by the component will be either ascendents or
     * descendants from that center person.
     *
     * @return The center person
     * @see getCenter
     */
    var center: Person?

    /**
     * Get the current scope of the component.
     *
     * The scope tells if the components displays ascendants, descendants, or
     * both, of the center person.
     *
     * @return The current scope.
     * @see Scope
     *
     * @see getCenter
     *
     * @see setScope
     */
    /**
     * Change the scope of the component
     *
     * @param scope
     * new Scope
     *
     * @see Scope
     *
     * @see getScope
     */
    var scope: Scope?
}

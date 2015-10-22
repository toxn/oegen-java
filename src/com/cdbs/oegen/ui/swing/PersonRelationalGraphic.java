/**
 *
 */
package com.cdbs.oegen.ui.swing;

import com.cdbs.oegen.ui.Messages;

/**
 * @author toxn
 *
 */
public abstract class PersonRelationalGraphic extends PersonRelationalComponent{
    public enum Orientation {
	TOP2BOTTOM, // Ascendants upward and descendants downward
	LEFT2RIGHT, // Descendants leftward and ascendant rightward
	BOTTOM2TOP, // Descendants upward and ascendants downward
	RIGHT2LEFT // Ascendants leftward and descendant rightward
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    private Orientation orientation;
    private int generations = 4;

    /**
     * @return le generations
     */
    public int getGenerations() {
	return generations;
    }

    /**
     * @return le orientation
     */
    public Orientation getOrientation() {
	return orientation;
    }

    /**
     * @param generations
     *            le generations à définir
     * @throws Exception
     *             Generation number is inferior to 1.
     */
    public void setGenerations(int newGenerations) throws Exception {
	if (generations != newGenerations) {
	    if (newGenerations < 1)
		throw new Exception(Messages.getString("PersonRelationalGraphic.GenerationNumTooLow")); //$NON-NLS-1$

	    generations = newGenerations;
	    rebuild();
	}
    }

    /**
     * @param orientation
     *            le orientation à définir
     */
    public void setOrientation(Orientation newOrientation) {
	if (orientation != newOrientation) {
	    orientation = newOrientation;
	    rebuild();
	}
    }

}

/**
 *
 */
package ui.swing;

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

    /**
     * @return le orientation
     */
    public Orientation getOrientation() {
	return orientation;
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

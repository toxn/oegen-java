/**
 * Displays a tree of ascendant and/or descendants of a person
 */
package ui.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import data.Person;

/**
 * @author toxn
 *
 */
public final class PersonTree
extends PersonRelationalGraphic
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final int boxWidth = 200;
    private final int boxHeight = 40;
    private final int boxSpace = 10;
    private final int boxStrokeWidth = 2;
    private final int lineWidth = 1;
    private final int fontSize = 8;
    private final int boxMargin = 2;
    private final int margin = 5;

    private final BasicStroke boxStroke = new BasicStroke(boxStrokeWidth);
    private final BasicStroke lineStroke = new BasicStroke(lineWidth);

    private void drawBox(Person person, Graphics2D g2d, int x, int y) {
	if (person == null || g2d == null)
	    return;

	// Save graphic context
	Stroke oldStroke = g2d.getStroke();
	Color oldColor = g2d.getColor();

	g2d.setStroke(boxStroke);
	g2d.setColor(Color.white);
	g2d.fillRect(x, y, boxWidth, boxHeight);
	g2d.setColor(Color.black);
	g2d.drawRect(x, y, boxWidth, boxHeight);
	g2d.setFont(getFont().deriveFont(fontSize));
	g2d.drawString(person.getLastName() + ", " + person.getFirstName(), x + boxMargin + boxStrokeWidth,
		y + fontSize + boxMargin + boxStrokeWidth);

	// Restore graphic context
	g2d.setStroke(oldStroke);
	g2d.setColor(oldColor);
    }

    private void drawLeftToRight(Graphics2D g2d) {
	if (g2d != null) {
	    try {
		if (getCenter() == null)
		    return;

		drawBox(getCenter(), g2d, 0, (getHeight() - boxHeight) / 2);
		drawParentsLeftToRight(getCenter(), g2d, 0, (getHeight() - boxHeight) / 2);

	    } finally {
		g2d.dispose(); // clean up
	    }
	}
    }

    private void drawParentsLeftToRight(Person person, Graphics2D g2d, int x, int y) {
	if (person == null || g2d == null)
	    return;

	Person father = person.getFather();
	Person mother = person.getMother();

	if (father == null && mother == null)
	    return;

	// Change graphic context
	Stroke oldStroke = g2d.getStroke();
	Color oldColor = g2d.getColor();

	g2d.setStroke(lineStroke);
	g2d.setColor(Color.black);

	int x1 = x + boxWidth;
	int y1 = y + boxHeight / 2;
	int x2 = x1 + boxSpace / 2;
	int y2 = y1;

	g2d.drawLine(x1, y1, x2, y2);

	x1 = x2;
	x2 += boxSpace / 2;
	y1 = y1 - boxHeight - boxSpace / 2;

	if (father != null) {
	    // Link to father's box
	    g2d.drawLine(x1, y1, x1, y2);
	    g2d.drawLine(x1, y1, x2, y1);

	    // Draw the father's box
	    drawBox(father, g2d, x2, y1 - boxHeight / 2);
	}

	y1 = y2;
	y2 = y2 + boxHeight + boxSpace / 2;

	if (mother != null) {
	    // Link to mother's box
	    g2d.drawLine(x1, y1, x1, y2);
	    g2d.drawLine(x1, y2, x2, y2);

	    // Draw the mother's box
	    drawBox(mother, g2d, x2, y2 - boxHeight / 2);
	}

	// Restore graphic context
	g2d.setStroke(oldStroke);
	g2d.setColor(oldColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
	if (isOpaque()) { // paint background
	    g.setColor(getBackground());
	    g.fillRect(0, 0, getWidth(), getHeight());
	}

	Graphics2D g2d = (Graphics2D) g.create();
	drawLeftToRight(g2d);
    }

    @Override
    protected void rebuild() {
	// Fixed 4 generation horizontal layout

	this.setSize(2 * margin + 4 * boxWidth + 3 * boxSpace, 2 * margin + 8 * boxHeight + 7 * boxSpace);
	this.repaint();
    }


}

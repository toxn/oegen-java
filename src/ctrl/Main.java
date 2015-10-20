/**
 *
 */
package ctrl;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import data.Person;
import data.io.xml.Exporter;
import ui.swing.MainWindowSwing;

/**
 * @author toxn
 *
 */
public final class Main
{
    /**
     * @param args
     */
    public static void main(String[] args)
    {
	// Create a small testing set
	Person oldpa1 = new Person();
	oldpa1.setFirstName("Old'pa"); //$NON-NLS-1$
	oldpa1.setLastName("Oiseau"); //$NON-NLS-1$

	Person oldma1 = new Person();
	oldma1.setFirstName("Old'ma"); //$NON-NLS-1$
	oldma1.setLastName("Oiseau"); //$NON-NLS-1$

	Person oldpa2 = new Person();
	oldpa2.setFirstName("Old'pa"); //$NON-NLS-1$
	oldpa2.setLastName("Oiselle"); //$NON-NLS-1$

	Person oldma2 = new Person();
	oldma2.setFirstName("Old'ma"); //$NON-NLS-1$
	oldma2.setLastName("Oiselle"); //$NON-NLS-1$

	Person oldpa3 = new Person();
	oldpa3.setFirstName("Old'pa"); //$NON-NLS-1$
	oldpa3.setLastName("Geai"); //$NON-NLS-1$

	Person oldma3 = new Person();
	oldma3.setFirstName("Old'ma"); //$NON-NLS-1$
	oldma3.setLastName("Geai"); //$NON-NLS-1$

	Person oldpa4 = new Person();
	oldpa4.setFirstName("Old'pa"); //$NON-NLS-1$
	oldpa4.setLastName("Mésange"); //$NON-NLS-1$

	Person oldma4 = new Person();
	oldma4.setFirstName("Old'ma"); //$NON-NLS-1$
	oldma4.setLastName("Mésange"); //$NON-NLS-1$

	Person grandpa = new Person();
	grandpa.setFirstName("Grand'pa"); //$NON-NLS-1$
	grandpa.setLastName("Oiseau"); //$NON-NLS-1$
	grandpa.setFather(oldpa1);
	grandpa.setMother(oldma1);

	Person grandma = new Person();
	grandma.setFirstName("Granny"); //$NON-NLS-1$
	grandma.setLastName("Oiselle"); //$NON-NLS-1$
	grandma.setFather(oldpa2);
	grandma.setMother(oldma2);

	Person ara = new Person();
	ara.setFirstName("Ara"); //$NON-NLS-1$
	ara.setLastName("Oiseau"); //$NON-NLS-1$
	ara.setFather(grandpa);
	ara.setMother(grandma);

	Person grandpa2 = new Person();
	grandpa2.setFirstName("Grand'pa"); //$NON-NLS-1$
	grandpa2.setLastName("Geai"); //$NON-NLS-1$
	grandpa2.setFather(oldpa3);
	grandpa2.setMother(oldma3);

	Person grandma2 = new Person();
	grandma2.setFirstName("Granny"); //$NON-NLS-1$
	grandma2.setLastName("Mésange"); //$NON-NLS-1$
	grandma2.setFather(oldpa4);
	grandma2.setMother(oldma4);

	Person dad = new Person();
	dad.setFirstName("perroquet"); //$NON-NLS-1$
	dad.setLastName("oiseau"); //$NON-NLS-1$
	dad.setFather(grandpa);
	dad.setMother(grandma);

	Person mom = new Person();
	mom.setFirstName("mésange"); //$NON-NLS-1$
	mom.setLastName("geai"); //$NON-NLS-1$
	mom.setFather(grandpa2);
	mom.setMother(grandma2);

	Person me = new Person();
	me.setFirstName("moineau"); //$NON-NLS-1$
	me.setLastName(dad.getLastName());
	me.setFather(dad);
	me.setMother(mom);

	Person alouette = new Person();
	alouette.setFirstName("alouette"); //$NON-NLS-1$
	alouette.setLastName("oiseau"); //$NON-NLS-1$
	alouette.setFather(dad);
	alouette.setMother(mom);

	Person pélican = new Person();
	pélican.setFirstName("Pélican"); //$NON-NLS-1$
	pélican.setLastName("Oiseau"); //$NON-NLS-1$
	pélican.setMother(mom);
	pélican.setFather(dad);

	Person orphanF = new Person();
	orphanF.setFirstName("Orphan"); //$NON-NLS-1$
	orphanF.setLastName("Of Father"); //$NON-NLS-1$
	orphanF.setMother(mom);

	Person orphanM = new Person();
	orphanM.setFirstName("Orphan"); //$NON-NLS-1$
	orphanM.setLastName("Of Mother"); //$NON-NLS-1$
	orphanM.setFather(dad);

	MainWindowSwing mainWin = new MainWindowSwing();
	mainWin.setTitle("Œgen"); //$NON-NLS-1$
	mainWin.setSize(800, 600);

	mainWin.addWindowListener(new WindowListener() {

	    @Override
	    public void windowActivated(WindowEvent e) {
		// TODO Stub de la méthode généré automatiquement

	    }

	    @Override
	    public void windowClosed(WindowEvent e) {
		// TODO Stub de la méthode généré automatiquement

	    }

	    @Override
	    public void windowClosing(WindowEvent e) {
		try (FileOutputStream fos = new FileOutputStream("oegen.xml")) {
		    Exporter exp = new Exporter (fos);
		    exp.doExport();
		} catch (FileNotFoundException e1) {
		    // TODO Bloc catch généré automatiquement
		    e1.printStackTrace();
		} catch (IOException e1) {
		    // TODO Bloc catch généré automatiquement
		    e1.printStackTrace();
		}

		System.exit(0);
	    }

	    @Override
	    public void windowDeactivated(WindowEvent e) {
		// TODO Stub de la méthode généré automatiquement

	    }

	    @Override
	    public void windowDeiconified(WindowEvent e) {
		// TODO Stub de la méthode généré automatiquement

	    }

	    @Override
	    public void windowIconified(WindowEvent e) {
		// TODO Stub de la méthode généré automatiquement

	    }

	    @Override
	    public void windowOpened(WindowEvent e) {
		// TODO Stub de la méthode généré automatiquement

	    }
	});
	mainWin.setVisible(true);

    }

}

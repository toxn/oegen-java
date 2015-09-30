/**
 *
 */
package ctrl;

import data.Person;
import ui.swing.MainWindow;

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
	Person dad = new Person();
	dad.setFirstName("perroquet");
	dad.setLastName("oiseau");

	Person mom = new Person();
	mom.setFirstName("mésange");
	mom.setLastName("geai");

	Person me = new Person();
	me.setFirstName("moineau");
	me.setLastName(dad.getLastName());
	me.setFather(dad);
	me.setMother(mom);

	Person alouette = new Person();
	alouette.setFirstName("alouette");
	alouette.setLastName("oiseau");
	alouette.setFather(dad);
	alouette.setMother(mom);

	Person pélican = new Person();
	pélican.setFirstName("Pélican");
	pélican.setLastName("Oiseau");
	pélican.setMother(mom);
	pélican.setFather(dad);

	Person orphanF = new Person();
	orphanF.setFirstName("Orphan");
	orphanF.setLastName("Of Father");
	orphanF.setMother(mom);

	Person orphanM = new Person();
	orphanM.setFirstName("Orphan");
	orphanM.setLastName("Of Mother");
	orphanM.setFather(dad);

	MainWindow mainWin = new MainWindow();
	mainWin.setTitle("Œgen");
	mainWin.setSize(800, 600);

	mainWin.setVisible(true);

    }

}

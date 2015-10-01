/**
 *
 */
package ctrl;

import data.Person;
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
	oldpa1.setFirstName("Old'pa");
	oldpa1.setLastName("Oiseau");

	Person oldma1 = new Person();
	oldma1.setFirstName("Old'ma");
	oldma1.setLastName("Oiseau");

	Person oldpa2 = new Person();
	oldpa2.setFirstName("Old'pa");
	oldpa2.setLastName("Oiselle");

	Person oldma2 = new Person();
	oldma2.setFirstName("Old'ma");
	oldma2.setLastName("Oiselle");

	Person oldpa3 = new Person();
	oldpa3.setFirstName("Old'pa");
	oldpa3.setLastName("Geai");

	Person oldma3 = new Person();
	oldma3.setFirstName("Old'ma");
	oldma3.setLastName("Geai");

	Person oldpa4 = new Person();
	oldpa4.setFirstName("Old'pa");
	oldpa4.setLastName("Mésange");

	Person oldma4 = new Person();
	oldma4.setFirstName("Old'ma");
	oldma4.setLastName("Mésange");

	Person grandpa = new Person();
	grandpa.setFirstName("Grand'pa");
	grandpa.setLastName("Oiseau");
	grandpa.setFather(oldpa1);
	grandpa.setMother(oldma1);

	Person grandma = new Person();
	grandma.setFirstName("Granny");
	grandma.setLastName("Oiselle");
	grandma.setFather(oldpa2);
	grandma.setMother(oldma2);

	Person ara = new Person();
	ara.setFirstName("Ara");
	ara.setLastName("Oiseau");
	ara.setFather(grandpa);
	ara.setMother(grandma);

	Person grandpa2 = new Person();
	grandpa2.setFirstName("Grand'pa");
	grandpa2.setLastName("Geai");
	grandpa2.setFather(oldpa3);
	grandpa2.setMother(oldma3);

	Person grandma2 = new Person();
	grandma2.setFirstName("Granny");
	grandma2.setLastName("Mésange");
	grandma2.setFather(oldpa4);
	grandma2.setMother(oldma4);

	Person dad = new Person();
	dad.setFirstName("perroquet");
	dad.setLastName("oiseau");
	dad.setFather(grandpa);
	dad.setMother(grandma);

	Person mom = new Person();
	mom.setFirstName("mésange");
	mom.setLastName("geai");
	mom.setFather(grandpa2);
	mom.setMother(grandma2);

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

	MainWindowSwing mainWin = new MainWindowSwing();
	mainWin.setTitle("Œgen");
	mainWin.setSize(800, 600);

	mainWin.setVisible(true);

    }

}

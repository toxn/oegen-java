/**
 *
 */
package com.cdbs.oegen.ctrl;

import com.cdbs.oegen.ui.swing.MainWindowSwing;

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
	MainWindowSwing mainWin = new MainWindowSwing();
	mainWin.buildUI();

	mainWin.setTitle("\u0152gen"); //$NON-NLS-1$
	mainWin.setSize(800, 600);

	mainWin.setVisible(true);

    }

}

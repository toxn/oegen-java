package fr.cdbs.oegen.ctrl

import fr.cdbs.oegen.ui.swing.MainWindowSwing

/**
 *
 */

/**
 *
 */
/**
 * @author toxn
 */
object Main {
    /**
     * @param args
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val mainWin = MainWindowSwing()
        mainWin.buildUI()

        mainWin.setTitle("\u0152gen") //$NON-NLS-1$
        mainWin.setSize(800, 600)

        mainWin.setVisible(true)
    }
}
/**
 *
 */
package com.cdbs.oegen.ui.swing;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import com.cdbs.oegen.ui.Messages;

/**
 * @author toxn
 *
 */
public abstract class CustomAction extends AbstractAction {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final String SUFFIX_LONGDESC = ".LongDescription"; //$NON-NLS-1$
    private static final String SUFFIX_MNEMONIC = ".Mnemonic"; //$NON-NLS-1$
    private static final String SUFFIX_NAME = ".Name"; //$NON-NLS-1$
    private static final String PREFIX = "MainWindowSwing.Action."; //$NON-NLS-1$
    private static final String SUFFIX_CONFIRMATION = ".Confirmation"; //$NON-NLS-1$
    private final String name;
    private final boolean confirm;

    public CustomAction(String actionName) {
	name = actionName;
	confirm = false;
    }

    public CustomAction(String actionName, boolean confirm) {
	name = actionName;
	this.confirm = confirm;
    }

    public final String getActionCommand() {
	return name.toUpperCase();
    }

    @Override
    public Object getValue(String key) {
	switch (key) {
	case Action.NAME:
	    return Messages.getString(PREFIX + name + SUFFIX_NAME);

	case Action.MNEMONIC_KEY:
	    try {
		return KeyStroke.getKeyStroke(Messages.getString(PREFIX + name + SUFFIX_MNEMONIC))
			.getKeyCode();
	    } catch (@SuppressWarnings("unused") Exception e) {
		return null;
	    }

	case Action.ACTION_COMMAND_KEY:
	    return getActionCommand();

	case Action.LONG_DESCRIPTION:
	    return Messages.getString(PREFIX + name + SUFFIX_LONGDESC)
		    + (confirm ? "\n" + Messages.getString(PREFIX + "Any" + SUFFIX_CONFIRMATION) : ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	case Action.ACCELERATOR_KEY:
	    try {
		return KeyStroke.getKeyStroke(Messages.getString(PREFIX + name + SUFFIX_MNEMONIC));
	    } catch (@SuppressWarnings("unused") Exception e) {
		return null;
	    }

	default:
	    return super.getValue(key);
	}
    }
}

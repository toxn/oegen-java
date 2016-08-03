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
    public static final int FLAG_CONFIRM = 1 << 0;
    public static final int FLAG_REQUEST = 1 << 1;
    private static final String PROPERTY_CONFIRM = "Confirm"; //$NON-NLS-1$
    private static final String PROPERTY_REQUEST = "Request"; //$NON-NLS-1$

    private final String m_ActionName;
    private final boolean m_Confirm;
    private boolean m_Request;

    public CustomAction(String actionName) {
	m_ActionName = actionName;
	m_Confirm = false;
	m_Request = false;
    }

    public CustomAction(String actionName, int flags) {
	m_ActionName = actionName;
	m_Confirm = (flags & FLAG_CONFIRM) != 0;
	m_Request = (flags & FLAG_REQUEST) != 0;
    }

    public final String getActionCommand() {
	return m_ActionName.toUpperCase();
    }

    public int getFlags() {
	return (m_Confirm ? FLAG_CONFIRM : 0) | (m_Request ? FLAG_REQUEST : 0);
    }

    @Override
    public Object getValue(String key) {
	switch (key) {
	case Action.NAME:
	    return Messages.getString(PREFIX + m_ActionName + SUFFIX_NAME) + (m_Request ? "\u2026" : ""); //$NON-NLS-1$ //$NON-NLS-2$

	case Action.MNEMONIC_KEY:
	    try {
		return KeyStroke.getKeyStroke(Messages.getString(PREFIX + m_ActionName + SUFFIX_MNEMONIC))
			.getKeyCode();
	    } catch (@SuppressWarnings("unused") Exception e) {
		return null;
	    }

	case Action.ACTION_COMMAND_KEY:
	    return getActionCommand();

	case Action.LONG_DESCRIPTION:
	    return Messages.getString(PREFIX + m_ActionName + SUFFIX_LONGDESC)
		    + (m_Confirm ? "\n" + Messages.getString(PREFIX + "Any" + SUFFIX_CONFIRMATION) : ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	case Action.ACCELERATOR_KEY:
	    try {
		return KeyStroke.getKeyStroke(Messages.getString(PREFIX + m_ActionName + SUFFIX_MNEMONIC));
	    } catch (@SuppressWarnings("unused") Exception e) {
		return null;
	    }

	case PROPERTY_CONFIRM:
	    return hasConfirm();

	case PROPERTY_REQUEST:
	    return hasRequest();

	default:
	    return super.getValue(key);
	}
    }

    public boolean hasConfirm() {
	return m_Confirm;
    }

    public boolean hasRequest() {
	return m_Request;
    }

    public void setRequest(boolean request) {
	boolean oldValue;
	String oldName;

	if(m_Request != request) {
	    oldValue = m_Request;
	    oldName = (String) getValue(Action.NAME);

	    m_Request = request;

	    super.firePropertyChange(PROPERTY_REQUEST, oldValue, m_Request);
	    super.firePropertyChange(Action.NAME, oldName, getValue(Action.NAME));
	}
    }
}

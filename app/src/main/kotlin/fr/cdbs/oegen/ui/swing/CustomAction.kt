package fr.cdbs.oegen.ui.swing

import fr.cdbs.oegen.ui.Messages
import javax.swing.AbstractAction
import javax.swing.Action
import javax.swing.KeyStroke

abstract class CustomAction(
    private val actionName: String,
    flags: Int = 0,
) : AbstractAction() {
    private val confirm = flags and FLAG_CONFIRM != 0
    private var request = flags and FLAG_REQUEST != 0

    fun getActionCommand(): String = actionName.uppercase()

    fun getFlags(): Int = (if (confirm) FLAG_CONFIRM else 0) or (if (request) FLAG_REQUEST else 0)

    override fun getValue(key: String?): Any? = when (key) {
        Action.NAME ->
            Messages.getString(PREFIX + actionName + SUFFIX_NAME) + if (request) "\u2026" else ""

        Action.MNEMONIC_KEY ->
            runCatching {
                KeyStroke.getKeyStroke(Messages.getString(PREFIX + actionName + SUFFIX_MNEMONIC))?.keyCode
            }.getOrNull()

        Action.ACTION_COMMAND_KEY -> getActionCommand()

        Action.LONG_DESCRIPTION ->
            Messages.getString(PREFIX + actionName + SUFFIX_LONGDESC) +
                if (confirm) "\n" + Messages.getString(PREFIX + "Any" + SUFFIX_CONFIRMATION) else ""

        Action.ACCELERATOR_KEY ->
            runCatching {
                KeyStroke.getKeyStroke(Messages.getString(PREFIX + actionName + SUFFIX_MNEMONIC))
            }.getOrNull()

        PROPERTY_CONFIRM -> hasConfirm()
        PROPERTY_REQUEST -> hasRequest()
        else -> super.getValue(key)
    }

    fun hasConfirm(): Boolean = confirm

    fun hasRequest(): Boolean = request

    fun setRequest(request: Boolean) {
        if (this.request != request) {
            val oldValue = this.request
            val oldName = getValue(Action.NAME)
            this.request = request
            firePropertyChange(PROPERTY_REQUEST, oldValue, this.request)
            firePropertyChange(Action.NAME, oldName, getValue(Action.NAME))
        }
    }

    companion object {
        private const val SUFFIX_LONGDESC = ".LongDescription"
        private const val SUFFIX_MNEMONIC = ".Mnemonic"
        private const val SUFFIX_NAME = ".Name"
        private const val PREFIX = "Action."
        private const val SUFFIX_CONFIRMATION = ".Confirmation"
        const val FLAG_CONFIRM = 1 shl 0
        const val FLAG_REQUEST = 1 shl 1
        private const val PROPERTY_CONFIRM = "Confirm"
        private const val PROPERTY_REQUEST = "Request"
    }
}

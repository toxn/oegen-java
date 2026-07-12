package fr.cdbs.oegen.ui

import java.text.MessageFormat
import java.util.*

object Messages {
    private const val BUNDLE_NAME = "fr.cdbs.oegen.ui.messages" //$NON-NLS-1$

    private val RESOURCE_BUNDLE: ResourceBundle = ResourceBundle.getBundle(BUNDLE_NAME)

    @JvmStatic
    fun getString(key: String): String {
        try {
            return RESOURCE_BUNDLE.getString(key)
        } catch (e: MissingResourceException) {
            return '!'.toString() + key + '!'
        }
    }

    @JvmStatic
    fun getString(key: String, vararg args: Any?): String {
        return MessageFormat.format(getString(key), *args)
    }
}

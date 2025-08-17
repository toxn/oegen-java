package fr.cdbs.oegen.data.io.xml

import java.util.*

object Messages {
    private const val BUNDLE_NAME = "fr.cdbs.oegen.data.io.xml.messages" //$NON-NLS-1$

    private val RESOURCE_BUNDLE: ResourceBundle = ResourceBundle.getBundle(BUNDLE_NAME)

    fun getString(key: String): String {
        try {
            return RESOURCE_BUNDLE.getString(key)
        } catch (e: MissingResourceException) {
            return "!$key!"
        }
    }
}

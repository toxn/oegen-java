package fr.cdbs.oegen.data.io

import java.util.MissingResourceException
import java.util.ResourceBundle

object IoMessages {
    private const val BUNDLE_NAME = "fr.cdbs.oegen.data.io.messages"

    private val RESOURCE_BUNDLE: ResourceBundle = ResourceBundle.getBundle(BUNDLE_NAME)

    fun getString(key: String): String {
        return try {
            RESOURCE_BUNDLE.getString(key)
        } catch (_: MissingResourceException) {
            "!$key!"
        }
    }
}

package com.cdbs.oegen.data.io.xml;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
    private static final String BUNDLE_NAME = "com.cdbs.oegen.data.io.xml.messages"; //$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    public static String getString(String key) {
	try {
	    return RESOURCE_BUNDLE.getString(key);
	} catch (@SuppressWarnings("unused") MissingResourceException e) {
	    return '!' + key + '!';
	}
    }

    private Messages() {
    }
}

/**
 * 
 */
package com.cdbs.oegen.ui;

import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
 * @author toxn
 *
 */
public class TranscriptionViewFactory
implements ViewFactory
{

	/* (non-Javadoc)
	 * @see javax.swing.text.ViewFactory#create(javax.swing.text.Element)
	 */
	@Override
	public View create(Element elem)
	{
		return new TranscriptionView(elem);
	}

}

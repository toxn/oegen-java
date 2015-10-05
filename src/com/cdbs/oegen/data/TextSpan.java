/**
 * 
 */
package com.cdbs.oegen.data;

/**
 * @author toxn
 *
 */
public class TextSpan
extends Span
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String text;

	/* (non-Javadoc)
	 * @see com.cdbs.oegen.data.Span#toString()
	 */
	@Override
	public String toString()
	{
		return text;
	}

}

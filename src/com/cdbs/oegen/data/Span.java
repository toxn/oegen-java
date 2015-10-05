/**
 * 
 */
package com.cdbs.oegen.data;


/**
 * Represents a textual span of a source.
 * 
 * @author toxn
 * 
 */
/* @model */
public abstract class Span
extends Oebject
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Reference reference;

	public Span()
	{
		super(null, null, null);
	}

	@Override
	public void finalize()
		throws Throwable
		{
		super.finalize();
		}

	@Override
	public abstract String toString();
}

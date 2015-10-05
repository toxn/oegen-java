/**
 * 
 */
package com.cdbs.oegen.data;

/**
 * @author toxn
 *
 */
/* @model */
public abstract class Reference
extends Oebject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Source source;

	public Span transcription;

	public Reference()
	{
		super(null, null, null);
	}

	@Override
	protected void finalize()
		throws Throwable
		{
		if (source != null)
		{
			source.content.remove(this);
			source = null;
		}

		if (transcription != null)
		{
			transcription.reference = null;
			transcription = null;
		}

		super.finalize();
		}

	/**
	 * @return the source
	 */
	public Source getSource()
	{
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(Source src)
	{
		if (src != source)
		{
			source.content.remove(this);
			source = src;
			src.content.add(this);
		}
	}
}

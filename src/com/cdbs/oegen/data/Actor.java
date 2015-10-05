/**
 * 
 */
package com.cdbs.oegen.data;


/**
 * An Actor is a person cited in a Source.
 * 
 * The person may or may not take part in an event. An Actor record must be
 * created as long as it's name is cited.
 * 
 * @author toxn
 * 
 */
public final class Actor
extends Reference
{
	public static enum NameType
	{
		Other,
		First,
		Last,
		Nick,
		Pronoun
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Text of the name
	 */
	public String text;

	public NameType type;
	
	public CustomNameType cusomType;
}

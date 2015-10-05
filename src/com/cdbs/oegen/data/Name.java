/**
 * 
 */
package com.cdbs.oegen.data;

/**
 * @author toxn
 *
 */
public class Name
	extends Reference
{
	public static enum NameType
	{
		Other,
		Full,
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

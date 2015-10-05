/**
 * 
 */
package com.cdbs.oegen.data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author toxn
 *
 */
public class Database
extends Oebject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Universe universe;

	public Set<Oebject> oebjects = new HashSet<Oebject>();

	public Set<Person> persons = new HashSet<Person>();

	public Set<Source> sources = new HashSet<Source>();

	public Set<Reference> references = new HashSet<Reference>();

	public Database()
	{
		super(null, null, null);
	}
}

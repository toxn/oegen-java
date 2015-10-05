/**
 * 
 */
package com.cdbs.oegen.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Contains all the objects still living (including databases).
 * 
 * @author toxn
 * 
 */
public class Universe
implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static Universe universe = new Universe();

	public Set<Database> databases = new HashSet<Database>();

	public Map<UUID, Oebject> oebjects = new HashMap<>();

	public Set<Person> persons = new HashSet<Person>();

	public Set<Source> sources = new HashSet<Source>();

	public Set<Reference> references = new HashSet<Reference>();



}

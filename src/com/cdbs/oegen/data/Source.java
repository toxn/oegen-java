/**
 * 
 */
package com.cdbs.oegen.data;

import java.util.HashSet;

/**
 * @author toxn
 *
 */
/* @model */
public class Source
extends Reference
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * All the objects directly refered to in this source (objects refered to in
	 * sub-sources must not be placed here).
	 */
	public HashSet<Reference> content;
}

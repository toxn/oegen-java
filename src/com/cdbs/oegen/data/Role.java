/**
 * 
 */
package com.cdbs.oegen.data;

/**
 * An actor assuming a function in an event.
 * 
 * @author toxn
 * 
 */
/* @model */
public class Role
extends Reference
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Event event;

	public Actor actor;

	public Function function;

}

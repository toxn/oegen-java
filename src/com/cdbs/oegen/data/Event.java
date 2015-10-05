/**
 * 
 */
package com.cdbs.oegen.data;

/**
 * An event described or referred to in a source.
 * 
 * One source can describe multiple events.
 * 
 * @author toxn
 * 
 */
/* @model */
public class Event
extends Reference
{
	public enum EventType
	{
		Other,
		Birth,
		Baptism,
		Adoption,
		Engagement,
		Wedding,
		Divorce,
		Death,
		Burial
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EventType type;

	public CustomEventType customType;
}

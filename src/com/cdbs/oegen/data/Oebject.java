package com.cdbs.oegen.data;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author toxn
 *
 */
public abstract class Oebject
implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * Text used for display in various UI elements like lists, combos, etc.
	 * 
	 * @model
	 */
	public String displayName;

	/**
	 * User comments
	 * 
	 * @model
	 */
	public String comment;

	/**
	 * a unique identifier
	 * 
	 * @model
	 */
	private UUID uid;

	/**
	 * 
	 * @param uid
	 *            a unique identifier or null to create one. uid will be checked
	 *            for unicity.
	 * @param displayName
	 *            a String containing the text (supposedly short) used to
	 *            represent the object in the various UI elements.
	 * @param comment
	 *            a String containing all manners of user comments regarding
	 *            this object.
	 */
	public Oebject(UUID uid, String displayName, String comment)
	{
		super();

		setUID(uid);
		this.displayName = displayName;
		this.comment = comment;
	}

	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
	}

	/**
	 * @return the unique identifier
	 */
	public UUID getUID()
	{
		return uid;
	}

	/**
	 * @param uid
	 *            the unique identifier to set.
	 * 
	 *            <p>
	 *            A new identifier will be created either if uid is null or if
	 *            it is already associated with another oebject.
	 *            </p>
	 */
	public void setUID(UUID uid)
	{
		if (uid == null || Universe.universe.oebjects.containsKey(uid))
		{
			this.uid = UUID.randomUUID();
		}
		else
		{
			this.uid = uid;
		}
	}
}

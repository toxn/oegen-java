/**
 * 
 */
package com.cdbs.oegen.data;

/**
 * @author toxn
 *
 */
/* @model */
public abstract class Association<R1 extends Reference, R2 extends Reference>
extends Reference
{
	public enum AssociationType
	{
		Other,
		isSame,
		actsAs,
		bloodRelated,
		cousin,
		child,
		adoptedBy,
		grandChild
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public R1 source;

	public AssociationType type;

	public CustomAssociationType customType;

	public R2 target;
}

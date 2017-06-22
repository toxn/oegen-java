/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cdbs.oegen.data.date;

import com.cdbs.oegen.ui.Messages;

/**
 *
 * @author toxn
 */
public class UnaryOperator extends ComplexDate {
    public enum Operator {
	Not,
	Before,
	After,
	Circa;
    }

    public Operator operator;
    public Date date;

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	UnaryOperator other = (UnaryOperator) obj;
	if (date == null) {
	    if (other.date != null)
		return false;
	} else if (!date.equals(other.date))
	    return false;
	if (operator != other.operator)
	    return false;
	return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (operator == null ? 0 : operator.hashCode());
	result = prime * result + (date == null ? 0 : date.hashCode());
	return result;
    }

    @Override
    public String toString() {
	String result = ""; //$NON-NLS-1$

	switch (operator) {
	case Not:
	    result += Messages.getString("UnaryOperator.Not"); //$NON-NLS-1$
	    break;

	case Before:
	    result += "< "; //$NON-NLS-1$
	    break;

	case After:
	    result += "> "; //$NON-NLS-1$
	    break;

	case Circa:
	    result += "≈ "; //$NON-NLS-1$
	    break;
	}

	return result + date.toString();
    }
}

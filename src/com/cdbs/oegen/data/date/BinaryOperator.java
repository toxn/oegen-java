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
public class BinaryOperator {
    public enum Operator {
	And, Or, Xor;
    }

    public Date date1;

    public Operator operator;

    public Date date2;
    /* (non-Javadoc)
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
	BinaryOperator other = (BinaryOperator) obj;
	if (date1 == null) {
	    if (other.date1 != null)
		return false;
	} else if (!date1.equals(other.date1))
	    return false;
	if (date2 == null) {
	    if (other.date2 != null)
		return false;
	} else if (!date2.equals(other.date2))
	    return false;
	if (operator != other.operator)
	    return false;
	return true;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (date1 == null ? 0 : date1.hashCode());
	result = prime * result + (date2 == null ? 0 : date2.hashCode());
	result = prime * result + (operator == null ? 0 : operator.hashCode());
	return result;
    }

    @Override
    public String toString() {
	String result = ""; //$NON-NLS-1$

	if (operator == Operator.Xor) {
	    result += Messages.getString("BinaryOperator.XorEither"); //$NON-NLS-1$
	}

	result += date1.toString();

	switch (operator) {
	case And:
	    result += Messages.getString("BinaryOperator.And"); //$NON-NLS-1$
	    break;

	case Or:
	    result += Messages.getString("BinaryOperator.Or"); //$NON-NLS-1$
	    break;

	case Xor:
	    result += Messages.getString("BinaryOperator.XorOr"); //$NON-NLS-1$
	    break;
	}

	result += date2.toString();

	return result;
    }
}

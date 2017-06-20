/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cdbs.oegen.data.date;

import java.beans.PropertyChangeSupport;

/**
 *
 * @author toxn
 */
public abstract class RomanDate extends SimpleDate {
    public static final String PROP_DAY = "DAY";
    public static final String PROP_MONTH = "MONTH";
    public static final String PROP_YEAR = "YEAR";

    public static final int DAY_UNKNOWN = 0;

    public static final int MONTH_UNKNOWN = 0;

    public static final int YEAR_UNKNOWN = Integer.MIN_VALUE;

    private int m_Day;

    private int m_Month;

    private int m_Year;

    private final transient PropertyChangeSupport propertyChangeSupport = new java.beans.PropertyChangeSupport(this);

    public RomanDate(int day, int month, int year) throws Exception {
	setDate(day, month, year);
    }

    /**
     * @return day number or DAY_UNKOWN
     */
    public int getDay() {
	return m_Day;
    }

    /**
     * @return the month or MONTH_UNKNOWN
     */
    public int getMonth() {
	return m_Month;
    }

    /**
     * @return the m_Year
     */
    public int getYear() {
	return m_Year;
    }

    /**
     * @param day the m_Day to set
     * @throws java.lang.Exception Invalid date.
     */
    public void setDay(int day) throws Exception {
	setDate(day, m_Month, m_Year);
    }

    /**
     * @param month the month to set
     */
    public void setMonth(int month) {
	int oldMonth = m_Month;
	m_Month = month;
	propertyChangeSupport.firePropertyChange(PROP_MONTH, oldMonth, m_Month);
    }

    /**
     * @param m_Year the m_Year to set
     */
    public void setYear(int year) throws Exception {
	setDate(m_Day, m_Month, year);
    }

    @Override
    public
    String toString() {
	String precision = "";

	switch(this.precision) {
	case Exact:
	    precision = "";
	    break;

	case After:
	    precision = "> ";
	    break;

	case Before:
	    precision = "< ";

	case Circa:
	    precision = "≈ ";
	}

	return String.format("%1$ts%2ts%3$ts-%4$ts-%5$ts", precision, getLetter(),
		getYear() == YEAR_UNKNOWN ? "????" : Integer.toString(getYear()),
			getMonth() == MONTH_UNKNOWN ? "??" : String.format("%1$2ts", getMonth()),
				getDay() == DAY_UNKNOWN ? "??" : String.format("%1$2ts", getDay()));
    }

    private void setDate(int day, int month, int year) throws Exception {
	if (month > 12 || month < 0)
	    throw new Exception("Illegal month value");

	if (day > 31 || day < 0)
	    throw new Exception("Illegal day value");

	switch (month) {
	case MONTH_UNKNOWN:
	    break;

	case 2:
	    if (isLeapYear(year) && day > 29)
		throw new Exception();
	    else if (day > 28)
		throw new Exception();
	    break;

	case 4:
	    // Fallthrough
	case 6:
	    // Fallthrough
	case 9:
	    // Fallthrough
	case 11:
	    if (day > 30)
		throw new Exception();
	}

	if (day != m_Day) {
	    int oldDay = m_Day;
	    m_Day = day;
	    propertyChangeSupport.firePropertyChange(PROP_DAY, oldDay, m_Day);
	}

	if (month != m_Month) {
	    int oldMonth = m_Month;
	    m_Month = month;
	    propertyChangeSupport.firePropertyChange(PROP_MONTH, oldMonth, m_Month);
	}

	if (year != m_Year) {
	    int oldYear = m_Year;
	    m_Year = year;
	    propertyChangeSupport.firePropertyChange(PROP_YEAR, oldYear, m_Year);
	}
    }

    abstract protected String getLetter();

    protected abstract boolean isLeapYear(int year);
}

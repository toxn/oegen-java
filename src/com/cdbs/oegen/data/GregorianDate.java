/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cdbs.oegen.data;

import java.beans.PropertyChangeSupport;

/**
 *
 * @author toxn
 */
public class GregorianDate {

    public static final String PROP_DAY = "DAY";
    public static final String PROP_MONTH = "MONTH";
    public static final String PROP_YEAR = "YEAR";

    private int m_Day;

    private static final int DAY_UNKNOWN = 0;

    private int m_Month;

    private static final int MONTH_UNKNOWN = 0;

    private int m_Year;

    private static final int YEAR_UNKNOWN = Integer.MIN_VALUE;

    private final transient PropertyChangeSupport propertyChangeSupport = new java.beans.PropertyChangeSupport(this);

    public GregorianDate(int day, int Month, int year) throws Exception {
        if (Month > 12 || Month < 0) {
            throw new Exception();
        }

        if (day > 31 || day < 0) {
            throw new Exception();
        }

        switch (Month) {
            case MONTH_UNKNOWN:
                break;

            case 2:
                if ((year % 4 == 0) && (year % 100 != 0) && (day > 29)) {
                    throw new Exception();
                } else if (day > 28) {
                    throw new Exception();
                }
                break;

            case 4:
            // Fallthrough
            case 6:
            // Fallthrough
            case 9:
            // Fallthrough
            case 11:
                if (day > 30) {
                    throw new Exception();
                }
        }

        m_Year = year;
        m_Month = Month;
        m_Day = day;
    }

    /**
     * @return day number or DAY_UNKOWN
     */
    public int getDay() {
        return m_Day;
    }

    /**
     * @param m_Day the m_Day to set
     */
    public void setDay(int day) {
        int oldDay = this.m_Day;
        this.m_Day = day;
        propertyChangeSupport.firePropertyChange(PROP_DAY, oldDay, m_Day);
    }

    /**
     * @return the month or MONTH_UNKNOWN
     */
    public int getMonth() {
        return m_Month;
    }

    /**
     * @param m_Month the m_Month to set
     */
    public void setMonth(int month) {
        int oldMonth = this.m_Month;
        this.m_Month = month;
        propertyChangeSupport.firePropertyChange(PROP_MONTH, oldMonth, m_Month);
    }

    /**
     * @return the m_Year
     */
    public int getYear() {
        return m_Year;
    }

    /**
     * @param m_Year the m_Year to set
     */
    public void setYear(int year) {
        int oldYear = this.m_Year;
        this.m_Year = year;
        propertyChangeSupport.firePropertyChange(PROP_YEAR, oldYear, m_Year);
    }
}

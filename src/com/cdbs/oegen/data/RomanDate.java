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
public class RomanDate {
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
     * @param day the m_Day to set
     * @throws java.lang.Exception Invalid date.
     */
    public void setDay(int day) throws Exception {
        setDate(day, m_Month, m_Year);
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
    public void setYear(int year) throws Exception {
        setDate(m_Day, m_Month, year);
    }

    private void setDate(int day, int month, int year) throws Exception {
        if (month > 12 || month < 0) {
            throw new Exception("Illegal month value");
        }

        if (day > 31 || day < 0) {
            throw new Exception("Illegal day value");
        }

        switch (month) {
            case MONTH_UNKNOWN:
                break;

            case 2:
                if ((year == YEAR_UNKNOWN) || ((year % 4 == 0) && (year % 100 != 0)) && (day > 29)) {
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

        if (day != m_Day) {
            int oldDay = this.m_Day;
            this.m_Day = day;
            propertyChangeSupport.firePropertyChange(PROP_DAY, oldDay, m_Day);
        }

        if (month != this.m_Month) {
            int oldMonth = this.m_Month;
            this.m_Month = month;
            propertyChangeSupport.firePropertyChange(PROP_MONTH, oldMonth, m_Month);
        }

        if (year != m_Year) {
            int oldYear = this.m_Year;
            this.m_Year = year;
            propertyChangeSupport.firePropertyChange(PROP_YEAR, oldYear, m_Year);
        }
    }
}

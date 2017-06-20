/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cdbs.oegen.data.date;

/**
 *
 * @author toxn
 */
public class GregorianDate extends RomanDate {

    public GregorianDate(int day, int month, int year) throws Exception {
	super(day, month, year);
    }

    @Override
    protected String getLetter() {
	return "G";
    }

    @Override
    protected boolean isLeapYear(int year) {
	return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    @Override
    @SuppressWarnings("fallthrough")
    int toDaysFromZero() throws Exception {
	// FIXME: fonctionne uniquement à partir de l'an 1

	if (getDay() == DAY_UNKNOWN
		|| getMonth() == MONTH_UNKNOWN
		|| getYear() == YEAR_UNKNOWN)
	    throw new Exception();

	int result = 0;

	for (int i = 1; i < getYear(); i++) {
	    result += isLeapYear(i) ? 366 : 365;
	}

	switch (getMonth()) {
	case 12:
	    result += 30;
	    // Fallthrough

	case 11:
	    result += 31;
	    // Fallthrough

	case 10:
	    result += 30;
	    // Fallthrough

	case 9:
	    result += 31;
	    // Fallthrough

	case 8:
	    result += 30;
	    // Fallthrough

	case 7:
	    result += 31;
	    // Fallthrough

	case 6:
	    result += 31;
	    // Fallthrough

	case 5:
	    result += 30;
	    // Fallthrough

	case 4:
	    result += 31;
	    // Fallthrough

	case 3:
	    result += isLeapYear(getYear()) ? 29 : 28;
	    // Fallthrough

	case 2:
	    result += 31;
	    // Fallthrough

	case 1:
	    result += getDay();
	}

	return result;
    }
}

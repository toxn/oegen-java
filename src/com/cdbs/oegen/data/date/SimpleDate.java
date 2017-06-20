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
public abstract class SimpleDate extends Date {
    public enum Precision {
	Exact, Before, Circa, After
    }

    public Precision precision = Precision.Exact;

    abstract int toDaysFromZero() throws Exception;

}

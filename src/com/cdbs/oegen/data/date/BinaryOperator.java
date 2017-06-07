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
public class BinaryOperator {
    public enum Operator {
        And,
        Or,
        Xor;
    }

    public Date date1;
    public Operator operator;
    public Date date2;
}

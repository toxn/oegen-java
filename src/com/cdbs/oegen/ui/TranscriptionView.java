/**
 * 
 */
package com.cdbs.oegen.ui;

import java.awt.Graphics;
import java.awt.Shape;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Position.Bias;
import javax.swing.text.View;

/**
 * @author toxn
 *
 */
public class TranscriptionView
extends View
{
	public TranscriptionView(Element elem)
	{
		super(elem);
	}

	/* (non-Javadoc)
	 * @see javax.swing.text.View#getPreferredSpan(int)
	 */
	@Override
	public float getPreferredSpan(int arg0)
	{
		return getMaximumSpan(arg0);
	}

	/* (non-Javadoc)
	 * @see javax.swing.text.View#modelToView(int, java.awt.Shape, javax.swing.text.Position.Bias)
	 */
	@Override
	public Shape modelToView(int arg0, Shape arg1, Bias arg2)
		throws BadLocationException
		{
		// TODO Auto-generated method stub
		return null;
		}

	/* (non-Javadoc)
	 * @see javax.swing.text.View#paint(java.awt.Graphics, java.awt.Shape)
	 */
	@Override
	public void paint(Graphics arg0, Shape arg1)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.swing.text.View#viewToModel(float, float, java.awt.Shape, javax.swing.text.Position.Bias[])
	 */
	@Override
	public int viewToModel(float arg0, float arg1, Shape arg2, Bias[] arg3)
	{
		// TODO Auto-generated method stub
		return 0;
	}

}

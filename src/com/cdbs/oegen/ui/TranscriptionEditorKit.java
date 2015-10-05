/**
 * 
 */
package com.cdbs.oegen.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import javax.swing.Action;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.ViewFactory;

/**
 * @author toxn
 *
 */
public class TranscriptionEditorKit
extends EditorKit
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see javax.swing.text.EditorKit#createCaret()
	 */
	@Override
	public Caret createCaret()
	{
		return new DefaultCaret();
	}

	/* (non-Javadoc)
	 * @see javax.swing.text.EditorKit#createDefaultDocument()
	 */
	@Override
	public Document createDefaultDocument()
	{
		return new TranscriptionDocument();
	}

	/* (non-Javadoc)
	 * @see javax.swing.text.EditorKit#getActions()
	 */
	@Override
	public Action[] getActions()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.text.EditorKit#getContentType()
	 */
	@Override
	public String getContentType()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.text.EditorKit#getViewFactory()
	 */
	@Override
	public ViewFactory getViewFactory()
	{
		return new TranscriptionViewFactory();
	}

	/* (non-Javadoc)
	 * @see javax.swing.text.EditorKit#read(java.io.InputStream, javax.swing.text.Document, int)
	 */
	@Override
	public void read(InputStream arg0, Document arg1, int arg2)
		throws IOException, BadLocationException
		{
		// TODO Auto-generated method stub

		}

	/* (non-Javadoc)
	 * @see javax.swing.text.EditorKit#read(java.io.Reader, javax.swing.text.Document, int)
	 */
	@Override
	public void read(Reader arg0, Document arg1, int arg2)
		throws IOException, BadLocationException
		{
		// TODO Auto-generated method stub

		}

	/* (non-Javadoc)
	 * @see javax.swing.text.EditorKit#write(java.io.OutputStream, javax.swing.text.Document, int, int)
	 */
	@Override
	public void write(OutputStream arg0, Document arg1, int arg2, int arg3)
		throws IOException, BadLocationException
		{
		// TODO Auto-generated method stub

		}

	/* (non-Javadoc)
	 * @see javax.swing.text.EditorKit#write(java.io.Writer, javax.swing.text.Document, int, int)
	 */
	@Override
	public void write(Writer arg0, Document arg1, int arg2, int arg3)
		throws IOException, BadLocationException
		{
		// TODO Auto-generated method stub

		}

}

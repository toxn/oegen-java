/**
 * 
 */
package com.cdbs.oegen.ui;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.Segment;

import com.cdbs.oegen.data.NodeSpan;
import com.cdbs.oegen.data.Span;
import com.cdbs.oegen.data.TextSpan;

/**
 * @author toxn
 *
 */
public final class TranscriptionDocument
implements Document
{
	public abstract class TranscriptionElement
	implements Element
	{
		protected TranscriptionNode parent = null;

		protected TranscriptionElement(TranscriptionNode parent)
		{
			this.parent = parent;
		}

		@Override
		protected void finalize()
			throws Throwable
			{
			if(parent != null)
			{
				parent.elements.remove(this);
				parent = null;
			}

			super.finalize();
			}

		@Override
		public AttributeSet getAttributes()
		{
			return null;
		}

		@Override
		public Document getDocument()
		{
			return TranscriptionDocument.this;
		}

		@Override
		public Element getParentElement()
		{
			return parent;
		}

		@Override
		public int getStartOffset()
		{
			int myIndex = parent.elements.indexOf(this);

			if (myIndex == 0)
				return parent.getStartOffset() + 1;

			return parent.elements.get(myIndex - 1).getEndOffset() + 1;
		}

		public abstract void insertString(int offset, String str)
			throws BadLocationException;

		public abstract void remove(int offs, int len)
			throws BadLocationException;
	}

	/**
	 * @author toxn
	 *
	 */
	public class TranscriptionLeaf
	extends TranscriptionElement
	{
		private TextSpan span;

		public TranscriptionLeaf(TranscriptionNode parent, TextSpan span)
		{
			super(parent);

			this.span = span;
		}

		@Override
		protected void finalize()
			throws Throwable
			{
			span = null;

			super.finalize();
			}

		/* (non-Javadoc)
		 * @see javax.swing.text.Element#getElement(int)
		 */
		@Override
		public Element getElement(int index)
		{
			return null;
		}

		/**
		 * @see javax.swing.text.Element#getElementCount()
		 */
		@Override
		public int getElementCount()
		{
			return 0;
		}

		/**
		 * @see javax.swing.text.Element#getElementIndex(int)
		 */
		@Override
		public int getElementIndex(int offset)
		{
			return -1;
		}

		/* (non-Javadoc)
		 * @see javax.swing.text.Element#getEndOffset()
		 */
		@Override
		public int getEndOffset()
		{
			return getStartOffset() + span.text.length();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.text.Element#getName()
		 */
		@Override
		public String getName()
		{
			return Messages.getString("TranscriptionDocument.Leaf"); //$NON-NLS-1$
		}

		@Override
		public void insertString(int offset, String str)
			throws BadLocationException
			{
			int start = getStartOffset();
			int end = getEndOffset();

			if (offset < start || offset > end)
				throw new BadLocationException(Messages.getString("Exception.offsetOutOfBound"), offset); //$NON-NLS-1$

			String startStr = span.text.substring(0, offset - start);
			String endStr = span.text.substring(offset - start + 1);
			span.text = startStr + str + endStr;
			}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.text.Element#isLeaf()
		 */
		@Override
		public boolean isLeaf()
		{
			return true;
		}

		@Override
		public void remove(int offs, int len)
			throws BadLocationException
			{
			int start = getStartOffset();
			int end = getEndOffset();

			int cutStart = offs - start;
			int cutEnd = cutStart + len;
			int textLen = span.text.length();

			if (cutEnd >= textLen)
			{
				cutEnd = textLen - 1;
			}

			if (cutStart < 0)
			{
				cutStart = 0;
			}

			if (offs > end || offs + len < start)
				return;

			span.text = span.text.substring(offs - start, cutEnd);

			if (textLen == 0)
			{
				try
				{
					span.finalize();
					span = null;
					finalize();
				}
				catch (Throwable e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			}

		@Override
		public String toString()
		{
			return span.toString();
		}

	}

	/**
	 * @author toxn
	 * 
	 */
	public class TranscriptionNode
	extends TranscriptionElement
	{
		protected NodeSpan span;

		final LinkedList<TranscriptionElement> elements = new LinkedList<>();

		public TranscriptionNode(TranscriptionNode parent, NodeSpan span)
		{
			super(parent);

			synchronized (span)
			{
				this.span = span;

				for(Span sp : span)
				{
					TranscriptionElement te;

					if (sp.getClass() == TextSpan.class)
					{
						te = new TranscriptionNode(this, (NodeSpan)sp);
					}
					else
					{
						te = new TranscriptionLeaf(this, (TextSpan)sp);
					}

					elements.addLast(te);
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.text.Element#getElement(int)
		 */
		@Override
		public Element getElement(int arg0)
		{
			return elements.get(arg0);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.text.Element#getElementCount()
		 */
		@Override
		public int getElementCount()
		{
			return elements.size();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.text.Element#getElementIndex(int)
		 */
		@Override
		public int getElementIndex(int arg0)
		{
			int result = 0;

			for (TranscriptionElement te : elements)
				if (te.getStartOffset() > arg0)
					return result;
				else
				{
					result++;
				}

			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.text.Element#getEndOffset()
		 */
		@Override
		public int getEndOffset()
		{
			return elements.get(elements.size() - 1).getEndOffset() + 1;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.text.Element#getName()
		 */
		@Override
		public String getName()
		{
			return Messages.getString("TranscriptionDocument.0"); //$NON-NLS-1$
		}

		@Override
		public void insertString(int offset, String str)
			throws BadLocationException
			{
			int start = getStartOffset();
			int end = getEndOffset();

			if(offset < start || offset > end)
				throw new BadLocationException(Messages.getString("Exception.offsetOutOfBound"), offset); //$NON-NLS-1$

			for(TranscriptionElement te : elements)
			{
				try
				{
					te.insertString(offset, str);
					break;
				}
				catch (BadLocationException e)
				{
					// No need to report it.
				}
			}
			}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.text.Element#isLeaf()
		 */
		@Override
		public boolean isLeaf()
		{
			return false;
		}


		@Override
		public void remove(int offs, int len)
			throws BadLocationException
			{
			// TODO Auto-generated method stub

			}

		@Override
		public String toString()
		{
			String result = "\u202f"; //$NON-NLS-1$

			for (TranscriptionElement te : elements)
			{
				result += te.toString();
			}

			result += '\u202f';

			return result;
		}
	}

	/**
	 * @author toxn
	 *
	 */
	public class TranscriptionPosition
	implements Position
	{

		private int offset;

		public TranscriptionPosition(int offset)
			throws BadLocationException
			{
			Element root = getDefaultRootElement();
			if (offset < root.getStartOffset() || offset > root.getEndOffset())
				throw new BadLocationException(Messages.getString("Exception.offsetOutOfBound"), offset); //$NON-NLS-1$
			}
		/**
		 * @see javax.swing.text.Position#getOffset()
		 */
		@Override
		public int getOffset()
		{
			return offset;
		}

	}
	private final TranscriptionElement root = null;

	private final Set<DocumentListener> docListeners = new HashSet<>();
	private final Set<UndoableEditListener> undoListener = new HashSet<>();

	@Override
	public void addDocumentListener(DocumentListener arg0)
	{
		docListeners.add(arg0);
	}


	@Override
	public void addUndoableEditListener(UndoableEditListener arg0)
	{
		undoListener.add(arg0);

	}

	@Override
	public Position createPosition(int offs)
		throws BadLocationException
		{
		return new TranscriptionDocument.TranscriptionPosition(offs);
		}

	@Override
	public Element getDefaultRootElement()
	{
		return root;
	}

	@Override
	public Position getEndPosition()
	{
		try
		{
			return new TranscriptionPosition(root.getEndOffset());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int getLength()
	{
		return root.getEndOffset();
	}

	@Override
	public Object getProperty(Object key)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element[] getRootElements()
	{
		TranscriptionElement[] result = new TranscriptionElement[1];
		result[0] = (TranscriptionElement)getDefaultRootElement();
		return result;
	}

	@Override
	public Position getStartPosition()
	{
		try
		{
			return new TranscriptionPosition(root.getStartOffset());
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getText(int offset, int length)
		throws BadLocationException
		{
		return root.toString().substring(offset, offset + length);
		}

	@Override
	public void getText(int offset, int length, Segment txt)
		throws BadLocationException
		{
		txt.array = root.toString().substring(offset, offset + length).toCharArray();

		}

	@Override
	public void insertString(int offset, String str, AttributeSet a)
		throws BadLocationException
		{
		root.insertString(offset, str);
		}

	@Override
	public void putProperty(Object key, Object value)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(int offs, int len)
		throws BadLocationException
		{
		root.remove(offs, len);

		}



	@Override
	public void removeDocumentListener(DocumentListener arg0)
	{
		docListeners.remove(arg0);
	}

	@Override
	public void removeUndoableEditListener(UndoableEditListener arg0)
	{
		undoListener.remove(arg0);
	}
	@Override
	public void render(Runnable r)
	{
		// TODO Auto-generated method stub

	}
}
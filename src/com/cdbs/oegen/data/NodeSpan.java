/**
 * 
 */
package com.cdbs.oegen.data;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;

/**
 * @author toxn
 *
 */
public class NodeSpan
extends Span
implements Cloneable, Iterable<Span>, Collection<Span>, Deque<Span>, List<Span>, Queue<Span>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LinkedList<Span> subSpans;

	@Override
	public void add(int arg0, Span arg1)
	{
		subSpans.add(arg0, arg1);
	}

	@Override
	public boolean add(Span arg0)
	{
		return subSpans.add(arg0);
	}

	@Override
	public boolean addAll(Collection<? extends Span> arg0)
	{
		return subSpans.addAll(arg0);
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends Span> arg1)
	{
		return subSpans.addAll(arg0, arg1);
	}

	@Override
	public void addFirst(Span arg0)
	{
		subSpans.addFirst(arg0);
	}

	@Override
	public void addLast(Span arg0)
	{
		subSpans.addLast(arg0);
	}

	@Override
	public void clear()
	{
		subSpans.clear();
	}

	@Override
	public boolean contains(Object arg0)
	{
		return subSpans.contains(arg0);
	}

	@Override
	public boolean containsAll(Collection<?> arg0)
	{
		return subSpans.containsAll(arg0);
	}

	@Override
	public Iterator<Span> descendingIterator()
	{
		return subSpans.descendingIterator();
	}

	@Override
	public Span element()
	{
		return subSpans.element();
	}

	@Override
	public Span get(int arg0)
	{
		return subSpans.get(arg0);
	}

	@Override
	public Span getFirst()
	{
		return subSpans.getFirst();
	}

	@Override
	public Span getLast()
	{
		return subSpans.getLast();
	}

	@Override
	public int indexOf(Object arg0)
	{
		return subSpans.indexOf(arg0);
	}

	@Override
	public boolean isEmpty()
	{
		return subSpans.isEmpty();
	}

	@Override
	public Iterator<Span> iterator()
	{
		return subSpans.iterator();
	}

	@Override
	public int lastIndexOf(Object arg0)
	{
		return subSpans.lastIndexOf(arg0);
	}

	@Override
	public ListIterator<Span> listIterator()
	{
		return subSpans.listIterator();
	}

	@Override
	public ListIterator<Span> listIterator(int arg0)
	{
		return subSpans.listIterator(arg0);
	}

	@Override
	public boolean offer(Span arg0)
	{
		return subSpans.offer(arg0);
	}

	@Override
	public boolean offerFirst(Span arg0)
	{
		return subSpans.offerFirst(arg0);
	}

	@Override
	public boolean offerLast(Span arg0)
	{
		return subSpans.offerLast(arg0);
	}

	@Override
	public Span peek()
	{
		return subSpans.peek();
	}

	@Override
	public Span peekFirst()
	{
		return subSpans.peekFirst();
	}

	@Override
	public Span peekLast()
	{
		return subSpans.peekLast();
	}

	@Override
	public Span poll()
	{
		return subSpans.poll();
	}

	@Override
	public Span pollFirst()
	{
		return subSpans.pollFirst();
	}

	@Override
	public Span pollLast()
	{
		return subSpans.pollLast();
	}

	@Override
	public Span pop()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void push(Span arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Span remove()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Span remove(int arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Object arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Span removeFirst()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeFirstOccurrence(Object arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Span removeLast()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeLastOccurrence(Object arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Span set(int arg0, Span arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Span> subList(int arg0, int arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.cdbs.oegen.data.Span#toString()
	 */
	@Override
	public String toString()
	{
		String result = ""; //$NON-NLS-1$
		for(Span s : subSpans)
		{
			result.concat(s.toString());
		}

		return result;
	}
}

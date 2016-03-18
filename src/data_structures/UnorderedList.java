/*Andrew Ball
 */

package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class UnorderedList<E> implements Iterable<E> {
	private int currentSize;
	private long modCounter;
	
	private class Node<E> {
		E data;
		Node<E> next;
		
		public Node(E data) {
			this.data = data;
			next = null;
		}
	}

	private Node<E> head,tail;
	
	public UnorderedList() {
		head = tail = null;
		modCounter = 0;
		currentSize = 0;
	}
	
	public boolean addFirst(E obj) {
		Node<E> newNode = new Node<E>(obj);
		if (head == null) {
			head = newNode;
		}
		else {
			newNode.next = head;
			head = newNode;
		}	
		currentSize++;
		modCounter++;
		return true;
	}
	
	public boolean addLast(E obj) { 
		Node<E> newNode = new Node<E>(obj);
		if (head == null) {
			head = newNode;
			tail = newNode;
		}
		else {
			tail.next = newNode;
			tail = newNode;
		}
		currentSize++;
		modCounter++;
		return true;
	}

	
	public E removeFirst() {
		if (head == null)
			return null;
		E first = head.data;
		head = head.next;
		if (head == null)
			tail = null;
		currentSize--;
		modCounter++;
		return first;
	}
	
	public E removeLast() {
		if (head == null)
			return null;
		E last = tail.data;
		 if (head == tail) {
			head = tail = null;
		 	return last;}
		Node<E> tmp = head, previous = null;
		while (tmp != tail) {
			previous = tmp;
			tmp = tmp.next;
		}
		tail = previous;
		previous.next = null;
		currentSize--;
		modCounter++;
		return last;
	}
		
	public E remove(E obj) {
		if (head == null) 
			return null;
		Node<E> tmp = head, previous = null;
		while(tmp != null && ((Comparable<E>) tmp.data).compareTo(obj) != 0) {
			previous = tmp;
			tmp = tmp.next;
		}
		if (tmp == null)  // not found
			return null;
		E returnValue = tmp.data;
		if (head == tail) { //one element
			head = tail = null;
		}	
		else if (tmp.next == null) { //at the tail or tmp == tail
			previous.next = null;
			tail = previous;
		}
		else if (previous == null) { //first element
			head = head.next;
		}
		else { // everything else
			previous.next = tmp.next;
		}
		currentSize--;
		modCounter++;
		return returnValue;
	}
	
	public E removeMin() {
		Node<E> previous = null, current = head, bestPrev = null, bestCurr = head;
		while(current != null) {
			if (((Comparable<E>) current.data).compareTo(bestCurr.data) < 0) {
				bestPrev = previous;
				bestCurr = current;
			}
		previous = current;
		current = current.next;
		}
		if (head == null) {
			tail = null;
			return null;
		}
		else if (bestPrev == null)
			head = head.next;
		else if( bestCurr == tail) {
			bestPrev.next = null;
			tail = bestPrev;
			}
		else {
			bestPrev.next = bestCurr.next;
		}
		currentSize--;
		modCounter++;
		return bestCurr.data;
	}
	
	public boolean contains(E obj) {
		Node<E> tmp = head;
		while(tmp != null && ((Comparable<E>) tmp.data).compareTo(obj) != 0) {
			tmp = tmp.next;
		}
		if (tmp == null)
			return false;
		return true;
	}
	
	public E find(E obj) {
		Node<E> tmp = head;
		while(tmp != null && ((Comparable<E>) tmp.data).compareTo(obj) != 0) {
			tmp = tmp.next;
		}
		if (tmp == null)
			return null;
		return tmp.data;
	}

	public void clear() {
		if (currentSize != 0)
			head = tail = null;
			currentSize = 0;
			modCounter++;
	}

	public E peekMin() {
		if (head == null)
			return null;
		Node<E> current = head, tmp = head;
		while(current != null) {
			if (((Comparable<E>) current.data).compareTo(tmp.data) < 0) {
				tmp = current;
			}
			current = current.next;
			}
		return tmp.data; 
	}
	
	public E peekFirst() {
		if (head == null)
			return null;
		return head.data;
	}
	
	public E peekLast() {
		if (head == null)
			return null;
		return tail.data;
	}
	
	public int size() {
		return currentSize;
	}
	
	public Iterator<E> iterator() {
		return new IteratorHelper<E>();
	}
		class IteratorHelper<T> implements Iterator<T> {
			Node<E> current;
			long listCheck;
			
			public IteratorHelper() {
				listCheck = modCounter;
				current = head;
			}

			public boolean hasNext() {
				if( current == null ) 
					return false;
				if( modCounter != listCheck)
					throw new ConcurrentModificationException();
				return true;	
			}

			public T next() {
				if (!hasNext())
					throw new NoSuchElementException();
				E returnValue = current.data;
				current = current.next;
				return (T) returnValue;
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		
	}
		

	
}

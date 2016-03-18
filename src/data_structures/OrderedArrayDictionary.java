/*Andrew Ball
 */

package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class OrderedArrayDictionary<K,V> implements DictionaryADT<K,V> {
	private int currentSize, maxSize;
	private long modCounter;
	private DictionaryEntry<K,V>[] storage;

	  public class DictionaryEntry<E,T> {
	        private E key;
	        private T value;
	        
	        public DictionaryEntry(E k, T v) {
	            key = k;
	            value = v;
	            }
	            
	        public E getKey() {
	            return key;
	            }
	            
	        public T getValue() {
	            return value;
	            }            
	        }
	  
	public OrderedArrayDictionary() {
		currentSize = 0;
		modCounter = 0;
		maxSize = 10000;
		storage = new DictionaryEntry[maxSize];
	}
	
	public OrderedArrayDictionary(int max) {
		currentSize = 0;
		modCounter = 0;
		maxSize = max;
		storage = new DictionaryEntry[maxSize];
	}

	public boolean add(K key, V value) {
		if (currentSize == maxSize)
			return false;
		if (contains(key))
			return false;
		int index = binaryInsert(key, 0, currentSize - 1);
		int shift = currentSize;
		while (index != shift) {
			storage[shift] = storage[shift - 1];
			shift--;
		}
		storage[index] = new DictionaryEntry<K,V> (key, value);
		currentSize++;
		modCounter++;
		return true;
	}
	
	public boolean delete(K key) {
		if (currentSize == 0)
			return false;
		int index = binarySearch(key, 0, currentSize - 1);
		if (index < 0)
			return false;
		currentSize--;
		int move = currentSize;
		int value = index;
		while (move != index) { // go over this and make sure it runs right
			storage[value] = storage[value + 1];
			move--;
			value++;
		}
		modCounter++;
		return true;
	}

	public boolean contains(K key) {
		if (currentSize == 0)
			return false;
		int index = binarySearch(key, 0, currentSize - 1);
		if (index < 0)
			return false;
		return true;
	}


	private int binaryInsert(K key, int eMin, int eMax) {
		if (eMax < eMin)
			return eMin;
		else {
			int mid = (eMin + eMax) / 2;
			if (((Comparable<K>) key).compareTo(storage[mid].key) == 0)
				return mid;
			if (((Comparable<K>) key).compareTo(storage[mid].key) < 0)
				return binaryInsert(key, eMin, mid - 1);
			return binaryInsert(key, mid + 1, eMax);
		}
	}

	private int binarySearch(K key, int eMin, int eMax) {
		if (eMax < eMin)
			return -1; 
		else {
			int mid = (eMin + eMax) / 2;
			if (((Comparable<K>) key).compareTo(storage[mid].key) == 0)
				return mid;
			if (((Comparable<K>) key).compareTo(storage[mid].key) < 0)
				return binarySearch(key, eMin, mid - 1);
			return binarySearch(key, mid + 1, eMax);
		}
	}

	public V getValue(K key) {
		if (currentSize == 0)
			return null;
		int index = binarySearch(key, 0, currentSize - 1);
		if (index < 0)
			return null;
		return storage[index].value;
	}
	
	public K getKey(V value) { 
		if (currentSize == 0)
			return null;
		for (DictionaryEntry<K,V> i: storage) {
			if (i == null)
				return null;
			else if (((Comparable<V>) value).compareTo(i.value) == 0) 
				return i.key;
		}
		return null;
	}

	public void clear() {
		if (!isEmpty()) {
			DictionaryEntry<K,V>[] tmp = new DictionaryEntry[maxSize]; 
			storage = tmp;
			currentSize = 0;
			modCounter++;
		}
	}
	
	public boolean isFull() {
		return currentSize == maxSize;
	}

	public boolean isEmpty() {
		return currentSize == 0;
	}

	public int size() {
		return currentSize;
	}

	public Iterator<K> keys() {
		return new IteratorKeys();
	}

	public Iterator<V> values() {
		return new IteratorValues();
	}


	class IteratorKeys implements Iterator<K> {
		private int iterIndex;
		private long sizeCheck;

		public IteratorKeys() {
			iterIndex = 0;
			sizeCheck = modCounter;
		}

		public boolean hasNext() {
			if (sizeCheck != modCounter)
				throw new ConcurrentModificationException();
			return iterIndex < currentSize;
		}

		public K next() {
			if (!hasNext())
				throw new NoSuchElementException();
			return storage[iterIndex++].key;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	class IteratorValues implements Iterator<V> {
		private int iterIndex;
		private long sizeCheck;

		public IteratorValues() {
			iterIndex = 0;
			sizeCheck = modCounter;
		}

		public boolean hasNext() {
			if (sizeCheck != modCounter)
				throw new ConcurrentModificationException();
			return iterIndex < currentSize;
		}

		public V next() {
			if (!hasNext())
				throw new NoSuchElementException();
			return storage[iterIndex++].value;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}


}
/******************************************************************************/

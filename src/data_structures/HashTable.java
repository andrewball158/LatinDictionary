/*Andrew Ball
 */

package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class HashTable<K,V> implements DictionaryADT<K,V> {
	private int currentSize, maxSize, tableSize;
	private long modCounter;
	
	private UnorderedList<DictionaryNode<K,V>> [] list;
	
	
	class DictionaryNode<K,V> implements Comparable<DictionaryNode<K,V>> {
		K key;
		V value;
		
		public DictionaryNode(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public int compareTo(DictionaryNode<K,V> node) {
			return ((Comparable <K>)key).compareTo((K)node.key); 
		}
		
		
	}

	public HashTable(int max) {
		currentSize = 0;
		maxSize = max;
		modCounter = 0;
		tableSize = (int) (maxSize * 1.3f);
		list = new UnorderedList[tableSize];
		for(int i = 0; i < tableSize; i++)
			list[i] = new UnorderedList<DictionaryNode<K,V>>();
	}
	
	public HashTable() {
		currentSize = 0;
		maxSize = 10000;
		modCounter = 0;
		tableSize = (int) (maxSize * 1.3f);
		list = new UnorderedList[tableSize];
		for(int i = 0; i < tableSize; i++)
			list[i] = new UnorderedList<DictionaryNode<K,V>>();
	}
	
	private int getIndex(K key) {
		return (key.hashCode()&0x7FFFFFFF) % tableSize;
	}
	
	public boolean contains(K key) {
		if (currentSize == 0)
			return false;
		int where = getIndex(key);
		return list[where].contains(new DictionaryNode<K,V>(key, null));	
	}

	public boolean add(K key, V value) {
		if(currentSize == maxSize) return false;
		int where = getIndex(key);
		if (list[where].contains(new DictionaryNode<K,V>(key,null)))
			return false;
		list[where].addLast(new DictionaryNode<K,V>(key,value));
		currentSize++;
		modCounter++;
		return true;
		
	}

	public boolean delete(K key) {
		if (currentSize == 0)
			return false;
		int where = getIndex(key);
		if (!list[where].contains(new DictionaryNode<K,V>(key, null)))
				return false;
		list[where].remove(new DictionaryNode<K,V>(key, null));
		currentSize--;
		modCounter++;
		return true;
			
	}

	public V getValue(K key) {
		if(currentSize == 0)
			return null;
		DictionaryNode<K,V> tmp = list[getIndex(key)].find(new DictionaryNode<K,V>(key, null));
		if (tmp == null)
			return null;
		return tmp.value;
	}

	public K getKey(V value) {
		if(currentSize == 0)
			return null;
		for (int i = 0; i < tableSize; i++)
			for (DictionaryNode<K,V> n: list[i])
				if (((Comparable<V>)value).compareTo(n.value) == 0)
					return n.key;
		return null;
	}

	public int size() {
		return currentSize;
	}

	public boolean isFull() {
		return currentSize == maxSize; 
	}

	public boolean isEmpty() {
		return currentSize == 0;
	}

	public void clear() {
		if (!isEmpty()) {
		for (int i = 0; i < tableSize; i++)
			list[i].clear();
		currentSize = 0;
		modCounter++;
		}
	}

	public Iterator<K> keys() {
		return new KeyIteratorHelper<K>();
	}

	public Iterator<V> values() {
		return new ValueIteratorHelper<V>();
	}
	
	abstract class IteratorHelper<E> implements Iterator<E> {
		protected DictionaryNode<K,V> [] nodes;
		protected int index;
		protected long modCheck;
		
		public IteratorHelper() {
			nodes = new DictionaryNode[currentSize];
			index = 0;
			int j =  0;
			modCheck = modCounter;
			for (int i = 0; i < tableSize; i++)  
				for(DictionaryNode<K,V> n : list[i])
					nodes[j++] = n;
			nodes = (DictionaryNode<K,V>[]) shellSort(nodes);
		}
		
		private DictionaryNode<K,V>[] shellSort(DictionaryNode<K,V> array[]) {
			nodes = array;
			int in, out, size = nodes.length, h=1;
			DictionaryNode<K,V> temp;
			while (h <= size/3)
				h = h*3+1;
			while (h > 0) {
				for(out=h; out < size; out++) {
					temp = nodes[out];
					in = out;
					while(in > h-1 && (nodes[in-h]).compareTo(temp) >= 0)  {
						nodes[in] = nodes[in-h];
						in -= h;
					}
					nodes[in] = temp;
				}
			h = (h-1)/3;
			}
			return nodes;
		}
	
		public boolean hasNext() {
			if (modCheck != modCounter)
				throw new ConcurrentModificationException();
			return index < currentSize;
		}
		
		public abstract E next();
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
		
		class KeyIteratorHelper<K> extends IteratorHelper<K> {
			public KeyIteratorHelper() {
				super();
			}
			
			public K next() {
				if(!hasNext())
					throw new ConcurrentModificationException();
				return (K) nodes[index++].key;
			}
		}
		
		class ValueIteratorHelper<V> extends IteratorHelper<V> {
			public ValueIteratorHelper() {
				super();
			}
			
			public V next() {
				if(!hasNext())
					throw new ConcurrentModificationException();
				return (V) nodes[index++].value;
			}
		}
	}


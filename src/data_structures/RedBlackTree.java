/*Andrew Ball
 */

package data_structures;

import java.util.Iterator;
import java.util.TreeMap;

public class RedBlackTree<K,V> implements DictionaryADT<K,V> {
	private TreeMap<K,V> tree;
	
	public RedBlackTree() {
		tree = new TreeMap<K,V>();
	}

	public boolean contains(K key) {
		return tree.containsKey(key);
	}

	public boolean add(K key, V value) {
		if (contains(key))
			return false;
		tree.put(key, value);
		return true;	
	}

	public boolean delete(K key) {
		if(!contains(key))
			return false;
		tree.remove(key);
		return true;
	}

	public V getValue(K key) {
		return tree.get(key);
	}

	public K getKey(V value) {
		Iterator<K> kIter = tree.keySet().iterator();
		Iterator<V> vIter = tree.values().iterator();
		K key = null;
		V val = null;
		
		while (kIter.hasNext()) {
			key = kIter.next();
			val = vIter.next();
			if (((Comparable<V>)value).compareTo(val) == 0) {
				return key; 
			}
		}
	return null;	
		
		
	}

	public int size() {
		return tree.size();
	}

	public boolean isFull() {
		return false;
	}

	public boolean isEmpty() {
		return tree.isEmpty();
	}

	public void clear() {
		if(!isEmpty())
			tree.clear();
	}

	public Iterator<K> keys() {
		return tree.keySet().iterator();
	}

	public Iterator<V> values() {
		return tree.values().iterator();
	}
	
	

}

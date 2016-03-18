/*Andrew Ball
 */

package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class BinarySearchTree<K,V> implements DictionaryADT<K,V> {
	private DictionaryNode<K,V> root;
	private int currentSize;
	private long modCounter;
	private K foundKey;
	
	public BinarySearchTree() {
		root = null;
		currentSize = 0;
		modCounter = 0;
	}
	
	private class DictionaryNode<K,V> {
		private K key;
		private V value;
		private DictionaryNode<K,V> leftChild;
		private DictionaryNode<K,V> rightChild;
		
		public DictionaryNode(K k, V v) {
			key = k;
			value = v;
			leftChild = rightChild = null;
		}
	}

	public boolean add(K key, V value) {
		if (contains(key))
			return false;
		if(root == null)
			root = new DictionaryNode<K,V>(key,value);
		else
			insert(key,value,root,null,false);
		currentSize++;
		modCounter++;
		return true;
	}
	
	private void insert(K k, V v, DictionaryNode<K,V> n, DictionaryNode<K,V> parent, boolean wasLeft) {
		if(n == null) {
			if(wasLeft) {
				parent.leftChild = new DictionaryNode<K,V>(k,v);
			}
			else {
			parent.rightChild = new DictionaryNode<K,V>(k,v);
			}
		}
		else if(((Comparable<K>)k).compareTo((K)n.key) < 0) 
			insert(k,v,n.leftChild,n,true);
		else 
			insert(k,v,n.rightChild,n,false);
	}

	public boolean delete(K key) {
		DictionaryNode<K,V> current = findNode(key, root);
		if (current == null)
			return false;
		DictionaryNode<K,V> parent = findParent(key, root);
		DictionaryNode<K,V> child;  
		if (current.rightChild == null && current.leftChild == null) { //leaf nodes & root
			if (parent == null)
				root = null;
			else if (((Comparable<K>)key).compareTo(parent.key) < 0)
				parent.leftChild = null;
			else
				parent.rightChild = null;
		}
		else if (current.rightChild == null) { // left child
			if (((Comparable<K>)key).compareTo(root.key) == 0)
				root = current.leftChild;
			else if (((Comparable<K>)current.key).compareTo(parent.key) > 0) {
				child = current.leftChild;
				parent.rightChild = child;
			}
			else {
			child = current.leftChild;
			parent.leftChild = child;
			}	
		}
		else if (current.leftChild == null) { // right child
			if (((Comparable<K>)key).compareTo(root.key) == 0) 
				root = current.rightChild;
			else if (((Comparable<K>)current.key).compareTo(parent.key) < 0) {
				child = current.rightChild;
				parent.leftChild = child;
			}
			else {
			child = current.rightChild;
			parent.rightChild = child;
			}  
		}
		else { //two children
			parent = null;
			child = current.rightChild;
			while (child.leftChild != null) { // inorder successor
				parent = child;
				child = parent.leftChild;
			}
			if (parent == null) {
				current.key = child.key;
				current.value = child.value;
				current.rightChild = child.rightChild;
			}
			else {
			current.key = child.key;
			current.value = child.value;
			parent.leftChild = child.rightChild;
			}
		}
		currentSize--;
		modCounter++;
		return true;	
	}

	private DictionaryNode<K,V> findNode(K key, DictionaryNode<K,V> node) {
		if (root == null)
			return null;
		while (((Comparable<K>)node.key).compareTo((K)key) != 0) {
			if(((Comparable<K>)key).compareTo((K)node.key) < 0)
				node = node.leftChild;
			else
				node = node.rightChild;
			if (node == null)
				return null;
		}
		return node;
	}

	private DictionaryNode<K,V> findParent(K key, DictionaryNode<K, V> node) {
		DictionaryNode<K,V> parent = null;
		while (((Comparable<K>)node.key).compareTo((K)key) != 0) {
			parent = node;
			if(((Comparable<K>)key).compareTo((K)node.key) < 0)
				node = node.leftChild;
			else
				node = node.rightChild;
		}
		return parent;
	}
	
	public V getValue(K key) {
		if (currentSize == 0)
			return null;
		return find(key, root);
	}
	
	private V find(K key, DictionaryNode<K,V> n) {
		if(n == null) return null;
		if (((Comparable<K>)key).compareTo(n.key) < 0)
			return find(key, n.leftChild);
		if (((Comparable<K>)key).compareTo(n.key) > 0)
				return find(key, n.rightChild);
		return (V) n.value;
	}
	
	public boolean contains(K key) {
		if (currentSize == 0)
			return false;
		return findIf(key,root);
	}
	
	private boolean findIf(K key, DictionaryNode<K,V> n) {
		if(n == null) return false;
		if (((Comparable<K>)key).compareTo(n.key) < 0) {
			return findIf(key, n.leftChild);
		}
		if (((Comparable<K>)key).compareTo(n.key) > 0)
				return findIf(key, n.rightChild);
		return true;
	}
	
	public K getKey(V value) {
		foundKey = null;
		findKey(value, root);
		return foundKey;
	}
	
	private void findKey(V value, DictionaryNode<K,V> n) {
		if (n==null) return;
		if (((Comparable<V>)value).compareTo(n.value) == 0) {
			foundKey = n.key;
			return;
		}
		findKey(value,n.rightChild);
		findKey(value,n.leftChild);	
	}

	public int size() {
		return currentSize;
	}

	public boolean isFull() {
		return false;
	}

	public boolean isEmpty() {
		return currentSize == 0;
	}

	public void clear() {
		if(!isEmpty()) {
			root = null;
			currentSize = 0;
			modCounter++;
		}
		
	}

	public Iterator<K> keys() {
		return new KeyIteratorHelper();
	}

	public Iterator<V> values() {
		return new ValueIteratorHelper();
	}
	
	abstract class IteratorHelper<E> implements Iterator<E> {
		protected DictionaryNode<K,V> [] array; 
		protected int index;
		protected long modCheck;
		private int j;
		
		public IteratorHelper() {
			array = new DictionaryNode[currentSize];
			index = 0;
			j =  0;
			modCheck = modCounter;
			inOrderFillArray(root);
		}
		
		
		private void inOrderFillArray(DictionaryNode<K,V> n) {
			if (n==null) return;
			inOrderFillArray(n.leftChild);
			array[j++] = n;
			inOrderFillArray(n.rightChild);
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
		
		class KeyIteratorHelper extends IteratorHelper<K> {
			public KeyIteratorHelper() {
				super();
			}
			
			public K next() {
				if(!hasNext())
					throw new ConcurrentModificationException();
				return (K) array[index++].key;
			}
		}
		
		class ValueIteratorHelper extends IteratorHelper<V> {
			public ValueIteratorHelper() {
				super();
			}
			
			public V next() {
				if(!hasNext())
					throw new ConcurrentModificationException();
				return (V) array[index++].value;
			}
		}

}
